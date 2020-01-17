(ns vanilla.widgets.area-chart
  (:require [vanilla.widgets.make-chart :as mc]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "area/plot-options " chart-config))

  {:xAxis       {; what to do about x-categories?  :categories (:src/x-categories data [])
                 :title {:text          (get-in data [:data :src/x-title] "x-axis")
                         :allowDecimals (get options :viz/x-allowDecimals false)}}
   :yAxis       {:title {:text          (get-in data [:data :src/y-title] "y-axis")
                         :allowDecimals (get options :viz/y-allowDecimals false)}}
   :plotOptions {:series {:animation (:viz/animation options false)}
                 :area   {:dataLabels {:enabled (get options :viz/dataLabels false)
                                       :format  (get options :viz/labelFormat "")}}}})

;;;;;;;;      y -> x-y conversion function (y-conversion)   ;;;;;;;;;;;;;

(defn y-conversion [chart-type d options]
  (let [s (get-in d [:data :series])
        ret (for [{:keys [name data]} s]
              (assoc {}
                :name name
                :data (into []
                            (for [x-val (range 0 (count data))]
                              [x-val (get data x-val) 1]))))]

    (prn "y-conversion " ret)
    (into [] ret)))



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :area-chart {:chart-options     {:chart/type              :area-chart
                                     :chart/supported-formats [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
                                     :chart                   {:type     "area"
                                                               :zoomType "x"}
                                     :yAxis                   {:min    0
                                                               :title  {:align "high"}
                                                               :labels {:overflow "justify"}}}

                 :merge-plot-option {:default plot-options}

                 :conversions       {:default mc/default-conversion
                                     :data-format/y y-conversion}}))
