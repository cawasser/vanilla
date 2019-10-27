(ns vanilla.widgets.bubble-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "column/plot-options " chart-config))

  {:xAxis       {; what to do about x-categories?  :categories (:src/x-categories data [])
                 :title {:text          (get-in data [:data :src/x-title] "x-axis")
                         :allowDecimals (get options :viz/x-allowDecimals false)}}
   :yAxis       {:title {:text          (get-in data [:data :src/y-title] "y-axis")
                         :allowDecimals (get options :viz/y-allowDecimals false)}}
   :plotOptions {:series {:dataLabels {:enabled (get options :viz/data-labels false)
                                       :format "{point.name}"}
                          :animation (get options :viz/animation false)}}})




;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(mc/register-type
  :bubble-chart {:chart-options     {:chart/type              :bubble-chart
                                     :chart/supported-formats [:data-format/x-y-n :data-format/x-y-e]
                                     :chart                   {:type "bubble"}
                                     :yAxis                   {:min    0
                                                               :title  {:align "high"}
                                                               :labels {:overflow "justify"}}
                                     :series                  {:dataLabels {:format "{point.name}"}}}


                 :merge-plot-option {:default plot-options}

                 :conversions       {:default mc/default-conversion}})


