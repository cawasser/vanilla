(ns vanilla.beam-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))





(defn- get-data [band]
  (->> (d/q '[:find ?name ?lat ?lon ?radius ?type
              :where [?e :band ?band]
              [?e :beam-id ?name]
              [?e :lat ?lat]
              [?e :lon ?lon]
              [?e :radius ?radius]
              [?e :type ?type]
              :in $ ?band]
         @excel/conn band)
    (map (fn [[name lat lon radius t city]]
           {:name (str band name) :lat lat :lon lon
            :e {:diam (* radius 2) :purpose t}}))))


(defn fetch-data [band]
  (log/info "Beam Locations" band "band")

  {:title "Beam Locations"
   :data-format :data-format/lat-lon-e
   :data (get-data band)})



(comment
  (sort-by :name (get-data "X"))

  (sort-by :name (get-data "Ka"))

  ())


