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
                 :H :mission-name
                 :Z :bandwidth
                 :AA :data-rate})
(def post-fn #(map (fn [{:keys [satellite] :as m}]
                     (assoc m :satellite (int satellite)))
                %))



(defn- query-for-data []
  (->> (clojure.set/union
         ; :rx-channel -> :rx-beam
         (->> (d/q '[:find ?from ?to ?bw
                     :where [?e :rx-channel ?from]
                     [?e :rx-beam ?to]
                     [?e :bandwidth ?bw]]
                @excel/conn)
           (map (fn [[from to bw]]
                  [(str from ".") to bw])))

         ;rx-beam -> :satellite
         (->> (d/q '[:find ?from ?to ?bw
                     :where [?e :rx-beam ?from]
                     [?e :satellite ?to]
                     [?e :bandwidth ?bw]]
                @excel/conn)
           (map (fn [[from to bw]]
                  [from (str to) bw])))

         ;:satellite -> :tx-beam
         (->>(d/q '[:find ?from ?to ?bw
                    :where [?e :satellite ?from]
                    [?e :tx-beam ?to]
                    [?e :bandwidth ?bw]]
               @excel/conn)
           (map (fn [[from to bw]]
                  [(str from) to bw])))

         ;:tx-beam -> :tx channel
         (->> (d/q '[:find ?from ?to ?bw
                     :where [?e :tx-beam ?from]
                     [?e :tx-channel ?to]
                     [?e :bandwidth ?bw]]
                @excel/conn)
           (map (fn [[from to bw]]
                  [from (str "." to) bw]))))))

    ;(map (fn [[from to]]
    ;       [from to 5]))))

(defn- get-data-from-excel []
  (excel/load-data excel/filename sheet column-map post-fn)
  (into [] (query-for-data)))




(defn fetch-data []
  (log/info "Signal Path Service")

  {:title "Signal Path Data"
   :data-format :data-format/from-to-n
   :series [{:keys ["from" "to" "weight"]
             :data (get-data-from-excel)}]})




(comment

  (excel/load-data excel/filename)

  (query-for-data)

  (get-data-from-excel excel/filename)

  (excel/load-data excel/filename sheet column-map post-fn)

  ())

