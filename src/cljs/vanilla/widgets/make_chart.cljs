(ns vanilla.widgets.make-chart
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [vanilla.widgets.util :as util]
            [re-frame-highcharts.utils :as hc]))


(declare default-conversion)


(rf/reg-event-db
  :register-hc-type
  (fn-traced [db [_ type type-fn]]
    ;(prn "registering highcharts type " type)
    (assoc-in db [:hc-type type] type-fn)))

;;;;;;;;;;;;;;;;;
;
; PRIVATE support functions
;
;
(defn- plot-config [chart-type chart-config data options]
  (let [chart-reg-entry @(rf/subscribe [:hc-type chart-type])
        format-type     (get-in data [:data :data-format])
        pc-fn           (get-in chart-reg-entry [:merge-plot-option format-type]
                          (get-in chart-reg-entry [:merge-plot-option :default]))]

    ;(prn "plot-config " chart-type
    ; " ///// (format-type)" format-type
    ; " ///// (pc-fn)" pc-fn)

    (if pc-fn
      (pc-fn chart-config data options))))



(defn- get-conversion [chart-type data options]
  (let [chart-reg-entry @(rf/subscribe [:hc-type chart-type])
        format-type     (get-in data [:data :data-format])
        conversions     (:conversions chart-reg-entry)
        conv-fn         (get conversions format-type (get conversions :default default-conversion))
        ret             (if data
                          (conv-fn chart-type data options)
                          [])]

    ;(prn "get-conversion " chart-type "/" format-type
    ;     " //// (data)" data
    ;     " //// (chart-reg-entry) " chart-reg-entry
    ;     " //// (conversions) " conversions
    ;     " //// (conv-fn) " conv-fn
    ;     " //// (ret) " ret)

    ret))




(defn- make-config
  "combine the various config items into the one 'master' config
   for all of Highcharts, since it has only 1 function:

         Highcharts.Chart()

   NOTE: this function will get more sophisticated as we flesh out the
         handling of :data-format/xxx and :src/xxx metadata keys"

  [chart-config data options]

  ;(prn "make-config " (-> chart-config :chart-options :chart/type)
  ;  " //// (chart-config)" chart-config
  ;  " ///// (data)" data)

  (let [chart-type   (-> chart-config :chart-options :chart/type)
        data-config  (if (instance? Atom data) @data data)
        base-config  {:title       {:text  (get options :viz/chart-title "")
                                    :style {:labels {:fontFamily "monospace"
                                                     :color      "#FFFFFF"}}}

                      :subtitle    {:text ""}

                      :tooltip     {:valueSuffix (:src/y-valueSuffix data-config "")}

                      :plotOptions {:series {:animation (:viz/animation options false)}}

                      :credits     {:enabled false}}

        plot-config  (plot-config chart-type base-config data options)

        final-config (util/deep-merge-with
                       util/combine
                       base-config plot-config (-> chart-config :chart-options))]

    ;(prn "make-config after " chart-type
    ;  ;" //// (chart-config)" chart-config
    ;  " //// (data)" data-config
    ;  ;" //// (base-config)" base-config
    ;  ;" //// (plot-config)" plot-config
    ;  " //// (final-config)" final-config)

    final-config))






(defn- merge-configs [chart-config data options]

  (let [chart-type (-> chart-config :chart/type)
        dat        (if (instance? Atom data) @data data)
        converted  (get-conversion chart-type dat options)
        ret        (if converted
                     (assoc chart-config :series converted)
                     chart-config)]

    ;(prn "merge-config after " chart-type
    ;  ;" //// (chart-config) " chart-config
    ;  " //// (data) " data
    ;  ;" //// (converted)" converted)
    ;  " //// (ret)" ret)


    ret))




;;;;;;;;;;;;;;;;;
;
; PUBLIC interface
;
;
(defn default-plot-options [chart-type data options]
  {})


(defn default-conversion [chart-type data options]
  (let [ret (get-in data [:data :series])]

    ;(prn "default-conversion " chart-type
    ;  " //// (data) " data
    ;  " //// (ret)" ret)

    ret))



(defn add-the-n-conversion [n-name default-n chart-config data options]

  (let [series (get-in data [:data :series])
        ret    (for [{keys :keys data :data :as all} series]
                 (assoc all
                   :keys (conj keys n-name)
                   :data (into []
                           (for [[x y] data]
                             [x y default-n]))))]

    ;(prn "add-the-n-conversion (from)" data
    ;  " //// (series) " series
    ;  " /// (to) " ret)

    (into [] ret)))




(defn register-type
  "register the data structure manipulations to support a certain
   Highcharts chart type, identified by 'id'

   'registry-data' provides a map of chart-options, plot-option
   configurations, and data conversion functions"

  [id registry-data]

  ;(prn "register-type " id
  ; " //// (registry-data)" registry-data)

  (rf/dispatch [:register-hc-type id registry-data]))




(defn make-chart
  "creates the correct reagent 'hiccup' and react/class to implement a
  Highcharts.js UI component that can be embedded inside any valid hiccup"

  [widget data options]

  ;(prn " entering make-chart " chart-config)

  (let [chart-config @(rf/subscribe [:hc-type (:type widget)])
        base-config (make-config chart-config @data options)
        all-configs (merge-configs base-config @data options)
        ret [:div {:style {:width (get options :viz/width "100%") :height "100%"}}
             [hc/chart {:chart-meta {:id (:key widget) :redo true}
                        :chart-data (merge-configs base-config @data options)}]]]


    ;(prn "make-chart " widget
    ;  " //// base-config " base-config
    ;  " //// all-configs " all-configs)
      ;" //// options " options
      ;" //// ret" ret
      ;" //// data " data)

    ret))




;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;

(comment

  {:chart-options     {:chart/type              :dependency-chart,
                       :chart/supported-formats [:data-format/from-to-n :data-format/from-to-e :data-format/from-to],
                       :chart                   {:type "dependencywheel"},
                       :plot-options            {:dataLabels {:color    "#333",
                                                              :textPath {:enabled true, :attributes {:dy 5}},
                                                              :distance 10},
                                                 :size       "95%"}},
   :merge-plot-option {:default ""},
   :conversions       {:default             "",
                       :data-format/from-to ""}}

  ())