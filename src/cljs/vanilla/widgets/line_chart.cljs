(ns vanilla.widgets.line-chart
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
    :line-chart {:chart-options     {:chart/type              :line-chart
                                     :chart/supported-formats [:data-format/x-y :data-format/y]
                                     :chart                   {:type     "line"
                                                               :zoomType "x"}
                                     :yAxis                   {:min    0
                                                               :title  {:align "high"}
                                                               :labels {:overflow "justify"}}}

                 :merge-plot-option {:data_format/x-y plot-options-x-y
                                     :default         plot-options-y}

                 :conversions       {:default mc/default-conversion}}))

