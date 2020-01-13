(ns vanilla.widgets.dependency-chart
  (:require [vanilla.widgets.make-chart :as mc]))


;(defn plot-options
;  [chart-config data options]
;
;  ;(.log js/console (str "sankey-deps/plot-options " chart-config))
;
;  {:plotOptions {:series {:animation (:viz/animation options false)}}})
;
;
;
;(defn dependency-conversion
;  [chart-type data options]
;
;  ;(prn "dependency-conversion " chart-type
;  ;  " //// (data)" data
;  ;  " //// (options)" options)
;
;  [{:keys       (get-in data [:data :src/keys] [])
;    :data       (get-in data [:data :series 0 :data])}])
;
;
;
;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :dependency-chart {:chart-options
                       {:chart/type              :dependency-chart
                        :chart/supported-formats [:data-format/from-to-n :data-format/from-to-e :data-format/from-to]
                        :chart                   {:type "dependencywheel"}
                        :plot-options {    :dataLabels {:color    "#333"
                                                        :textPath {:enabled    true
                                                                   :attributes {:dy 5}}
                                                        :distance 10}
                                       :size       "95%"}}


                       :merge-plot-option
                       {:default mc/default-plot-options}

                       :conversions
                       {:default mc/default-conversion
                        :data-format/from-to mc/add-the-n-conversion}}))


