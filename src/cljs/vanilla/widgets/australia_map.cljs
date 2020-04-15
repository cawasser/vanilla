(ns vanilla.widgets.australia-map
  (:require [vanilla.widgets.make-map :as mm]
            ["@highcharts/map-collection/countries/au/au-all.geo.json" :as aus-geo]))

(defn plot-options
  [chart-config data options]

  ;(.prn "column/plot-options " chart-config)

  {:plotOptions   {:series {:animation (:viz/animation options false)}
                   :bar    {:dataLabels   {:enabled (get options :viz/dataLabels false)
                                           :format  (get options :viz/labelFormat "")}
                            :pointPadding 0.2}}
   :mapNavigation {:enabled       true
                   :buttonOptions {:verticalAlign "bottom"}}

   :series        []})


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
    :australia-map {:chart-options     {:chart/type              :australia-map
                                        :chart/supported-formats [:data-format/lat-lon-label]
                                        :chart                   {:map      aus-geo
                                                                  :zoomType "xy"}
                                        :yAxis                   {:title  {:align "high"}
                                                                  :labels {:overflow "justify"}}}

                    :merge-plot-option {:default plot-options}

                    :conversions       {:default data-conversion}}))


