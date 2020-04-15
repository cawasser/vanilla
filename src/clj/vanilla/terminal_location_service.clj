(ns vanilla.terminal-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))


(def filename "resources/public/excel/Demo.xlsx")
(def sheet "Terminals")
(def column-map {:C :name
                 :E :lat
                 :F :lon})
(def post-fn (fn [x] x))



(defn- get-terminal-data []
  (excel/load-data filename sheet column-map post-fn)

  (->> (d/q '[:find ?name ?lat ?lon
              :where [?e :name ?name]
              [?e :lat ?lat]
              [?e :lon ?lon]]
         @excel/conn)
    (map (fn [[name lat lon]]
           {:name name :lat lat :lon lon}))))



(defn fetch-data []
  (log/info "Terminal Locations")

  {:title "Terminal Locations"
   :data-format :data-format/cont-n
   :data (get-terminal-data)})



(comment
  (excel/load-data filename sheet column-map post-fn)


  (->> (d/q '[:find ?name ?lat ?lon
              :where [?e :name ?name]
              [?e :lat ?lat]
              [?e :lon ?lon]]
         @excel/conn)
    (map (fn [[name lat lon]]
           {:name name :lat lat :lon lon})))

  ())