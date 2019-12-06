(ns vanilla.widgets.heatmap-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]))



;;;;;;;;;;;
;
; line vanilla.widgets.line-chart
;
(defn plot-options-x-y
  [chart-config data options]

  ;(.log js/console (str "line/plot-options-x-y " chart-config))

  {:xAxis       {:title {:text          (get-in data [:data :src/x-title] "x-axis")
                         :allowDecimals (get-in options [:viz/x-allowDecimals] false)}}
   :yAxis       {:title {:text          (get-in data [:data :src/y-title] "y-axis")
                         :allowDecimals (get-in options [:viz/y-allowDecimals] false)}}
   :plotOptions {:series {:animation (:viz/animation options false)}
                 :line   {:dataLabels {:enabled (:viz/dataLabels options false)
                                       :format  (:viz/labelFormat options "")}
                          :lineWidth  (:viz/lineWidth options 1)}}})


(defn plot-options-y
  [chart-config data options]

  ;(.log js/console (str "line/plot-options-y " chart-config))

  {:xAxis       {; what to do about x-categories?  :categories (:src/x-categories data [])
                 :title {:text          (get-in data [:data :src/x-title] "x-axis")
                         :allowDecimals (get-in options [:viz/x-allowDecimals] false)}}
   :yAxis       {:title {:text          (get-in data [:data :src/y-title] "y-axis")
                         :allowDecimals (get-in options [:viz/y-allowDecimals] false)}}
   :plotOptions {:series {:animation (:viz/animation options false)}
                 :line   {:dataLabels {:enabled (:viz/dataLabels options false)
                                       :format  (:viz/labelFormat options "")}
                          :lineWidth  (:viz/lineWidth options 1)}}})



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(mc/register-type
  :heatmap-chart
  {:chart-options     {:chart/type              :heatmap-chart
                       :chart/supported-formats [:data-format/y :data-format/x-y]
                       :chart                   {:type            "heatmap"
                                                 :zoomType        "x"
                                                 :marginTop       40
                                                 :marginBottom    80
                                                 :plotBorderWidth 1}

                       :title                   {:text ""}

                       :xAxis                   {:categories ["Apples" "Avocados" "Bananas" "Oranges" "Peaches" "Pears" "Plums" "Prunes" "Starfruit" "Tangerine"]}

                       :yAxis                   {:categories ["North America" "South America" "Africa" "Europe" "Asia" "Australia" "Antarctica"]
                                                 :title ""
                                                 :reversed   true}

                       :colorAxis               {:min      0
                                                 :minColor "#FFFFFF"
                                                 :maxColor "#006400"} ;Highcharts.getOptions().colors[0]}

                       :legend   {:align         "right"
                                  :layout        "vertical"
                                  :margin        0
                                  :verticalAlign "top"
                                  :y             25
                                  :symbolHeight  280}
                       :plotOptions             {}
                       :series                  [{:name        "Fruit Production per Continent"
                                                  :borderWidth 1
                                                  :data        [[0 0 10] [0 1 19] [0 2 8] [0 3 24] [0 4 67] [0 5 12] [0 6 0]
                                                                [1 0 92] [1 1 58] [1 2 78] [1 3 117] [1 4 48] [1 5 35] [1 6 0]
                                                                [2 0 35] [2 1 15] [2 2 123] [2 3 64] [2 4 52] [2 5 42] [2 6 0]
                                                                [3 0 72] [3 1 132] [3 2 114] [3 3 19] [3 4 16] [3 5 62] [3 6 0]
                                                                [4 0 38] [4 1 5] [4 2 8] [4 3 117] [4 4 115] [4 5 2] [4 6 0]
                                                                [5 0 88] [5 1 32] [5 2 12] [5 3 6] [5 4 120] [5 5 12] [5 6 0]
                                                                [6 0 13] [6 1 44] [6 2 88] [6 3 98] [6 4 96] [6 5 120] [6 6 0]
                                                                [7 0 31] [7 1 1] [7 2 82] [7 3 32] [7 4 30] [7 5 44] [7 6 0]
                                                                [8 0 85] [8 1 97] [8 2 123] [8 3 64] [8 4 84] [8 5 55] [8 6 0]
                                                                [9 0 47] [9 1 114] [9 2 31] [9 3 48] [9 4 91] [9 5 100] [9 6 0]]
                                                  :dataLabels  {:enabled true
                                                                :color   "#000000"}}]}

   :merge-plot-option {:default mc/default-plot-options}

   :conversions       {:default mc/default-conversion}})

