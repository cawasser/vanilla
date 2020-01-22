(ns vanilla.widgets.dependency-chart
  (:require [vanilla.widgets.make-chart :as mc]))


;(defn plot-options
;  [chart-config data options]
;
;  ;(.log js/console (str "sankey-deps/plot-options " chart-config))
;
;  {:plotOptions {:series {:animation (:viz/animation options false)}}})


(def label-options {:dataLabels {:color    "#333"
                                 :textPath {:enabled    true
                                            :attributes {:dy 5}}
                                 :distance 10}
                    :size       "95%"})



(defn dependency-default
  [chart-type data options]

  (let [default (mc/default-conversion chart-type data options)
        ret     [(merge (first default) label-options)]]

    ;(prn "dependency-conversion (data) " data
    ;  " //// (default) " default
    ;  " //// (ret)" ret)

    ret))



(defn dependency-add-the-n
  [n-name default-n chart-type data options]

  (let [add-the-n (mc/add-the-n-conversion n-name default-n chart-type data options)
        ret       [(merge (first add-the-n) label-options)]]

    ;(prn "dependency-conversion (add-the-n) " add-the-n
    ;  " //// (ret)" ret)

    ret))




;;;;;;;;;;;;;;
;
; register all the data stuff so we have access to it
;
(defn register-type []
  (mc/register-type
    :dependency-chart {:chart-options     {:chart/type              :dependency-chart
                                           :chart/supported-formats [:data-format/from-to-n :data-format/from-to :data-format/from-to-e]
                                           :chart                   {:type "dependencywheel"}}

                       :merge-plot-option {:default mc/default-plot-options}

                       :conversions       {:default             dependency-default
                                           :data-format/from-to (partial dependency-add-the-n "weight" 1)}}))


