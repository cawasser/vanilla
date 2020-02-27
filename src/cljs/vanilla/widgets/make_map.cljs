(ns vanilla.widgets.make-map
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [vanilla.widgets.util :as util]))

(declare default-conversion)

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
    ;  " //// (chart-config)" chart-config
    ;  " //// (data)" data-config
    ;  " //// (base-config)" base-config
    ;  " //// (plot-config)" plot-config
    ;  " //// (final-config)" final-config)

    final-config))






(defn- merge-configs [chart-config data options]

  (let [chart-type (-> chart-config :chart/type)
        dat        (if (instance? Atom data) @data data)
        converted  (get-conversion chart-type dat options)
        ret        (if converted
                     (assoc chart-config :series converted)
                     chart-config)]

    ;(prn "merge-configs " chart-type
    ;  " //// (chart-config) " chart-config
    ;  " //// (data) " data
    ;  " //// (converted)" converted
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
        ret (for [{keys :keys data :data :as all} series]
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

  [chart-config data options]

  (let [dom-node        (reagent/atom nil)
        chart-type      (-> chart-config :chart-options :chart/type)
        chart-reg-entry @(rf/subscribe [:hc-type chart-type])]

    ;(prn "MAKE-chart " chart-type
    ;  " //// (chart-config)" chart-config
    ;  " ////// (chart-reg-entry)" chart-reg-entry)

    (reagent/create-class
      {:reagent-render
       (fn [args]
         @dom-node                                          ; be sure to render if node changes
         [:div {:style {:width (get options :viz/width "100%") :height "100%"}}])

       :component-did-mount
       (fn [this]
         (let [node (reagent/dom-node this)]

           ;(prn "component-did-mount " chart-type)

           (reset! dom-node node)))

       :component-did-update
       (fn [this old-argv]
         (let [new-args (rest (reagent/argv this))
               new-data (js->clj (second new-args))
               base-config (make-config chart-config new-data options)
               all-configs (merge-configs base-config new-data options)]

           ;(prn "component-did-update " chart-type
           ;  " //// chart-config " chart-config
           ;  " //// chart-reg-entry " chart-reg-entry
           ;  " //// base-config " base-config
           ;  " //// (all-config)" all-configs)

           (js/Highcharts.mapChart. (reagent/dom-node this)
                                 (clj->js all-configs))))})))
