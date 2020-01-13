(ns vanilla.widgets.scatter-chart
  (:require [vanilla.widgets.make-chart :as mc]))

(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "column/plot-options " chart-config))

  {:xAxis       {:title {:text          (get-in data [:data :src/x-title] "x-axis")
                         :allowDecimals (get options :viz/x-allowDecimals false)}
                 :startOnTick true
                 :endOnTick true
                 :showLastLabel true}


   :yAxis       {:title {:text          (get-in data [:data :src/y-title ] "y-axis")
                         :allowDecimals (get options :viz/y-allowDecimals false)}}

   :legend      {:align         "right"
                 :verticalAlign "bottom"
                 :layout        "horizontal"
                 :x 0
                 :y -50
                 :floating true
                 :borderWidth 1}

   :plotOptions {:series {:dataLabels {:enabled (get options :viz/data-labels false)
                                       :format "{point.name}"}
                          :animation (get options :viz/animation false)}
                 :scatter {:marker
                               {:radius 5}}}})

;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :scatter-chart {:chart-options     {:chart/type              :scatter-chart
                                        :chart/supported-formats [:data-format/x-y :data-format/x-y-n :data-format/x-y-e]
                                        :chart                   {:type "scatter"
                                                                  :zoomType "xy"}
                                        :yAxis                   {:labels {:overflow "justify"}}
                                        :series                  {}}


                    :merge-plot-option {:default plot-options}

                    :conversions       {:default mc/default-conversion}}))



