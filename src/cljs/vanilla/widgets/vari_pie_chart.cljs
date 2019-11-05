(ns vanilla.widgets.vari-pie-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.util :as util]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "pie/plot-options " chart-config))

  {:plotOptions {:series {:animation (:viz/animation options false)}
                 :variablepie    {:allowPointSelect true
                                  :dataLabels       {:enabled (get options :viz/dataLabels false)
                                                     :format  (get options :viz/labelFormat "")}}}})




;;;;;;;;;;;;;;
;
; these functions will convert the data from the current format into
; what the chart type actually wants. This may involve adding
; data to the :series, or rearranging the contents

(defn- process-data [data slice-at]

  (.log js/console "vari-pie process-data" (str data))
  (.log js/console "VARI-PROCESSED-DATA: " (str (map #(conj % (rand 100) false) data)))

  [{:colorByPoint true
    :zmin         0
    :innerSize    "20%"
    :minPointSize 10
    :keys         ["name" "y" "z" "selected"]
    :data         (map #(conj % (rand 100) false) data)}])


(defn convert-x-y
  [chart-type data options]

  (.log js/console (str "vari-pie/convert-x-y " chart-type))

  (process-data (get-in data [:data (get-in options [:src/extract])])
                (get-in options [:viz/slice-at])))


(defn convert-name-y
  [chart-type data options]

  (.log js/console (str "vari-pie/convert-name-y " chart-type
                       " //// " data " //// " options
                       " //// " (get-in data [:data :series 0 :data])))

  (process-data (get-in data [:data :series 0 :data] [])
                (get-in options [:viz/slice-at])))


;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(mc/register-type
  :vari-pie-chart {:chart-options   {:chart/type              :vari-pie-chart
                                     :chart/supported-formats [:data-format/name-y :data-format/x-y]
                                     :chart                   {:type  "variablepie"
                                                               :style {:labels {:fontFamily "monospace"
                                                                                :color      "#FFFFFF"}}}}
                   :merge-plot-option {:default plot-options}

                   :conversions       {:data-format/x-y convert-x-y
                                       :default         convert-name-y}})




