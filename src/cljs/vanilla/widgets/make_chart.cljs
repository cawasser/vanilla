(ns vanilla.widgets.make-chart
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [vanilla.widgets.util :as util]
            ["highcharts" :as Highcharts]
            ["highcharts-more" :as HighchartsMore]
            [vanilla.dark-mode :as dark]))

; required to make "extra" charts, like bubble and arearange, available.
(HighchartsMore Highcharts)

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
                       base-config plot-config (-> chart-config :chart-options) dark/dark-theme)]

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
  ;  " //// (registry-data)" registry-data)

  (rf/dispatch [:register-hc-type id registry-data]))




(defn make-chart
  "creates the correct reagent 'hiccup' and react/class to implement a
  Highcharts.js UI component that can be embedded inside any valid hiccup"

  [chart-config data options]

  ;(prn " entering make-chart " chart-config)

  ; TODO: should make-chart return a (fn [])?
  (let [chart-type      (-> chart-config :chart-options :chart/type)
        chart-reg-entry @(rf/subscribe [:hc-type chart-type])
        dom-node        (reagent/atom nil)
        base-config     (make-config chart-config data options)
        all-configs     (merge-configs base-config data options)]

        ;(prn "make-chart " chart-type
        ; " //// (chart-config) " chart-config
        ; " //// (chart-reg-entry) " chart-reg-entry
        ; " //// (all-configs) " all-configs )

    (reagent/create-class
      {:reagent-render
       (fn [args]
         ;@dom-node                                          ; be sure to render if node changes, doesnt seem necessary
         [:div#hc {:style {:width "100%" :height "100%"}}])

       :component-did-mount
       (fn [this]
         [:> (Highcharts/chart (reagent/dom-node this) (clj->js all-configs))])})))

;; in case we need to add :component-did-update in later, it used to look like this before shadow-cljs
        ;:component-did-update
        ;(fn [this old-argv]
        ;  (let [new-args (rest (reagent/argv this))
        ;        new-data (js->clj (second new-args))
        ;        base-config (make-config chart-config new-data options)
        ;        all-configs (merge-configs base-config new-data options)]
        ;
        ;    (prn "component-did-update " chart-type
        ;         " //// chart-config " chart-config
        ;         " //// chart-reg-entry " chart-reg-entry
        ;         " //// base-config " base-config
        ;         " //// (all-config)" all-configs)
        ;
        ;    (js/Highcharts.Chart. (reagent/dom-node this)
        ;                          (clj->js all-configs)))

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

  ;;;;;;;;;;;;ALL-CONFIGS;;;;;;;;;;;;; for future reference
  {:legendBackgroundColor "rgba(48, 48, 48, 0.8)",
   :labels {:style {:color "#CCC"}},
   :dataLabelsColor "#444",
   :chart/supported-formats [:data-format/label-y],
   :series [{:colorByPoint true,
             :keys ["name" "y" "selected" "sliced"],
             :data (["Apples" 23.921723055312473 false true]
                    ["Pears" 1.0911997822503738 false true]
                    ["Oranges" 73.0289964661802 false false]
                    ["Plums" 64.16060699437367 false false]
                    ["Bananas" 38.89849716522741 false true]
                    ["Peaches" 80.23576014616512 false false]
                    ["Prunes" 11.94279513674401 false true]
                    ["Avocados" 98.73950387707737 false false])}],
   :background2 "rgb(70, 70, 70)",
   :navigation {:buttonOptions {:symbolStroke "#DDDDDD",
                                :hoverSymbolStroke "#FFFFFF",
                                :theme {:fill {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                               :stops [[0.4 "#606060"] [0.6 "#333333"]]},
                                        :stroke "#000000"}}},
   :chart/type :pie-chart,
   :legend {:align "right",
            :verticalAlign "top",
            :layout "vertical",
            :itemStyle {:color "#CCC"},
            :itemHoverStyle {:color "#FFF"},
            :itemHiddenStyle {:color "#333"}},
   :colors ["#DDDF0D" "#7798BF" "#55BF3B" "#DF5353" "#aaeeee" "#ff0066" "#eeaaee" "#55BF3B" "#DF5353" "#7798BF" "#aaeeee"],
   :rangeSelector {:buttonTheme {:fill {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                        :stops [[0.4 "#888"] [0.6 "#555"]]},
                                 :stroke "#000000",
                                 :style {:color "#CCC", :fontWeight "bold"},
                                 :states {:hover {:fill {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                         :stops [[0.4 "#BBB"] [0.6 "#888"]]},
                                                  :stroke "#000000", :style {:color "white"}},
                                          :select {:fill {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                          :stops [[0.1 "#000"] [0.3 "#333"]]},
                                                   :stroke "#000000", :style {:color "yellow"}}}},
                   :inputStyle {:backgroundColor "#333", :color "silver"},
                   :labelStyle {:color "silver"}},
   :plotOptions {:series {:animation false, :nullColor "#444444"},
                 :pie {:allowPointSelect true,
                       :dataLabels {:enabled true,
                                    :format "{point.name}"},
                       :showInLegend true},
                 :line {:dataLabels {:color "#CCC"},
                        :marker {:lineColor "#333"}},
                 :spline {:marker {:lineColor "#333"}},
                 :scatter {:marker {:lineColor "#333"}},
                 :candlestick {:lineColor "white"}},
   :title {:text "", :style {:labels {:fontFamily "monospace", :color "#FFFFFF"},
                             :color "#FFF", :font "16px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
   :chart {:borderRadius 0,
           :type "pie",
           :plotBorderWidth 0,
           :borderWidth 0,
           :style {:labels {:fontFamily "monospace",
                            :color "#FFFFFF"}},
           :plotBackgroundColor nil,
           :plotShadow false,
           :backgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                             :stops [[0 "rgb(96, 96, 96)"] [1 "rgb(16, 16, 16)"]]}},
   :yAxis {:title {:align "high",
                   :style {:color "#AAA", :font "bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
           :labels {:overflow "justify",
                    :style {:color "#999",
                            :fontWeight "bold"}},
           :alternateGridColor nil,
           :minorTickInterval nil,
           :gridLineColor "rgba(255, 255, 255, .1)",
           :minorGridLineColor "rgba(255,255,255,0.07)",
           :lineWidth 0, :tickWidth 0},
   :textColor "#E0E0E0",
   :vanilla-mode "dark",
   :credits {:enabled false},
   :subtitle {:text "", :style {:color "#DDD", :font "12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
   :navigator {:handles {:backgroundColor "#666", :borderColor "#AAA"},
               :outlineColor "#CCC",
               :maskFill "rgba(16, 16, 16, 0.5)",
               :series {:color "#7798BF", :lineColor "#A6C7ED"}},
   :maskColor "rgba(255,255,255,0.3)",
   :toolbar {:itemStyle {:color "#CCC"}},
   :xAxis {:gridLineWidth 0, :lineColor "#999", :tickColor "#999", :labels {:style {:color "#999", :fontWeight "bold"}},
           :title {:style {:color "#AAA", :font "bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}}},
   :scrollbar {:barBackgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1}, :stops [[0.4 "#888"] [0.6 "#555"]]},
               :barBorderColor "#CCC",
               :buttonArrowColor "#CCC",
               :buttonBackgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1}, :stops [[0.4 "#888"] [0.6 "#555"]]},
               :buttonBorderColor "#CCC",
               :rifleColor "#FFF",
               :trackBackgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1}, :stops [[0 "#000"] [1 "#333"]]},
               :trackBorderColor "#666"},
   :tooltip {:valueSuffix "",
             :backgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                               :stops [[0 "rgba(96, 96, 96, .8)"] [1 "rgba(16, 16, 16, .8)"]]},
             :borderWidth 0, :style {:color "#FFF"}}}

  ())