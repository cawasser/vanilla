(ns vanilla.widgets.bar-chart
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



(defn convert-pie-to-bar [chart-type data options]

  ;(.log js/console (str "bar/convert-pie-to-bar " chart-type
  ;                      " //// (data)" data
  ;                      " //// (options)" options))

  (let [dats (get-in data [:data :series 0 :data])
        new-data (into [] (map (fn [[n v]] {:name n :data [v]}) dats))]

    ;(.log js/console (str "pie->bar " data " -> " new-data))

    new-data))



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :bar-chart {:chart-options     {:chart/type              :bar-chart
                                    :chart/supported-formats [:data-format/y :data-format/x-y]
                                    :chart                   {:type     "bar"
                                                              :zoomType "x"}
                                    :yAxis                   {:min    0
                                                              :title  {:align "high"}
                                                              :labels {:overflow "justify"}}}

                :merge-plot-option {:default plot-options}

                :conversions       {:data-format/name-y convert-pie-to-bar
                                    :default mc/default-conversion}}))

