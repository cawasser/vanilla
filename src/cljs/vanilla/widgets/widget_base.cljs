(ns vanilla.widgets.widget-base
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.util :as util]))


;;;;;;;;;;;;;;;;;
;
; One structure to specify each highcharts "type" along with a few
; small config things
;
; :chart/supported-formats is a vector of the keys that this chart
;                          can display.
;                          TODO used to find the correct format converters
;
(def chart-configs
  {
   :area-chart
   {:chart/supported-formats [:data-format/y :data-format/x-y]
    :chart {:type     "area"
            :zoomType "x"}
    :yAxis {:min    0
            :title  {:align "high"}
            :labels {:overflow "justify"}}}

   :bar-chart
   {:chart/supported-formats [:data-format/y :data-format/x-y]
    :chart {:type     "bar"
            :zoomType "x"}
    :yAxis {:min    0
            :title  {:align "high"}
            :labels {:overflow "justify"}}}

   :bubble-chart
   {:chart/supported-formats [:data-format/x-y-n :data-format/x-y-e]
    :chart   {:type "bubble"}
    :series  {:dataLabels {:format "{point.name}"}}}

   :column-chart
   {:chart/supported-formats [:data-format/y :data-format/x-y]
    :chart {:type "column"
            :zoomType "x"}
    :yAxis {:min    0
            :title  {:align "high"}
            :labels {:overflow "justify"}}}

   :line-chart
   {:chart/supported-formats [:data-format/y :data-format/x-y]
    :chart {:type     "line"
            :zoomType "x"}
    :yAxis {:min    0
            :title  {:align "high"}
            :labels {:overflow "justify"}}}

   :network-chart
   {:chart/supported-formats [:data-format/from-to :data-format/form-to-n]
    :chart  {:type "networkgraph"}
    :series {:dataLabels {:linkFormat ""}}}

   :org-chart
   {:chart/supported-formats [:data-formats/form-to]
    :chart {:type "organization"}
    :plotOptions {:type "organization"
                  :keys ["from", "to"]}
    :series {:type "organization"}}

   :pie-chart
   {:chart/supported-formats [:data-format/x-y]
    :chart {:type "pie"}}

   :sankey-chart
   {:chart/supported-formats [:data-format/from-to :data-format/form-to-n]
    :chart {:type "sankey"}
    :series {:type "sankey"}}})




(defn get-config [type]
  (let [config (get chart-configs type {})]

    (.log js/console (str "get-config " type ", " config))

    config))




(defn make-widget [id chart-config]

  (.log js/console (str "make-widget " id ", " chart-config))

  (widget-common/register-widget

    id

    (fn [data options]

      [basic/basic-widget data options
       [:div {:style {:width "95%" :height "100%"}}
        [mc/make-chart chart-config data options]]])))


(defn make-stacked-widget [id chart-configs]
  (widget-common/register-widget

    id

    (fn [data options]

      (.log js/console (str "dual-chart " data ", " options))

      [basic/basic-widget data options

       (into [:div]
         (map
           #([:div {:style {:width "95%" :height "65%"}}
              [mc/make-chart % data options]])
           chart-configs))])))


(defn make-side-by-side [id left-chart right-chart]
  (widget-common/register-widget

    id

    (fn [data options]

      ;(.log js/console (str ":side-by-side-chart " data))

      [basic/basic-widget data options

       [:div.columns {:style {:height "100%" :width "100%" :marginTop "10px"}}

        [:div.column.is-two-thirds {:style {:height "200px"}}
         [mc/make-chart left-chart data options]]

        [:div.column.is-one-third {:style {:height "200px"}}
         [mc/make-chart right-chart data options]]]])))

