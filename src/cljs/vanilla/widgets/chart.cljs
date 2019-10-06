(ns vanilla.widgets.chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [cljsjs.highcharts]
            [cljsjs.jquery]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]))


(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])

(def line-chart-config
  {:chart {:type            :line
           :backgroundColor "transparent"

           :style           {:labels {
                                      :fontFamily "monospace"
                                      :color      "#FFFFFF"}}}
   :yAxis {:title  {:style {:color "#000000"}}
           :labels {:color "#ffffff"}}
   :xAxis {:labels {:style {:color "#fff"}}}})


(defn- plot-line [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge line-chart-config config)]
    (.highcharts (js/$ (r/dom-node this))
                 (clj->js all-config))))

(defn line-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-line
                   :component-did-update plot-line}))


(widget-common/register-widget
  :line-chart
  (fn [data options]
    (let [dats (get-in data [:data (get-in options [:src :extract])])
          num  (count dats)]
      ;(.log js/console (str ":line-chart " (str (first dats))))

      [basic/basic-widget data options

        [line-chart
         {:chart-options
          {:zoomType    :x
           :title       {:text ""}

           :xAxis       {:title {:text (get-in options [:viz :x-title] "x-axis")}}

           :yAxis       {:title {:text (get-in options [:viz :y-title] "y-axis")}}

           :plotOptions {:line    {:lineWidth (get-in options [:viz :line-width] 1)}
                         :series  {:animation (-> options :viz :animation)}
                         :tooltip (-> options :viz :tooltip)}

           :series      (into []
                              (for [n (range num)]
                                {:name  (get-in dats [n (get-in options [:src :name] :name)] (str "set " n))
                                 :data  (into [] (get-in dats [n (get-in options [:src :values] :values)]))}))}}]])))
