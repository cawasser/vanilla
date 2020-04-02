(ns vanilla.widgets.org-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]
            ["react-highcharts" :as ReactHighcharts]
            ["highcharts/modules/organization" :as addOrganizationModule]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "org/plot-options " chart-config))

  {:plotOptions {:series       {:animation (:viz/animation options false)}
                 :organization {:keys ["from", "to"]}}})


(defn convert [chart-type data options]

  (let [ret [(merge {:dataLabels {:enabled true :linkFormat ""}}
                    {:data (get-in data [:data :series 0 :data])})]]

    ;(.log js/console (str "org/convert " chart-type
    ;                      " //// (data)" data
    ;                      " //// (ret)" ret))

    ret))


;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (addOrganizationModule ReactHighcharts/Highcharts)
  (mc/register-type
    :org-chart {:chart-options     {:chart/type              :org-chart
                                    :chart/supported-formats [:data-format/from-to :data-format/from-to-n :data-format/from-to-e]
                                    :chart                   {:type "organization"}
                                    :plotOptions             {:keys ["from", "to"]}
                                    :series                  {:dataLabels {:linkFormat ""}}}

                :merge-plot-option {:default plot-options}

                :conversions       {:default convert}}))
