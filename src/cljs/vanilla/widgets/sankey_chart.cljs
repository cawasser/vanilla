(ns vanilla.widgets.sankey-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]))



(defn plot-options
  [chart-config data options]

  (.log js/console (str "org/plot-options " chart-config))

  {:plotOptions {:series {:animation (:viz/animation options false)}}

   :series      [{:type "sankey"
                  :keys (get data :src/keys [])
                  :data (get-in data [:data :series])}]})




;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(mc/register-type
  :sankey-chart {:chart-options     {:chart/type              :sankey-chart
                                     :chart/supported-formats [:data-format/from-to :data-format/form-to-n]
                                     :chart                   {:type "sankey"}}
                 ;:series                  {:dataLabels {:linkFormat ""}}}

                 :merge-plot-option {:default plot-options}

                 :conversions       {:default mc/default-conversion}})









