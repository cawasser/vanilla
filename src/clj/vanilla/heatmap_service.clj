(ns vanilla.heatmap-service
  (:require [clojure.tools.logging :as log]))


(def data-rates (into [] (map #(* 2048 %) (range 10))))

(defn heatmap-data []
  (let [lat (into [] (map str (keep #(if (not= 0 %) %) (range -80 80 10))))
        lon (into [] (map str (keep #(if (not= 0 %) %) (range -180 180 10))))]
    [
     ;{:name        "Fruit Production per Continent"
     ; :keys ["x" "y" "value"]
     ; :categories {:x ["Apples" "Avocados" "Bananas" "Oranges" "Peaches" "Pears" "Plums" "Prunes" "Starfruit" "Tangerine"]
     ;              :y ["North America" "South America" "Africa" "Europe" "Asia" "Australia" "Antarctica"]}
     ; :data (vec (for [x (range 0 10) y (range 0 7)] [x y (rand-int 101)]))}]))

     {:name        "Data Rate"
      :keys ["x" "y" "value"]
      :categories {:x lat
                   :y lon}
      :data (vec (for [x (keep #(if (not= 0 %) %) (range -8 8 1)) ;(range (count lat))
                       y (keep #(if (not= 0 %) %) (range -18 18 1))] ;(range (count lon))]
                   [x y (nth data-rates (inc (rand-int 9)))]))}]))

(defn fetch-data []
  (log/info "Heatmap Service")

  {:title "Heatmap Data"
   :data-format :data-format/grid-n

   :series (heatmap-data)})


(comment

  (vanilla.subscription-manager/refresh-source :heatmap-data)
  ())
