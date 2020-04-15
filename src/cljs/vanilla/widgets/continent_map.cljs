(ns vanilla.widgets.continent-map
  (:require [vanilla.widgets.make-map :as mm]
            ["@highcharts/map-collection/custom/world.geo.json" :as world-geo]))




(defn plot-options
  [chart-config data options]

  ;(prn "column/plot-options " chart-config)

  {:plotOptions   {:series {:animation (:viz/animation options false)}}
   :mapNavigation {:enabled       true
                   :buttonOptions {:verticalAlign "bottom"}}})


(defn data-conversion [chart-type data options]

  (let [base [{:name "Basemap"
               :borderColor "#A0A0A0"
               :showInLegend false}
              {:name "Separators"
               :type "mapline"
               :nullColor "#707070"
               :showInLegend false
               :enableMouseTracking false}
              {:type  "mappoint"
               :name  "data"
               :color "#FFFFFF"
               :data  (get-in data [:data :data])}]]

    (prn "data-conversion " base)
    base))



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mm/register-type
    :continent-map {:chart-options     {:chart/type              :continent-map
                                        :chart/supported-formats [:data-format/cont-n]
                                        :chart                   {:map      world-geo
                                                                  :zoomType "xy"}
                                        :yAxis                   {:title  {:align "high"}
                                                                  :labels {:overflow "justify"}}}

                    :merge-plot-option {:default plot-options}

                    :conversions       {:default data-conversion}}))

