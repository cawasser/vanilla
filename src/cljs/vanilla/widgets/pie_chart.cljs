(ns vanilla.widgets.pie-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [cljsjs.highcharts]
            [cljsjs.jquery]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]))


(defn- render
  []
  [:div {:style {:width "100%" :height "70%"}}])

(def pie-chart-config
  {:chart {:type            "pie"
           :backgroundColor "transparent"

           :style           {:labels {
                                      :fontFamily "monospace"
                                      :color      "#FFFFFF"}}}
   :yAxis {:title  {:style {:color "#000000"}}
           :labels {:color "#ffffff"}}
   :xAxis {:labels {:style {:color "#fff"}}}})


(defn- plot-pie [this]
  (let [config     (-> this r/props :chart-options)
        render-to  {:render-to (r/dom-node this)}
        all-config (merge pie-chart-config config render-to)]

    (.log js/console (str "plot-pie " (-> this r/dom-node) ", "
                          (-> this r/dom-node .-children) "|"))

    (.highcharts (js/$ (r/dom-node this))
                 (clj->js all-config))))

(defn- pie-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-pie
                   :component-did-update plot-pie}))


(defn embed-pie [data options]
  (let [dats (util/process-data (get-in data [:data (get-in options [:src :extract])])
                                (get-in options [:src :slice-at]))]

    ;(.log js/console (str ":pie-chart " name " " slice-at))

    [:div {:style {:height "100%"}}
     [pie-chart
      {:chart-options
       {:title       {:text ""}
        :plotOptions {:series {:animation (get-in options [:viz :animation] false)}}
        :tooltip     (get-in options [:viz :tooltip] {})
        :series      dats}}]]))


(widget-common/register-widget
  :pie-chart
  (fn [data options]
    [basic/basic-widget data options
     [:div {:style {:height "40%" :marginTop "-25px"}}
      [embed-pie data options]]]))
