(ns vanilla.heatmap-service
  (:require [clojure.tools.logging :as log]))

(defn heatmap-data []
  [{:name        "Fruit Production per Continent"
    :keys ["x" "y" "value"]
    :data (vec (for [x (range 0 10) y (range 0 7)] [x y (rand-int 101)]))}])

(defn fetch-data []
  (log/info "Heatmap Service")

  {:title "Heatmap Data"
   :data-format :data-format/grid-n

   :series  (heatmap-data)})
