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
(defn register-type []
  (mc/register-type
    :heatmap-chart
    {:chart-options     {:chart/type              :heatmap-chart
                         :chart/supported-formats [:data-format/x-y-n :data-format/x-y-e :data-format/lat-lon-n :data-format/lat-lon-e]
                         :chart                   {:type            "heatmap"
                                                   :zoomType        "x"
                                                   :marginTop       40
                                                   :marginBottom    80
                                                   :plotBorderWidth 1}

                         :title                   {:text ""}

                         :xAxis                   {:categories ["Apples" "Avocados" "Bananas" "Oranges" "Peaches" "Pears" "Plums" "Prunes" "Starfruit" "Tangerine"]}

                         :yAxis                   {:categories ["North America" "South America" "Africa" "Europe" "Asia" "Australia" "Antarctica"]
                                                   :title      ""
                                                   :reversed   true}

                         :colorAxis               {:min      0
                                                   :minColor "#FFFFFF"
                                                   :maxColor "#006400"} ;Highcharts.getOptions().colors[0]}

                         :legend                  {:align         "right"
                                                   :layout        "vertical"
                                                   :margin        0
                                                   :verticalAlign "top"
                                                   :y             25
                                                   :symbolHeight  280}
                         :plotOptions             {}

                         :series                  [{:dataLabels {:enabled true
                                                                 :color   "#000000"}}]}

     :merge-plot-option {:default mc/default-plot-options}

     :conversions       {:default mc/default-conversion}}))

