(ns vanilla.signal-path-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [vanilla.db.materialized-view :as mv]
            [datascript.core :as d]))


(defn- sat-tag
  "helper to 'unique-ify' the names of intermediate elements fo the signal-path, since
  a user might use the same 'simple name' for things on the Tx side and he Rx side.

  This helps address the 'context-sensitive' nature of human-named things (humans tend to name things within a
  given context, such as 'channel 3', which exists on both the TX and Rx sides o fht path). Inside the
  system these things are different, so their names must be made different.

  Assumption:

  only the :satellite-id is used for uniqueness. beams, antennas, etc are NOT

  only handles 3 satellites"

  [id]
  (condp = id
    mv/sat-1 "-"
    mv/sat-2 "_"
    :default ".."))



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
  "combines the various data collections into the proper format for use in a Highcharts Sankey
  chart, i.e., [<from> <to> <weight>] where

  <from> is starting elements of a segment of the path (tx-terminal, satellite, etc.)

  <to> is ending elements of a segment of the path (tx-terminal, satellite, etc.)

  <weight> is a number used to draw the line thickness, signal-paths use the data-rate

  sorts the data by the <from> then the <to>"

  [[k v]]
  {:name         k
   :showInLegend true
   :type         :sankey
   :keys         ["from" "to" "weight"]
   :data         (->> v
                   (map (fn [{:keys [epoch data]}]
                          data))
                   (apply concat)
                   (into [])
                   (remove nil?)
                   (sort-by (juxt (fn [x] (get x 0))
                              (fn [x] (get x 1)))))})



(defn get-signal-path-data
  "returns signal-paths:

       tx-terminal -> tx-beam -> tx-channel -> satellite  -> rx-channel -> rx-beam -> rx-terminal

   per epoch.

   returns a sequence of maps:

       ({:name <epoch-id>
         :data ([<from> <to> <weight>]...)})"

  []
  (mv/query-thread
    {:q-fn     (d/q '[:find [(pull ?e [*]) ...]
                      :where [?e :tx-term-id ?tx-term-id]
                      [?e :rx-term-id]]
                 @mv/conn)
     :map-fn   (map signal-path)
     :merge-fn merge-signal-paths}))



(defn fetch-data []
  (log/info "Signal Path Service")

  {:title       "Signal Path Data - Grouped"
   :data-format :data-format/grouped-from-to-n
   :series      (get-signal-path-data)})                    ;(mv/signal-path-query)})




(comment
  (get-signal-path-data)


  (sort-by (juxt (fn [x] (get x 0))
             (fn [x] (get x 1)))
    (remove nil?
      (into []
        (apply concat
          (map (fn [{:keys [epoch data]}]
                 data)
            v)))))

  (take 1 (mv/signal-path-query))

  (vanilla.subscription-manager/refresh-source :signal-path-service)

  ())

