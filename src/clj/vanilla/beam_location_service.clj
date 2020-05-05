(ns vanilla.beam-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))





(defn- get-data []
  (->> (d/q '[:find ?name ?lat ?lon ?radius ?type ?city
              :where [?e :band "X"]
              [?e :beam-id ?name]
              [?e :lat ?lat]
              [?e :lon ?lon]
              [?e :radius ?radius]
              [?e :type ?type]
              [?e :city ?city]]
         @excel/conn)
    (map (fn [[name lat lon radius t city]]
           {:name (str "X" name) :lat lat :lon lon
            :e {:diam (* radius 2) :purpose t :city city}}))))


(defn fetch-data []
  (log/info "Beam Locations")

  {:title "Beam Locations"
   :data-format :data-format/lat-lon-e
   :data (get-data)})



(comment
  (sort-by :name (get-data))

  ())


