(ns vanilla.widgets.highmaps
  (:require [vanilla.widgets.make-chart :as mc]))


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
                          :pointPadding 0.2}}})



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :bar-chart {:chart-options     {:chart/type              :highmaps
                                    :chart/supported-formats [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
                                    :chart                   {:type     "bar"
                                                              :zoomType "x"}
                                    :yAxis                   {:title  {:align "high"}
                                                              :labels {:overflow "justify"}}}

                :merge-plot-option {:default plot-options}

                :conversions       {:default mc/default-conversion}}))

