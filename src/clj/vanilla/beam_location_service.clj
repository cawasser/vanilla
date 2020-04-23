(ns vanilla.beam-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))


(def sheet "Beams")
(def column-map {:A :id
                 :B :lat
                 :C :lon
                 :D :diam
                 :E :type
                 :F :city})
(def post-fn (fn [x] x))



(defn- get-data []
  (excel/load-data excel/filename sheet column-map post-fn)

  (->> (d/q '[:find ?name ?lat ?lon ?diam ?type ?city
              :where [?e :id ?name]
              [?e :lat ?lat]
              [?e :lon ?lon]
              [?e :diam ?diam]
              [?e :type ?type]
              [?e :city ?city]]
         @excel/conn)
    (map (fn [[name lat lon diam t city]]
           {:name name :lat lat :lon lon
            :e {:type :diameter :diam diam :purpose t :city city}}))))


(defn fetch-data []
  (log/info "Beam Locations")

  {:title "Beam Locations"
   :data-format :data-format/lat-lon-e
   :data (get-data)})



(comment
  (excel/load-data excel/filename sheet column-map post-fn)

  (get-data)

  ())


