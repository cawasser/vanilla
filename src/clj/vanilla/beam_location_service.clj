(ns vanilla.beam-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))


(def sheet "Beams")
(def column-map {:A :band
                 :B :id
                 :C :lat
                 :D :lon
                 :E :radius
                 :F :type
                 :G :city})
(def post-fn (fn [x] x))



(defn- get-data []
  (excel/load-data excel/filename sheet column-map post-fn)

  (->> (d/q '[:find ?name ?lat ?lon ?radius ?type ?city
              :where [?e :band "X"]
              [?e :id ?name]
              [?e :lat ?lat]
              [?e :lon ?lon]
              [?e :radius ?radius]
              [?e :type ?type]
              [?e :city ?city]]
         @excel/conn)
    (map (fn [[name lat lon radius t city]]
           {:name (str "X" (int name)) :lat lat :lon lon
            :e {:diam (* radius 2) :purpose t :city city}}))))


(defn fetch-data []
  (log/info "Beam Locations")

  {:title "Beam Locations"
   :data-format :data-format/lat-lon-e
   :data (get-data)})



(comment
  (excel/load-data excel/filename sheet column-map post-fn)

  (sort-by :name (get-data))

  ())


