(ns vanilla.heatmap-service
  (:require [clojure.tools.logging :as log]))

(defn heatmap-data []
  [{:name        "Fruit Production per Continent"
    :keys ["x" "y" "value"]
    :categories {:x ["Apples" "Avocados" "Bananas" "Oranges" "Peaches" "Pears" "Plums" "Prunes" "Starfruit" "Tangerine"]
                 :y ["North America" "South America" "Africa" "Europe" "Asia" "Australia" "Antarctica"]}
    :data (vec (for [x (range 0 10) y (range 0 7)] [x y (rand-int 101)]))}])

(defn fetch-data []
  (log/info "Heatmap Service")

  {:title "Heatmap Data"
   :data-format :data-format/grid-n

   :series  (heatmap-data)})
