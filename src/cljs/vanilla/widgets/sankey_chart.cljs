(ns vanilla.widgets.sankey-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]))



(defn sankey-plot-options
  [chart-config data options]

  ;(.log js/console (str "sankey/plot-options " chart-config))

  {:plotOptions {:series {:animation (:viz/animation options false)}}

   :series      [{;:type "sankey"
                  :keys (get data :src/keys [])
                  :data (get-in data [:data :series])}]})



(defn dependency-plot-options
  [chart-config data options]

  ;(.log js/console (str "dependency/plot-options " chart-config))

  {:plotOptions {:series {:animation (:viz/animation options false)}}

   :series      [{:keys (get data :src/keys [])
                  :data (get-in data [:data :series])}]})



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
                 {:default sankey-plot-options}

                 :conversions
                 {:default mc/default-conversion}})



(mc/register-type
  :dependency-chart {:chart-options
                     {:chart/type              :dependency-chart
                      :chart/supported-formats [:data-format/from-to :data-format/from-to-n]
                      :chart                   {:type "dependencywheel"}
                      :series                  {:dataLabels {:color    "#333"
                                                             :textPath {:enabled    true
                                                                        :attributes {:dy 5}}
                                                             :distance 10}}}

                     :merge-plot-option
                     {:default dependency-plot-options}

                     :conversions
                     {:default mc/default-conversion}})
