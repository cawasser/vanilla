(ns vanilla.widgets.heatmap-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]))




(defn- plot-options [chart-config data options]
  ;(prn "heat-map/plot-options" data)
  (let [ret {:xAxis {:categories (get-in data [:data :series 0 :categories :x])
                     :max 180
                     :min -180
                     :labels {:format "{value}0"}}
             :yAxis {:categories (get-in data [:data :series 0 :categories :y])
                     :max 90
                     :min -90
                     :labels {:format "{value}0"}}}]
    ;(prn "options" ret)
    ret))



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :heatmap-chart
    {:chart-options     {:chart/type              :heatmap-chart
                         :chart/supported-formats [:data-format/grid-n] ; TODO: heat-map support for :data-format/grid-e :data-format/lat-lon-n :data-format/lat-lon-e
                         :chart                   {:type            "heatmap"
                                                   :zoomType        "xy"
                                                   :marginTop       40
                                                   :marginBottom    80
                                                   :plotBorderWidth 1}

                         :title                   {:text ""}

                         ;:xAxis                   {:categories ["Apples" "Avocados" "Bananas" "Oranges" "Peaches" "Pears" "Plums" "Prunes" "Starfruit" "Tangerine"]}

                         ;:yAxis                   {;:categories ["North America" "South America" "Africa" "Europe" "Asia" "Australia" "Antarctica"]
                         ;                          :title      ""
                         ;                          :reversed   true}

                         :colorAxis               {:stops [[0, "#3060cf"],
                                                           [0.5, "#fffbbc"],
                                                           [0.9, "#c4463a"],
                                                           [1, "#c4463a"]]}


                         ;:legend                  {:align         "bottom"
                         ;                          :layout        "horizontal"
                         ;                          :margin        0
                         ;                          :itemDistance 50}
                         :plotOptions             {:series {:dataLabels {;:enabled true
                                                                         :color "#000000"}}}}

     :merge-plot-option {:default mc/default-plot-options} ;plot-options}

     :conversions       {:default mc/default-conversion}}))

