(ns vanilla.db.materialized-view
  (:require [dk.ative.docjure.spreadsheet :refer :all]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.edn :as edn]
            [clojure.tools.logging :as log]
            [datascript.core :as d]
            [clojure.set :refer [union]]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; 'context' for importing data from excel
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def excel-filename "public/excel/Demo CONUS.xlsx")

(def signal-path-context
  {:sheet      "SCN_NETWORK_CARRIER_VW"
   :data-type  :TERMINAL
   :column-map {:A :satellite-id :B :tx-beam :C :tx-channel :D :rx-beam
                :E :rx-channel :F :plan-id :G :mission-id :K :tx-term-lat
                :L :tx-term-lon :N :rx-term-lat :O :rx-term-lon :Q :tx-term-id
                :R :rx-term-id :S :start-epoch :T :end-epoch :U :data-rate}
   :post-fn    (fn [x] x)})

(def beam-context
  {:file       "public/excel/Demo CONUS.xlsx"
   :sheet      "Beams"
   :data-type  :BEAM
   :column-map {:A :satellite-id
                :B :band
                :C :beam-id
                :D :lat
                :E :lon
                :F :radius
                :G :start-epoch
                :H :end-epoch
                :J :beam-type}
   :post-fn    (fn [x] x)})


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; 'PRIVATE' 'helper' functions
;
;   i.e., the stuff that makes it work
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def sat-1 "SAT1")
(def sat-2 "SAT2")

; TODO - migrate to the same connection in excel
(def conn (d/create-conn {}))



(defn merge-data-sets
  "the default merge implementation, used whenever there is NO :merge-fn defined in the parameter
  to (query-thread), which is mapped over all all the results as the very last step in the query-thread

  does a simple (into #{}) for all the values (v) of the epoch (k)"

  [[k v]]
  {:name k
   :data (into #{} v)})


(defn- extract-add-event
  "compute all the :add events (i.e., when something 'starts')"

  [type s]
  {:epoch (:start-epoch s) :event :add :data (assoc s :data-type type)})



(defn- extract-remove-event
  "compute all the :remove events (i.e., when something 'ends')"

  [type s]
  {:epoch (:end-epoch s) :event :remove :data (assoc s :data-type type)})



(defn- events-for
  "get all the events for a given epoch"

  [events epoch]
  (filter #(= epoch (:epoch %)) events))



(defn- apply-event
  "update the accum by either adding (conj) ore removing (disj) an event"

  [accum event]
  (condp = (:event event)
    :add (conj accum (:data event))
    :remove (disj accum (:data event))))



(defn- apply-epoch-events
  "apply all the events for a given epoch into the accum-ulator"

  [accum events]
  (loop [a accum
         e events]
    ;(prn "apply-epoch-events" a)
    (if (empty? e)
      a
      (recur (apply-event a (first e)) (rest e)))))



(defn- add-epoch
  "this little helper function just adds the epoch as an explicit keye/vale
  in the hash-map"

  [[k v]]
  (for [data v]
    (assoc data :epoch k)))



(defn apply-events
  "accumulate the changes from each add/remove event, with each 'epoch'
  starting with the accumulated state from then previous epoch"

  [events]

  (log/info "Constructing materialized view")

  (let [state-map  (atom {})
        last-accum (atom #{})]
    (doall
      (for [epoch (sort (into #{} (map #(:epoch %) events)))]
        (let [current-accum (->> epoch
                              (events-for events)
                              (apply-epoch-events @last-accum))]
          (swap! state-map assoc epoch current-accum)
          (reset! last-accum current-accum))))
    @state-map))



(defn- get-all-events
  "get all the data from the spreadsheet(s) into a single flat vector of
   add and remove events. this vector will then be materialized into views
   by 'epoch'


   NOTE: 'juxt' is a HOF that runs each param-function separately on the input and puts
         all the results into a collection

   NOTE: we combine partial with juxt because the functions in juxt require an additional 'context' parameter as well as
         the data-item to be processes (the 2nd argument). 'partial' let's us pre-mix this parameter into each call beforehand,
         so when we call the juxt'd functions with 'map' they only need the data-item itself.

         see the 'rich comment' at line 535"

  [sources]

  (log/info "Get events from excel" excel-filename)

  (try
    (with-open [workbook (load-workbook-from-resource excel-filename)]
      (apply concat
        (for [context sources]
          (->> workbook
            (select-sheet (:sheet context))
            (select-columns (:column-map context))
            (drop 1)                                        ; the header row
            (map (partial (juxt extract-add-event extract-remove-event) (:data-type context)))
            flatten))))

    (catch Exception e (log/error "Exception: " (.getMessage e)))

    (finally (log/info "Excel file" excel-filename "loaded!"))))




;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;
; PUBLIC API functions
;
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-state
  "creates a 'materialized view' of the events. accumulates the adds and removes at each epoch boundary,
  creating a snapshot of the current state which is constant throughout that epoch. The subsequent
  epoch starts with the state from the prior epoch, hence the accumulation

  updates the datascript database in 'conn'"

  []

  (log/info "Loading materialized view into Datascript")

  (->> [signal-path-context beam-context]
    (get-all-events)
    (apply-events)
    (map add-epoch)
    (apply concat)
    (d/transact! conn)))



(defn query-thread
  "this function chains together the query (:q-fn), a function to
  pick out the fields you want (with any renaming you need), called :map-fn
  and a function to combine all the results for a given epoch into a single
  data collection, usually a vector or set (:merge-fn)

  takes a map with up to 3 keys:

  :q-fn (required)   - the datascript query to get the data out of the database, do whatever you want here

  :map-fn (optional) - a function (technically a transducer) to map each result into the correct format
                       for the data-source. maybe you need just some of the fields, or you need to change
                       the 'name' (keyword) to return the data to the client

                       expects this function to produce results in the form:
                           {:epoch 'epoch-string' :data <whatever collection you prefer>}

                       if you don't provide a :map-fn, query-thread will use 'identity' as the default

  :merge-fn (optional) - a function (again a transducer) to combine the :data results for each :epoch
                         into a single collection. the data passed into this transducer will be a collection
                         of collections.

                         if not provided it will use (merge-data-sets) as the default

                         Note: merge-data-sets will include an :epoch value in each data map; just ignore it"

  [{q-fn :q-fn map-fn :map-fn merge-fn :merge-fn
    :or  {map-fn identity merge-fn merge-data-sets}}]

  ;(prn "map-fn" map-fn "merge-fn" merge-fn)
  (->> q-fn
    (sequence map-fn)
    (group-by :epoch)
    sort
    (map merge-fn)))


; work out the materialized view logic (applying the adds and removes)
(comment


  (count (get-all-events [beam-context]))
  (count (get-all-events [signal-path-context]))

  (def all-events (get-all-events [beam-context signal-path-context]))

  (= (first all-events)
    (second all-events))
  (count all-events)

  (sort (into #{} (map #(:epoch %) all-events)))

  ;(def event-state (atom {}))
  (def source-context beam-context)
  (def source-context signal-path-context)

  (def epoch "161200Z JUL 2020")
  (def last-accum (atom #{}))
  (def current-accum (apply-epoch-events @last-accum (events-for all-events epoch)))


  (def all-events (get-all-events [beam-context signal-path-context]))

  (apply-events event-state all-events)

  (->> [signal-path-context beam-context]
    (get-all-events)
    (apply-events pmt-event-state))



  (->> (map #(:epoch %) all-events)
    (into #{})
    sort)

  (def epoch-of-interest "141807Z JUL 2020")
  (->> (filter #(= (:epoch %) epoch-of-interest) all-events)
    count)
  (->> (get @event-state epoch-of-interest)
    count)

  ())

; work out the datascript implementation for querying the MV
(comment
  ; some example data for clarity
  (def one-epoch
    {"141807Z JUL 2020"
     #{{:tx-term-id  "ORL", :data-type :TERMINAL, :start-epoch "141807Z JUL 2020",
        :data-rate   10240.0, :plan-id "Alpha", :tx-channel 1.0,
        :rx-term-lon "-119.81", :tx-term-lat "28.53", :rx-term-id "MOB1",
        :rx-channel  3.0, :tx-term-lon "-81.4", :mission-id "One",
        :end-epoch   "151807Z JUL 2020", :satellite-id "SAT1", :tx-beam "T3",
        :rx-term-lat "34.79", :rx-beam "R1"}
       {:tx-term-id  "MOB1", :data-type :TERMINAL, :start-epoch "141807Z JUL 2020",
        :data-rate   7168.0, :plan-id "Alpha", :tx-channel 3.0,
        :rx-term-lon "-81.4", :tx-term-lat "34.79", :rx-term-id "ORL",
        :rx-channel  1.0, :tx-term-lon "-119.81", :mission-id "One",
        :end-epoch   "151807Z JUL 2020", :satellite-id "SAT1", :tx-beam "T2",
        :rx-term-lat "28.53", :rx-beam "R3"}}})



  ; get the epochs
  ;(get-state)

  ; flatten into a single vector pf maps that can be put into datascript
  (def flattened (apply concat
                   (map (fn [[k v]]
                          (for [data v]
                            (assoc data :epoch k)))
                     (get-state))))

  ; transact the data into datascript
  (def conn (d/create-conn {}))
  (d/transact! conn (apply concat
                      (map (fn [[k v]]
                             (for [data v]
                               (assoc data :epoch k)))
                        (get-state))))




  ; query for something interesting
  (group-by first
    (d/q '[:find ?epoch ?tx-term-id ?rx-term-id
           :where [_ :epoch ?epoch]
           [?e :tx-term-id ?tx-term-id]
           [?e :rx-term-id ?rx-term-id]]
      @conn))



  ;
  ; helper function for munging some of the data coming out of datascript
  ;
  (defn merge-data-sets [[k v]]
    {:name k
     :data (into #{} v)})


  (merge-data-sets ["221200Z JUL 2020"
                    [{:tx-term-id  "MOB2",
                      :epoch       "221200Z JUL 2020",
                      :data-type   :TERMINAL,
                      :start-epoch "221200Z JUL 2020",
                      :data-rate   2048.0,}
                     {:tx-term-id  "DUL",
                      :epoch       "221200Z JUL 2020",
                      :data-type   :TERMINAL,
                      :start-epoch "221200Z JUL 2020",
                      :data-rate   2048.0,}]])



  ; maybe we can use transducers (my first!)
  ;
  ;    NB: the key here is that (sequence <x-ducer>) changes the format of the result, so
  ;        the group-by must be different
  ;
  (defn query-thread
    "this function chains together the query (:q-fn), a function to
    pick out the fields you want (with any renaming you need), called :map-fn
    and a function to combine all the results for a given epoch into a single
    data collection, usually a vector or set (:merge-fn)

    takes a map with up to 3 keys:

    :q-fn (required)   - the datascript query to get the data out of the database, do whatever you want here

    :map-fn (optional) - a function (technically a transducer) to map each result into the correct format
                         for the data-source. maybe you need just some of the fields, or you need to change
                         the 'name' (keyword) to return the data to the client

                         expects this function to produce results in the form:
                             {:epoch 'epoch-string' :data <whatever collection you prefer>}

                         if you don't provide a :map-fn, query-thread will use 'identity' as the default

    :merge-fn (optional) - a function (again a transducer) to combine the :data results for each :epoch
                           into a single collection. the data passed into this transducer will be a collection
                           of collections.

                           if not provided it will use (merge-data-sets) as the default

                           Note: merge-data-sets will include an :epoch value in each data map; just ignore it"

    [{q-fn :q-fn map-fn :map-fn merge-fn :merge-fn
      :or  {map-fn identity merge-fn merge-data-sets}}]

    ;(prn "map-fn" map-fn "merge-fn" merge-fn)
    (->> q-fn
      (sequence map-fn)
      (group-by :epoch)
      sort
      (map merge-fn)))



  (query-thread
    {:q-fn (d/q '[:find [(pull ?e [*]) ...]
                  :where [?e :tx-term-id ?tx-term-id]]
             @conn)})




  ;
  ;
  ; can we go back to using datascript queries for the services?
  ;
  ; YES!!!!!
  ;
  ;



  (= (map :name (get-beam-data "Ka")) (map :name (beam-query "Ka")))
  (clojure.set/difference
    (into #{} (map :name (get-beam-data "Ka")))
    (into #{} (map :name (beam-query "Ka"))))
  (clojure.set/difference
    (into #{} (map :name (beam-query "Ka")))
    (into #{} (map :name (get-beam-data "Ka"))))
  ; only missing the empty epoch "230000Z JUL 2020"



  ;
  ;
  ; TODO: move code to terminal-location-service
  ;
  ;

  (get-terminal-location-data)
  (= (get-terminal-location-data) (terminal-location-query))

  (= (map :name (get-terminal-location-data)) (map :name (terminal-location-query)))
  (clojure.set/difference
    (into #{} (map :name (get-terminal-location-data)))
    (into #{} (map :name (terminal-location-query))))
  (clojure.set/difference
    (into #{} (map :name (terminal-location-query)))
    (into #{} (map :name (get-terminal-location-data))))
  ; only missing the empty epoch "230000Z JUL 2020"



  ;
  ;
  ; TODO: move to signal-path-service
  ;


  (= (get-signal-path-data) (signal-path-query))
  (= (map :name (get-signal-path-data)) (map :name (signal-path-query)))

  (clojure.set/difference
    (into #{} (map :name (get-signal-path-data)))
    (into #{} (map :name (signal-path-query))))
  (clojure.set/difference
    (into #{} (map :name (signal-path-query)))
    (into #{} (map :name (get-signal-path-data))))
  ; only missing the empty epoch "230000Z JUL 2020"



  ;
  ;
  ; mission-data
  ;
  ;    TODO: migrate to task-service
  ;

  (defn get-mission-data
    ""

    []
    (->> (d/q '[:find [(pull ?e [*]) ...]
                :where [?e :mission-id]]
           @conn)
      (map mission-data)
      (map #(assoc %
              :start-set #{(:start %)}
              :end-set #{(:end %)}))
      (group-by :id)
      (map (fn [[k v]] (merge-mission v)))))

  (get-mission-data)

  (= (get-mission-data) (mission-query))
  (clojure.set/difference
    (into #{} (map :name (get-mission-data)))
    (into #{} (map :name (mission-query))))
  (clojure.set/difference
    (into #{} (map :name (mission-query)))
    (into #{} (map :name (get-mission-data))))
  ; only missing the empty epoch "230000Z JUL 2020"



  ())



(comment

  (def events (with-open [workbook (load-workbook-from-resource excel-filename)]
                (for [context [signal-path-context beam-context]]
                  (->> workbook
                    (select-sheet (:sheet signal-path-context))
                    (select-columns (:column-map signal-path-context))
                    (drop 1)))))
  ;(map (juxt #(extract-add-event (:data-type signal-path-context) %)
  ;       #(extract-remove-event (:data-type signal-path-context) %)))))))

  (= (map (juxt #(extract-add-event (:data-type signal-path-context) %)
            #(extract-remove-event (:data-type signal-path-context) %))
       (first events))

    (map (partial (juxt extract-add-event extract-remove-event) (:data-type signal-path-context))
      (first events)))

  ())