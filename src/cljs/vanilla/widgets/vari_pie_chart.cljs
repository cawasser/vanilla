(ns vanilla.widgets.vari-pie-chart
  (:require [vanilla.widgets.make-chart :as mc]))



(defn plot-options
  [chart-config data options]

  ;(.log js/console (str "pie/plot-options " chart-config))

  {:plotOptions {:series      {:animation (:viz/animation options false)}
                 :variablepie {:allowPointSelect true
                               :dataLabels       {:enabled (get options :viz/dataLabels false)
                                                  :format  (get options :viz/labelFormat "")}}}})


(def default-options {:colorByPoint true
                      :zmin         0
                      :innerSize    "20%"
                      :minPointSize 10})




(defn- add-slice-data [data slice-at]

  ;(prn "vari-pie add-slice-data STARTED!! " data)

  (let [new-keys (conj (:keys data) "selected" "sliced")
        new-data (map #(conj % false (< (second %) slice-at)) (:data data))
        ret [{:colorByPoint true
              :keys         new-keys
              :data         new-data}]]

    ;(prn "add-slice-data " data
    ;  " //// (slice-at) " slice-at
    ;  " //// (new-keys) " new-keys
    ;  " //// (new-data) " new-data
    ;  " //// (ret) " ret)

    ret))


(defn add-the-y-conversion [default-y chart-config data options]

  (let [series (get-in data [:data :series])
        ret (for [{data :data :as all} series]
              (assoc all
                :keys ["name" "y" "z"]
                :data (into []
                        (for [[x z] data]
                          [x default-y z]))))]

    ;(prn "add-the-y-conversion (from)" data
    ;  " //// (series) " series
    ;  " /// (to) " ret)

    (into [] ret)))


(defn- process-data [default-y slice-at chart-config data options]

  ;(prn "vari-pie PROCESS started (data) " data)

  (let [add-the-n (add-the-y-conversion default-y chart-config data options)
        add-slice (add-slice-data (first add-the-n) slice-at)
        ret    (for [d add-slice]
                 (merge d default-options))]

    ;(prn "vari-pie process-data (data) " data
    ;  " //// (add-th-n) " add-the-n
    ;  " //// (add-slice) " add-slice
    ;  " //// (ret) " ret)

    ret))



;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :vari-pie-chart {:chart-options     {:chart/type              :vari-pie-chart
                                         :chart/supported-formats [:data-format/label-y-n :data-format/label-y :data-format/label-y-e]
                                         :chart                   {:type  "variablepie"
                                                                   :style {:labels {:fontFamily "monospace"
                                                                                    :color      "#FFFFFF"}}}}
                     :merge-plot-option {:default plot-options}

                     :conversions       {:default mc/default-conversion
                                         :data-format/label-y (partial process-data 100 45)}}))




