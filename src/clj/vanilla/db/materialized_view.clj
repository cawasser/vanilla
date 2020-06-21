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

(def signal-path-context
  {:file       "resources/public/excel/Demo CONUS.xlsx"
   :sheet      "SCN_NETWORK_CARRIER_VW"
   :data-type  :TERMINAL
   :column-map {:A :satellite-id :B :tx-beam :C :tx-channel :D :rx-beam
                :E :rx-channel :F :plan-id :G :mission-id :K :tx-term-lat
                :L :tx-term-lon :N :rx-term-lat :O :rx-term-lon :Q :tx-term-id
                :R :rx-term-id :S :start-epoch :T :end-epoch :U :data-rate}
   :post-fn    (fn [x] x)})

(def beam-context
  {:file       "resources/public/excel/Demo CONUS.xlsx"
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

(defn- extract-add-event
  "compute all the :add events (i.e., when something 'starts')"

  [s type]
  {:epoch (:start-epoch s) :event :add :data (assoc s :data-type type)})



(defn- extract-remove-event
  "compute all the :remove events (i.e., when something 'ends')"

  [s type]
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


;; we need to get each invocation to start with the results of the LAST invocation
;;
;; so well use and atom to gather the state as we apply the change events
;;
;(defn- empty-state-map
;  "create a map where each key is an epoch"
;
;  [epochs]
;  (zipmap epochs (repeat #{})))
;
;
;
(defn apply-events
  "accumulate the changes from each add/remove event, with each 'epoch'
  starting with the acumulated state from then previous epoch"

  [events]

  (let [state-map  (atom {})
        last-accum (atom #{})]
    (doall
      (for [epoch (sort (into #{} (map #(:epoch %) events)))]
        (let [current-accum (apply-epoch-events @last-accum (events-for events epoch))]
          ;(prn @last-accum "/" current-accum)
          (swap! state-map assoc epoch current-accum)
          (reset! last-accum current-accum))))
    ;(prn "apply-event" @state-map)
    @state-map))



(defn- get-all-events
  "get all thee data from the spreadheet(s) into a single flat vector of
   add and remove events. this vector will then be materialized into views
   by 'epoch'"

  [sources]
  (apply concat
    (for [source-context sources]
      (->> (load-workbook (:file source-context))
        (select-sheet (:sheet source-context))
        (select-columns (:column-map source-context))
        (drop 1)
        (map (fn [s]
               [(extract-add-event s (:data-type source-context))
                (extract-remove-event s (:data-type source-context))]))
        flatten))))



; TODO - drop event-query
(defn- event-query
  "use the event-state as the source of data for the query.

  for each epoch in an epoch-map (the 'event-state'), call the 'build-fn'
  to generate the appropriate data"

  [event-state type build-fn]

  (into []
    (sort-by :name
      (for [[epoch events] event-state]
        (build-fn epoch type events)))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; signal-path support functions
;
;   designed to provide data compatible with
;   the SANKEY chart widget
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TODO - migrate sat-tag to signal-path-service
(defn- sat-tag [id]
  (condp = id
    sat-1 "-"
    sat-2 "_"
    :default ".."))


; TODO - drop signal-path
(defn- signal-path
  "build the data needed for a Sankey diagram. we must add the 't' and 'r'
  as well as a notation for the specific satellite to make the names
  distinct, otherwise we the diagram think they are the
  same thing, even if one is 'transmit' and the other is 'receive'"

  [{:keys [satellite-id tx-beam tx-channel rx-beam
           rx-channel tx-term-id rx-term-id data-rate]}]
  (let [tx (str "t" (sat-tag satellite-id))
        rx (str "r" (sat-tag satellite-id))]
    [[(str tx tx-term-id) (str tx tx-channel) data-rate]
     [(str tx tx-channel) (str tx tx-beam) data-rate]
     [(str tx tx-beam) satellite-id data-rate]
     [satellite-id (str rx rx-beam) data-rate]
     [(str rx rx-beam) (str rx rx-channel) data-rate]
     [(str rx rx-channel) (str rx rx-term-id) data-rate]]))


; TODO - drop build-signal-path
(defn- build-signal-path
  "for each set of events, ge the 'path' data for a sankey chart"

  [epoch type events]
  {:name         epoch
   :showInLegend true
   :type         :sankey
   :keys         ["from" "to" "weight"]
   :data         (into []
                   (sort-by (juxt (fn [x] (get x 0))
                              (fn [x] (get x 1)))
                     (remove nil?
                       (apply concat (map #(if (= type (:data-type %))
                                             (signal-path %))
                                       events)))))})


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; terminal-location support functions
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TODO - drop terminal-location
(defn- terminal-location
  "build the data needed to draw a terminal on a layer in the
  WorldWind widget on the client"

  [{:keys [tx-term-id tx-term-lat tx-term-lon
           rx-term-id rx-term-lat rx-term-lon]}]

  [{:name tx-term-id :lat (Double/valueOf tx-term-lat) :lon (Double/valueOf tx-term-lon)}
   {:name rx-term-id :lat (Double/valueOf rx-term-lat) :lon (Double/valueOf rx-term-lon)}])


; TODO - drop build-terminal-locations
(defn- build-terminal-locations
  "for each set of events, get the locations of all the terminals"

  [epoch type events]
  {:name epoch
   :data (into #{}
           (remove nil?
             (apply concat (map #(if (= type (:data-type %))
                                   (terminal-location %))
                             events))))})




;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; mission-list support functions
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def datetime-formatter (f/formatter "ddHHmmz MMM yyyy"))

(defn ->GMT [x] (clojure.string/replace x #"Z" "GMT"))
(defn ->Z [x] (clojure.string/replace x #"UTC" "Z"))
(defn strip-tz [x] (clojure.string/replace x #"UTC|GMT|Z" ""))

(defn- mission-data
  "build the data needed to present a Mission in a timeline widget"

  [{:keys [mission-id plan-id start-epoch end-epoch]}]

  {:id    mission-id :name mission-id :type plan-id
   :start (f/parse datetime-formatter (->GMT start-epoch))
   :end   (f/parse datetime-formatter (->GMT end-epoch))})



(defn- build-mission-data
  "for each set of events, get the locations of all the terminals"

  [epoch type events]
  {:name epoch
   :data (into #{}
           (remove nil?
             (map #(if (= type (:data-type %))
                     (mission-data %))
               events)))})



(defn- merge-mission
  "combine all the mission entries in the argument into a single 'mission'

  returns map of one mission entry:

  :id, :name, :type from the first item

  :start   earliest of all the :start values (converted to strings for transmission)
  :end     latest of all the :end values (converted to strings for transmission)

  ASSUMPTIONS

  - all the :id :name: and :type values are the same"

  [args]
  (if (seq args)
    (let [a (first args)]
      {:id    (:id a)
       :name  (:name a)
       :type  (:type a)
       :start (->> (map #(:start-set %) args)
                (apply union)
                sort
                first
                .toDate)
       :end   (->> (map #(:end-set %) args)
                (apply union)
                sort
                last
                .toDate)})
    {}))



;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; ka-beam-location support functions
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TODO - drop beam-data
(defn- beam-data
  "build the data needed to present a ka-beam location in a timeline widget"

  [band {:keys [satellite-id beam-id radius lat lon beam-type]}]

  {:name         (str (condp = satellite-id
                        sat-1 "1"
                        sat-2 "2"
                        :default "?")
                   "-" band "-" beam-id)
   :lat          lat
   :lon          lon
   :satellite-id satellite-id
   :e            {:diam    (* radius 2)
                  :purpose beam-type}})


; TODO - drop build-beam-data
(defn- build-beam-data
  "for each set of events, get the locations of all the Ka beams"

  [band epoch type events]
  ;(prn "build-beam-data" epoch type)
  {:name epoch
   :data (into #{}
           (remove nil?
             (map #(if (and (= type (:data-type %))
                         (= band (:band %)))
                     (beam-data band %))
               events)))})


; TODO - add a local ds/connection
;(def conn (d/create-conn {}))

(defn- get-state []
  (log/info "Loading Materialized View")
  (->> [signal-path-context beam-context]
    (get-all-events)
    (apply-events)))
; TODO - add data to datascript so we only need to load it ONCE from the spreadsheet(s)
;(apply concat
;  (map (fn [[k v]]
;         (for [data v]
;           (assoc data :epoch k)))))
;(d/transact! conn)))





;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; PUBLIC API functions
;
;   working to get rid of these!
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TODO - drop signal-path-query
(defn signal-path-query []
  (event-query (get-state) :TERMINAL build-signal-path))


(defn mission-query []
  (let [events (event-query (get-state) :TERMINAL build-mission-data)]
    (map
      (fn [[k v]] (merge-mission v))
      (group-by :id
        (map #(assoc % :start-set #{(:start %)}
                :end-set #{(:end %)})
          (apply concat
            (map #(:data %) events)))))))


; TODO - drop terminal-location-query
(defn terminal-location-query []
  (event-query (get-state) :TERMINAL build-terminal-locations))


; TODO - drop beam-query
(defn beam-query [band]
  (event-query (get-state) :BEAM (partial build-beam-data band)))



(comment

  (defn- test-apply-events [event-state all-events]
    (let [last-accum (atom #{})]
      (for [epoch (sort (into #{} (map #(:epoch %) all-events)))]
        (let [current-accum (apply-epoch-events @last-accum (events-for all-events epoch))]
          ;(prn @last-accum "/" current-accum)
          (swap! event-state assoc epoch current-accum)
          (reset! last-accum current-accum)))))


  (defn- get-all-events [sources]
    (apply concat
      (for [source-context sources]
        (->> (load-workbook (:file source-context))
          (select-sheet (:sheet source-context))
          (select-columns (:column-map source-context))
          (drop 1)
          (map (fn [s]
                 [(extract-add-event s (:type source-context))
                  (extract-remove-event s (:type source-context))]))
          flatten))))


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


(comment

  (signal-path-query)

  (mission-query)

  (terminal-location-query)


  (event-query (with-state event-state) :BEAM (partial build-beam-data "Ka"))

  (beam-query "X")
  (beam-query "Ka")


  (vanilla.subscription-manager/refresh-source :signal-path-service)
  (vanilla.subscription-manager/refresh-source :terminal-location-service)
  (vanilla.subscription-manager/refresh-source :x-beam-location-service)
  (vanilla.subscription-manager/refresh-source :ka-beam-location-service)

  (map #(:band %) (val (first @event-state)))

  (into #{}
    (map #(if (and (= :BEAM (:type %))
                (= "Ka" (:band %)))
            (beam-data "Ka" %))
      (val (first @event-state))))


  {:start-epoch  "161200Z JUL 2020",
   :band         "Ka",
   :type         "R",
   :radius       111000.0,
   :lon          29.564,
   :lat          32.633,
   :end-epoch    "230000Z JUL 2020",
   :beam-id      2,
   :satellite-id sat-2}

  ())


(comment
  ka-beam-context

  (def missions (->> (load-workbook (:file signal-path-context))
                  (select-sheet (:sheet signal-path-context))
                  (select-columns (:column-map signal-path-context))
                  (drop 1)))

  (def beams (->> (load-workbook (:file ka-beam-context))
               (select-sheet (:sheet ka-beam-context))
               (select-columns (:column-map ka-beam-context))
               (drop 1)))

  (def row {:tx-term-id   "TRML-1", :start-epoch "222000Z JUL 2020",
            :data-rate    2048.0, :plan-id "PLAN-1",
            :tx-channel   "1", :rx-term-lon "132.22",
            :tx-term-lat  "-14.31", :rx-term-id "TRML-2",
            :rx-channel   "1", :tx-term-lon "32.22",
            :mission-id   "MISSION-1", :end-epoch "230000Z JUL 2020",
            :satellite-id sat-2, :tx-beam "3",
            :rx-term-lat  "-34.31", :rx-beam "3"})
  (:start-epoch row)

  (f/parse (f/formatter "ddHHmm MMM yyyy") "010000 JAN 2020")
  (f/parse (f/formatter "ddHHmmz MMM yyyy") "010000GMT JAN 2020")

  ; this doesn't work, 'Z' not a valid timezone
  (f/parse (f/formatter "ddHHmmz MMM yyyy") "010000Z JAN 2020")

  ; then replace it with GMT, which means the same
  (clojure.string/replace "010000Z JAN 2020" #"Z" "GMT")

  (f/parse (f/formatter "ddHHmmz MMM yyyy")
    (clojure.string/replace "010000Z JAN 2020" #"Z" "GMT"))

  (f/parse (f/formatter "ddHHmmz MMM yyyy")
    (clojure.string/replace (:start-epoch row) #"Z" "GMT"))


  ; work out just getting the missions (we don't need the epoch data)
  (def missions
    (event-query signal-path-context pmt-event-state build-mission-data))
  (apply concat
    (map #(:data %) missions))

  (def smaller-data
    [{:id    "MISSION-1",
      :name  "MISSION-1",
      :type  "PLAN-1",
      :start "2020-07-22T12:00:00.000Z",
      :end   "2020-07-22T16:00:00.000Z"}
     {:id    "MISSION-1",
      :name  "MISSION-1",
      :type  "PLAN-1",
      :start "2020-07-22T16:00:00.000Z",
      :end   "2020-07-28T20:00:00.000Z"}
     {:id    "MISSION-1",
      :name  "MISSION-1",
      :type  "PLAN-1",
      :start "2020-07-22T16:00:00.000Z",
      :end   "2020-07-23T20:00:00.000Z"}])

  (map #(assoc % :start-set #{(:start %)} :end-set #{(:end %)}) smaller-data)

  (def set-data (map #(assoc % :start-set #{(:start %)}
                        :end-set #{(:end %)}) smaller-data))

  ; none of the clojure 'merges' work for what we want, which is the scalar values and
  ;the union of the set values

  (def a (first set-data))
  (def b (second set-data))
  (def args set-data)

  (merge-mission set-data)

  (def intermediate
    (group-by :id
      (map #(assoc % :start-set #{(:start %)}
              :end-set #{(:end %)})
        (apply concat
          (map #(:data %) missions)))))

  (map (fn [[k v]] v) (take 1 intermediate))

  (map
    (fn [[k v]] (merge-mission v))
    (group-by :id
      (map #(assoc % :start-set #{(:start %)}
              :end-set #{(:end %)})
        (apply concat
          (map #(:data %) missions)))))

  (first (sort (apply union (map #(:start-set %) args))))

  (->> (map #(:start-set %) args)
    (apply union)
    sort
    first)
  (->> (map #(:end-set %) args)
    (apply union)
    sort
    last)
  (f/unparse datetime-formatter)

  ())


; can we keep using datascript?
; this will get us out of the business of building the MV for every query like we are now
;
;  no idea why I can't get the (atom) for the MV to work inside a function so it only
;     gets called once, but it always resets to {}
;
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

  ;
  ; TODO: move code to beam-location-service
  ;
  (defn get-beam-data
    "get all beams of a give band, by epoch

    - band  - a string value of the :band of interest

    returns a sequence of maps:

        ({:name <epoch-id>
          :data #{{:name <beam-id> :lat <lat> :lon <lon>
                   :e {:diam <diam> :purpose <data-format>}}})

    see (query-thread) for additional information"

    [band]
    (query-thread
      {:q-fn   (d/q '[:find [(pull ?e [*]) ...]
                      :where [?e :band ?band]
                      [?e :beam-id ?name]
                      :in $ ?band] @conn band)
       :map-fn (map (fn [{:keys [epoch band beam-id lat lon radius beam-type]}]
                      {:epoch epoch
                       :name  (str band beam-id) :lat lat :lon lon
                       :e     {:diam (* radius 2) :purpose beam-type}}))}))

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
  ;   TODO: need to test with Rx or only Tx terminals to be sure we capture them
  ;

  (defn- tx-rx-terminals-by-epoch
    "this is a helper function to combine the results of the Tx and Rx terminal
    into a single result set"

    [a b]
    (for [{:keys [name data]} a]
      (->> (get-in b [name data])
        (clojure.set/union data)
        ((fn [x] {:name name :data x})))))

  (defn get-terminal-location-data
    "get all the terminals (Tx and Rx) by epoch

    returns a sequence maps:

        ({:name <epoch-id>
          :data #{{:epoch <epoch-id>
                   :name <terminal-id>
                   :lat <lat> :lon <lon>}}})"

    []
    (tx-rx-terminals-by-epoch
      ; this gets the Tx terminals
      (->> (d/q '[:find [(pull ?e [*]) ...]
                  :where [?e :tx-term-id ?tx-term-id]]
             @conn)
        (map (fn [{:keys [epoch tx-term-id tx-term-lat tx-term-lon]}]
               {:epoch epoch :name tx-term-id :lat tx-term-lat :lon tx-term-lon}))
        (group-by :epoch)
        sort
        (map merge-data-sets))

      ; and the rx terminals
      (->> (d/q '[:find [(pull ?e [*]) ...]
                  :where [?e :rx-term-id ?rx-term-id]]
             @conn)
        (map (fn [{:keys [epoch rx-term-id rx-term-lat rx-term-lon]}]
               {:epoch epoch :name rx-term-id :lat rx-term-lat :lon rx-term-lon}))
        (group-by :epoch)
        sort
        (map merge-data-sets))))

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

  (defn- signal-path
    "build the data needed for a Sankey diagram. we must add the 't' and 'r'
    as well as a notation for the specific satellite to make the names
    distinct, otherwise we the diagram think they are the
    same thing, even if one is 'transmit' and the other is 'receive'"

    [{:keys [epoch satellite-id tx-beam tx-channel rx-beam
             rx-channel tx-term-id rx-term-id data-rate]}]
    (let [tx (str "t" (sat-tag satellite-id))
          rx (str "r" (sat-tag satellite-id))]
      {:epoch epoch
       :data  [[(str tx tx-term-id) (str tx tx-channel) data-rate]
               [(str tx tx-channel) (str tx tx-beam) data-rate]
               [(str tx tx-beam) satellite-id data-rate]
               [satellite-id (str rx rx-beam) data-rate]
               [(str rx rx-beam) (str rx rx-channel) data-rate]
               [(str rx rx-channel) (str rx rx-term-id) data-rate]]}))



  (defn merge-signal-paths
    "combines the various data collections

    "

    [[k v]]
    {:name k
     :data (sort-by (juxt (fn [x] (get x 0))
                      (fn [x] (get x 1)))
             (remove nil?
               (into []
                 (apply concat
                   (map (fn [{:keys [epoch data]}]
                          data)
                     v)))))})



  (defn get-signal-path-data
    "returns signal-paths:

       tx-terminal -> tx-beam -> tx-channel -> satellite  -> rx-channel -> rx-beam -> rx-terminal

    per epoch.

    returns a sequence of maps:

        ({:name <epoch-id>
          :data ([<from> <to> <weight>]...)})"

    []
    (query-thread
      {:q-fn     (d/q '[:find [(pull ?e [*]) ...]
                        :where [?e :tx-term-id ?tx-term-id]
                        [?e :rx-term-id]]
                   @conn)
       :map-fn   (map signal-path)
       :merge-fn merge-signal-paths}))

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




