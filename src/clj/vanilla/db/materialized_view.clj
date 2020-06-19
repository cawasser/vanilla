(ns vanilla.db.materialized-view
  (:require [dk.ative.docjure.spreadsheet :refer :all]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.edn :as edn]
            [clojure.tools.logging :as log]

            [clojure.set :refer [union]]))



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
  "compute all the :add events (anytime some thing changes, so again 'start' and 'end')"
  [s type]
  {:epoch (:start-epoch s) :event :add :data (assoc s :type type)})



(defn- extract-remove-event
  "compute all the :remove events (anytime some thing changes, so again 'start' and 'end')"
  [s type]
  {:epoch (:end-epoch s) :event :remove :data (assoc s :type type)})



(defn- events-for
  "get the events for a given epoch"

  [events epoch]
  (filter #(= epoch (:epoch %)) events))



(defn- apply-event
  "update the accum by either adding (conj) ore removing (disj) an event"

  [accum event]
  (condp = (:event event)
    :add (conj accum (:data event))
    :remove (disj accum (:data event))))



(defn- apply-epoch-events
  "apply all the events for a given epoch into the state-map"

  [accum events]
  (loop [a accum
         e events]
    ;(prn "apply-epoch-events" a)
    (if (empty? e)
      a
      (recur (apply-event a (first e)) (rest e)))))


; we need to get each invocation to start with the results of the LAST invocation
;
; so well use and atom to gather the state as we apply the change events
;
(defn- empty-state-map
  "create a map where each key is an epoch"

  [epochs]
  (zipmap epochs (repeat #{})))





(defn apply-events
  "accumulate the changes form each add/remove event, with each 'epoch'
  staring with the final result of previous one"

  [events]

  (let [state-map (atom {})
        last-accum (atom #{})]
    (doall
      (for [epoch (sort (into #{} (map #(:epoch %) events)))]
        (let [current-accum (apply-epoch-events @last-accum (events-for events epoch))]
          ;(prn @last-accum "/" current-accum)
          (swap! state-map assoc epoch current-accum)
          (reset! last-accum current-accum))))
    ;(prn "apply-event" @state-map)
    @state-map))



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

(defn- sat-tag [id]
  (condp = id
    sat-1 "-"
    sat-2 "_"
    :default ".."))



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
                       (apply concat (map #(if (= type (:type %))
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

(defn- terminal-location
  "build the data needed to draw a terminal on a layer in the
  WorldWind widget on the client"

  [{:keys [tx-term-id tx-term-lat tx-term-lon
           rx-term-id rx-term-lat rx-term-lon]}]

  [{:name tx-term-id :lat (Double/valueOf tx-term-lat) :lon (Double/valueOf tx-term-lon)}
   {:name rx-term-id :lat (Double/valueOf rx-term-lat) :lon (Double/valueOf rx-term-lon)}])


(defn- build-terminal-locations
  "for each set of events, get the locations of all the terminals"

  [epoch type events]
  {:name epoch
   :data (into #{}
           (remove nil?
             (apply concat (map #(if (= type (:type %))
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
             (map #(if (= type (:type %))
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

(defn- beam-data
  "build the data needed to present a ka-beam location in a timeline widget"

  [band {:keys [satellite-id beam-id radius lat lon type]}]

  {:name         (str (condp = satellite-id
                        sat-1 "1"
                        sat-2 "2"
                        :default "?")
                   "-" band "-" beam-id)
   :lat          lat
   :lon          lon
   :satellite-id satellite-id
   :e            {:diam    (* radius 2)
                  :purpose type}})



(defn- build-beam-data
  "for each set of events, get the locations of all the Ka beams"

  [band epoch type events]
  ;(prn "build-beam-data" epoch type)
  {:name epoch
   :data (into #{}
           (remove nil?
             (map #(if (and (= type (:type %))
                         (= band (:band %)))
                     (beam-data band %))
               events)))})



(defn- get-state []
  ;(log/info "Loading Materialized View")
  (->> [signal-path-context beam-context]
    (get-all-events)
    (apply-events)))




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
   :type       :TERMINAL
   :column-map {:A :satellite-id :B :tx-beam :C :tx-channel :D :rx-beam
                :E :rx-channel :F :plan-id :G :mission-id :K :tx-term-lat
                :L :tx-term-lon :N :rx-term-lat :O :rx-term-lon :Q :tx-term-id
                :R :rx-term-id :S :start-epoch :T :end-epoch :U :data-rate}
   :post-fn    (fn [x] x)})

(def beam-context
  {:file       "resources/public/excel/Demo CONUS.xlsx"
   :sheet      "Beams"
   :type       :BEAM
   :column-map {:A :satellite-id
                :B :band
                :C :beam-id
                :D :lat
                :E :lon
                :F :radius
                :G :start-epoch
                :H :end-epoch
                :J :type}
   :post-fn    (fn [x] x)})


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; PUBLIC API functions
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;


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


(defn terminal-location-query []
  (event-query (get-state) :TERMINAL build-terminal-locations))



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


