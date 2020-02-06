(ns vanilla.widgets.continent-map
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

   :colorAxis {:min 0}

   :series []})



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mm/register-type
    :continent-map {:chart-options {:chart/type              :continent-map
                                    :chart/supported-formats [:data-format/cont-n]
                                    :chart                   {:map "custom/world-continents"
                                                              :zoomType "x"}
                                    :yAxis                   {:title  {:align "high"}
                                                              :labels {:overflow "justify"}}}

                :merge-plot-option {:default plot-options}

                :conversions       {:default mm/default-conversion}}))

