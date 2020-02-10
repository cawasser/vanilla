(ns vanilla.widgets.australia-map
  (:require [vanilla.widgets.make-map :as mm]))

(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "column/plot-options " chart-config))

  {:plotOptions {:series {:animation (:viz/animation options false)}
                 :bar    {:dataLabels   {:enabled (get options :viz/dataLabels false)
                                         :format  (get options :viz/labelFormat "")}
                          :pointPadding 0.2}}
   :mapNavigation {:enabled true
                   :buttonOptions {:verticalAlign "bottom"}}

   :series []})



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mm/register-type
    :australia-map {:chart-options {:chart/type              :australia-map
                                    :chart/supported-formats [:data-format/lat-lon-label]
                                    :chart                   {:map "countries/au/au-all"
                                                              :zoomType "x"}
                                    :yAxis                   {:title  {:align "high"}
                                                              :labels {:overflow "justify"}}}

                    :merge-plot-option {:default plot-options}

                    :conversions       {:default mm/default-conversion}}))


