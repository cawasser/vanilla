(ns vanilla.signal-path-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))


(def filename "resources/public/excel/Demo.xlsx")
(def sheet "SCN_NETWORK_CARRIER_VW")
(def column-map {:A :satellite
                 :B :rx-beam
                 :C :rx-channel
                 :D :tx-beam
                 :E :tx-channel
                 :H :mission-name})
(def post-fn #(map (fn [{:keys [satellite] :as m}]
                     (assoc m :satellite (int satellite)))
                %))



(defn- query-for-data []
  (->> (clojure.set/union
         ; :rx-channel -> :rx-beam
         (->> (d/q '[:find ?from ?to
                     :where [?e :rx-channel ?from]
                     [?e :rx-beam ?to]]
                @excel/conn)
           (map (fn [[from to]]
                  [(str from ".") to])))

         ;rx-beam -> :satellite
         (->> (d/q '[:find ?from ?to
                     :where [?e :rx-beam ?from]
                     [?e :satellite ?to]]
                @excel/conn)
           (map (fn [[from to]]
                  [from (str to)])))

         ;:satellite -> :tx-beam
         (->>(d/q '[:find ?from ?to
                    :where [?e :satellite ?from]
                    [?e :tx-beam ?to]]
               @excel/conn)
           (map (fn [[from to]]
                  [(str from) to])))

         ;:tx-beam -> :tx channel
         (->> (d/q '[:find ?from ?to
                     :where [?e :tx-beam ?from]
                     [?e :tx-channel ?to]]
                @excel/conn)
           (map (fn [[from to]]
                  [from (str "." to)]))))

    (map (fn [[from to]]
           [from to 5]))))

(defn- get-data-from-excel []
  (excel/load-data filename sheet column-map post-fn)
  (into [] (query-for-data)))




(defn fetch-data []
  (log/info "Signal Path Service")

  {:title "Signal Path Data"
   :data-format :data-format/from-to-n
   :series [{:keys ["from" "to" "weight"]
             :data (get-data-from-excel)}]})




(comment

  (def f "resources/public/excel/Demo.xlsx")

  (excel/load-data f)

  (query-for-data)

  (get-data-from-excel f)

  (excel/load-data filename sheet column-map post-fn)

  ())

