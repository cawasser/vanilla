(ns vanilla.energy-use-service
  (:require [clojure.tools.logging :as log]))



(defn make-data []
  [["Oil" "Transportation" 94]
   ["Natural Gas" "Transportation" 3]
   ["Coal" "Transportation" 0]
   ["Renewable" "Transportation" 0]
   ["Nuclear" "Transportation" 3]
   ["Oil" "Industrial" 41]
   ["Natural Gas" "Industrial" 40]
   ["Coal" "Industrial" 7]
   ["Renewable" "Industrial" 11]
   ["Nuclear" "Industrial" 0]
   ["Oil" "Residential & Commercial" 17]
   ["Natural Gas" "Residential & Commercial" 76]
   ["Coal" "Residential & Commercial" 1]
   ["Renewable" "Residential & Commercial" 7]
   ["Nuclear" "Residential & Commercial" 0]
   ["Oil" "Electric Power" 1]
   ["Natural Gas" "Electric Power" 18]
   ["Coal" "Electric Power" 48]
   ["Renewable" "Electric Power" 11]
   ["Nuclear" "Electric Power" 22]])



(defn fetch-data []
  (log/info "Energy Use Service")

  {:title "Energy Use By Section"
   :data-format :data-format/from-to-n
   :series  [{:keys ["from" "to" "weight"]
              :data (make-data)}]})
