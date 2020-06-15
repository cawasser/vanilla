(ns vanilla.terminal-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))





(defn- get-terminal-data []
  (->> (d/q '[:find [(pull ?e [:terminal-id :lat :lon]) ...]
              :where [?e :terminal-id]]
         @excel/conn)
    (map (fn [{:keys [terminal-id lat lon]}]
           {:name terminal-id :lat lat :lon lon}))))



(defn fetch-data []
  (log/info "Terminal Locations")

  {:title "Terminal Locations"
   :data-format :data-format/cont-n
   :data (get-terminal-data)})



(comment
  (->> (d/q '[:find ?name ?lat ?lon
              :where [?e :name ?name]
              [?e :lat ?lat]
              [?e :lon ?lon]]
         @excel/conn)
    (map (fn [[name lat lon]]
           {:name name :lat lat :lon lon})))



  (d/q '[:find [(pull ?e []) ...]
         :where [?e :terminal-id]]
    @excel/conn)

  ())