(ns vanilla.widgets.network-graph-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "network/plot-options " chart-config))

  {:plotOptions {:series       {:animation (:viz/animation options false)}
                 :networkgraph {:keys ["from", "to"]}}})


(defn convert [chart-type data options]

  (let [ret [(merge {:dataLabels {:enabled true :linkFormat ""}}
                    {:data (get-in data [:data :series 0 :data])})]]

    ;(.log js/console (str "network/convert " chart-type
    ;                      " //// (data)" data
    ;                      " //// (ret)" ret))

    ret))


;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :network-chart {:chart-options     {:chart/type              :network-chart
                                        :chart/supported-formats [:data-format/from-to :data-format/from-to-n]
                                        :chart                   {:type "networkgraph"}
                                        :plotOptions             {:keys ["from", "to"]}
                                        :series                  {:dataLabels {:linkFormat ""}}}

                    :merge-plot-option {:default plot-options}

                    :conversions       {:default convert}}))



