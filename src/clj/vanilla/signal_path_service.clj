(ns vanilla.signal-path-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))


;#(map (fn [{:keys [satellite] :as m}]
;        (assoc m :satellite (int satellite)))
;   %))



(defn- get-data []
  (into []
    (->> (clojure.set/union
           (->> (d/q '[:find ?from ?to ?data-rate
                       :where [?e :rx-channel ?from]
                       [?e :rx-beam ?to]
                       [?e :data-rate ?data-rate]]
                  @excel/conn)
             (map (fn [[from to data-rate]]
                    [(str from ".") to data-rate])))
           ; :rx-channel -> :rx-beam
           (->> (d/q '[:find ?from ?to ?data-rate
                       :where [?e :rx-channel ?from]
                       [?e :rx-beam ?to]
                       [?e :data-rate ?data-rate]]
                  @excel/conn)
             (map (fn [[from to data-rate]]
                    [(str from ".") to data-rate])))

           ;rx-beam -> :satellite
           (->> (d/q '[:find ?from ?to ?data-rate
                       :where [?e :rx-beam ?from]
                       [?e :satellite-id ?to]
                       [?e :data-rate ?data-rate]]
                  @excel/conn)
             (map (fn [[from to data-rate]]
                    [from (str to) data-rate])))

           ;:satellite -> :tx-beam
           (->> (d/q '[:find ?from ?to ?data-rate
                       :where [?e :satellite-id ?from]
                       [?e :tx-beam ?to]
                       [?e :data-rate ?data-rate]]
                  @excel/conn)
             (map (fn [[from to data-rate]]
                    [(str from) to data-rate])))

           ;:tx-beam -> :tx channel
           (->> (d/q '[:find ?from ?to ?data-rate
                       :where [?e :tx-beam ?from]
                       [?e :tx-channel ?to]
                       [?e :data-rate ?data-rate]]
                  @excel/conn)
             (map (fn [[from to data-rate]]
                    [from (str "." to) data-rate])))))))




(defn fetch-data []
  (log/info "Signal Path Service")

  {:title       "Signal Path Data"
   :data-format :data-format/from-to-n
   :series      [{:type  :sankey
                  :name "Signal Paths"
                  :keys ["from" "to" "weight"]
                  :data (sort-by (juxt (fn [x] (get x 0))
                                   (fn [x] (get x 1)))
                          (get-data))
                  :showInLegend true}
                 {:type  :sankey
                  :name "Languages"
                  :keys ["from" "to" "weight"]
                  :data (vanilla.sankey-service/make-data)
                  :showInLegend true
                  :visible false}
                 {:type  :sankey
                  :name "Energy"
                  :keys ["from" "to" "weight"]
                  :data (vanilla.energy-use-service/make-data)
                  :showInLegend true
                  :visible false}]})






(comment

  (excel/load-data excel/filename)

  (sort-by #(get % 0) (query-for-data))

  (get-data-from-excel)

  (excel/load-data excel/filename sheet column-map post-fn)

  ())

