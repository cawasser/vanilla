(ns vanilla.widgets.arearange-chart
  (:require [vanilla.widgets.make-chart :as mc]))


(defn plot-options
  [chart-config data options]

  ;(prn "area/plot-options " chart-config)

  {:xAxis       {; what to do about x-categories?  :categories (:src/x-categories data [])
                 :title {:text          (get-in data [:data :src/x-title] "x-axis")
                         :allowDecimals (get options :viz/x-allowDecimals false)}}
   :yAxis       {:title {:text          (get-in data [:data :src/y-title] "y-axis")
                         :allowDecimals (get options :viz/y-allowDecimals false)}}
   :plotOptions {:series {:animation (:viz/animation options false)}
                 :area   {:dataLabels {:enabled (get options :viz/dataLabels false)
                                       :format  (get options :viz/labelFormat "")}}}})

(defn register-type []
  (mc/register-type
    :arearange-chart {:chart-options     {:chart/type              :arearange-chart
                                          :chart/supported-formats [:data-format/date-yl-yh]
                                          :chart                   {:type     "arearange"
                                                                    :zoomType "x"}

                                          ; TODO: need to reconsider how to describe the x-axis...
                                          :xAxis                   {:type "datetime"
                                                                    :accessibility {:rangeDescription "Range: Jan 1st 2017 to Dec 31 2017"}}

                                          :yAxis                   {:title  {:align "high"}
                                                                    :labels {:overflow "justify"}}}

                      :merge-plot-option {:default plot-options}

                      :conversions       {:default mc/default-conversion}}))
