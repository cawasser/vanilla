(ns vanilla.widgets.rose-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "column/plot-options " chart-config))

  {:xAxis       {:categories        (:src/keys data [])
                 :tickmarkPlacement "on"
                 :title             {:text          (get-in data [:data :src/x-title] "x-axis")
                                     :allowDecimals (get options :viz/x-allowDecimals false)}}
   :yAxis       {:title         {:text          (get-in data [:data :src/y-title] "y-axis")
                                 :allowDecimals (get options :viz/y-allowDecimals false)}
                 :endOnTick     false
                 :showLastLabel true
                 :reversedStacks false}
   :legend      {:align         "right"
                 :verticalAlign "top"
                 :layout        "vertical"}
   :plotOptions {:series {:stacking       :normal
                          :groupPadding   0
                          :pointPlacement "on"}
                 :column {:dataLabels   {:enabled (get options :viz/dataLabels false)
                                         :format  (get options :viz/labelFormat "")}
                          :pointPadding 0.2}}})



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(mc/register-type
  :rose-chart {:chart-options     {:chart/type              :rose-chart
                                   :chart/supported-formats [:data-format/y :data-format/x-y]
                                   :chart                   {:type     "column"
                                                             :polar    "true"
                                                             :zoomType "x"}
                                   :yAxis                   {:min    0
                                                             :title  {:align "high"}
                                                             :labels {:overflow "justify"}}}

               :merge-plot-option {:default plot-options}

               :conversions       {:default mc/default-conversion}})

