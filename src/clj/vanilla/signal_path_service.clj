(ns vanilla.signal-path-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))


(def sheet "SCN_NETWORK_CARRIER_VW")
(def column-map {:A :satellite
                 :B :rx-beam
                 :C :rx-channel
                 :D :tx-beam
                 :E :tx-channel
                 :F :plan
                 :G :mission-name
                 :H :service
                 :R :data-rate})
(def post-fn
  (fn [x] x))
;#(map (fn [{:keys [satellite] :as m}]
;        (assoc m :satellite (int satellite)))
;   %))



(defn- query-for-data []
  (->> (clojure.set/union
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
                     [?e :satellite ?to]
                     [?e :data-rate ?data-rate]]
                @excel/conn)
           (map (fn [[from to data-rate]]
                  [from (str to) data-rate])))

         ;:satellite -> :tx-beam
         (->> (d/q '[:find ?from ?to ?data-rate
                     :where [?e :satellite ?from]
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
                  [from (str "." to) data-rate]))))))

;(map (fn [[from to]]
;       [from to 5]))))

(defn- get-data-from-excel []
  (excel/load-data excel/filename sheet column-map post-fn)
  (into [] (query-for-data)))





(defn fetch-data []
  (log/info "Signal Path Service")

  {:title       "Signal Path Data"
   :data-format :data-format/from-to-n
   :series      [{:keys ["from" "to" "weight"]
                  :data (sort-by (juxt (fn [x] (get x 0))
                                   (fn [x] (get x 1)))
                          (get-data-from-excel))}]})




(comment

  (excel/load-data excel/filename)

  (sort-by #(get % 0) (query-for-data))

  (get-data-from-excel)

  (excel/load-data excel/filename sheet column-map post-fn)

  ())

