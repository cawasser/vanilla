(ns vanilla.widgets.pie-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.util :as util]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "pie/plot-options " chart-config))

  {
   ;:legend      {:align         "right"
   ;              :verticalAlign "top"
   ;              :layout        "vertical"}

   :plotOptions {:series {:animation (:viz/animation options false)}
                 :pie    {:allowPointSelect true
                          :dataLabels       {:enabled (get options :viz/dataLabels false)
                                             :format  (get options :viz/labelFormat "")}}}})




;;;;;;;;;;;;;;
;
; these functions will convert the data from the current format into
; what the chart type actually wants. This may involve adding
; data to the :series, or rearranging the contents

(defn- add-slice-data [data slice-at]

  ; (prn "pie process-data" (str data))

  [{:colorByPoint true
    :keys         ["name" "y" "selected" "sliced"]
    :data         (map #(conj % false (< (second %) slice-at)) data)}])




(defn convert-name-y
  [slice-at chart-type data options]

  (let [ret (add-slice-data (get-in data [:data :series 0 :data] []) slice-at)]
    (prn "pie/convert-name-y " chart-type
      " //// (data) " data
      " //// (:data) " (get-in data [:data :series 0 :data])
      " //// (ret) " ret)

    ret))




;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :pie-chart {:chart-options     {:chart/type              :pie-chart
                                    :chart/supported-formats [:data-format/label-y]
                                    :chart                   {:type  "pie"
                                                              :style {:labels {:fontFamily "monospace"
                                                                               :color      "#FFFFFF"}}}}

                :merge-plot-option {:default plot-options}

                :conversions       {:default (partial convert-name-y 45)}}))



