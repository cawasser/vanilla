(ns vanilla.widgets.sankey-chart
  (:require [vanilla.widgets.make-chart :as mc]))



;(defn plot-options
;  [chart-config data options]
;
;  ;(.log js/console (str "sankey-deps/plot-options " chart-config))
;
;  {:plotOptions {:series {:animation (:viz/animation options false)}}})
;
;
(defn sankey-conversion
  [chart-type data options]

  ;(prn "sankey-conversion " chart-type
  ;  " //// (data)" data
  ;  " //// (options)" options)

  [{:keys (get-in data [:data :src/keys] [])
    :data (get-in data [:data :series 0 :data])}])



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :sankey-chart {:chart-options
                   {:chart/type              :sankey-chart
                    :chart/supported-formats [:data-format/from-to :data-format/from-to-n]
                    :chart                   {:type "sankey"}}

                   :merge-plot-option
                   {:default mc/default-plot-options}

                   :conversions
                   {:default sankey-conversion}}))



