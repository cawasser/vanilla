(ns vanilla.widgets.sankey-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "sankey-deps/plot-options " chart-config))

  {:plotOptions {:series {:animation (:viz/animation options false)}}})


(defn sankey-conversion
  [chart-type data options]

  ;(.log js/console (str "sankey-conversion " chart-type
  ;                      " //// (data)" data
  ;                      " //// (options)" options))

  [{:keys (get-in data [:data :src/keys] [])
    :data (get-in data [:data :series 0 :data])}])



(defn dependency-conversion
  [chart-type data options]

  ;(.log js/console (str "dependency-conversion " chart-type
  ;                      " //// (data)" data
  ;                      " //// (options)" options))

  [{:keys       (get-in data [:data :src/keys] [])
    :dataLabels {:color    "#333"
                 :textPath {:enabled    true
                            :attributes {:dy 5}}
                 :distance 10}
    :size       "95%"
    :data       (get-in data [:data :series 0 :data])}])



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(mc/register-type
  :sankey-chart {:chart-options
                 {:chart/type              :sankey-chart
                  :chart/supported-formats [:data-format/from-to :data-format/from-to-n]
                  :chart                   {:type "sankey"}}

                 :merge-plot-option
                 {:default mc/default-plot-options}

                 :conversions
                 {:default sankey-conversion}})



(mc/register-type
  :dependency-chart {:chart-options
                     {:chart/type              :dependency-chart
                      :chart/supported-formats [:data-format/from-to :data-format/from-to-n]
                      :chart                   {:type "dependencywheel"}}

                     :merge-plot-option
                     {:default mc/default-plot-options}

                     :conversions
                     {:default dependency-conversion}})
