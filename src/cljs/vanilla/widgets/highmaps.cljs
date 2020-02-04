(ns vanilla.widgets.highmaps
  (:require [vanilla.widgets.make-map :as mm]))


(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "column/plot-options " chart-config))

  {:xAxis       {; what to do about x-categories?  :categories (:src/x-categories data [])
                 :title {:text          (get-in data [:data :src/x-title] "x-axis")
                         :allowDecimals (get options :viz/x-allowDecimals false)}}
   :yAxis       {:title {:text          (get-in data [:data :src/y-title] "y-axis")
                         :allowDecimals (get options :viz/y-allowDecimals false)}}
   :plotOptions {:series {:animation (:viz/animation options false)}
                 :bar    {:dataLabels   {:enabled (get options :viz/dataLabels false)
                                         :format  (get options :viz/labelFormat "")}
                          :pointPadding 0.2}}
   :mapNavigation {:enabled true

                   :buttonOptions {:verticalAlign "bottom"}}



   :colorAxis {:min 0}





   :series [{:data [["eu" 0]

                    ["oc" 10]

                    ["af" 200]

                    ["as" 33]

                    ["na" 44]

                    ["sa" 55]]

             :name "Tons produced"

             :states {:hover {:color "#BADA55"}}

             :dataLabels {:enabled true

                          :format "{point.name}"}}]})



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mm/register-type
    :highmaps {:chart-options     {:chart/type              :highmaps
                                    :chart/supported-formats [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
                                    :chart                   {:map "custom/world-continents"
                                                              :zoomType "x"}
                                    :yAxis                   {:title  {:align "high"}
                                                              :labels {:overflow "justify"}}}

                :merge-plot-option {:default plot-options}

                :conversions       {:default mm/default-conversion}}))

