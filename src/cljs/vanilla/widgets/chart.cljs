(ns vanilla.widgets.chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [cljsjs.highcharts]
            [cljsjs.jquery]
            [dashboard-clj.widgets.core :as widget-common]))


(defn render
  []
  [:div {:style {:width "100%" :height "100%"}}])

(def bar-chart-config
  {:chart {:type            "column"
           :backgroundColor "transparent"

           :style           {:labels {
                                      :fontFamily "monospace"
                                      :color      "#FFFFFF"}}}
   :yAxis {:title  {:style {:color "#000000"}}
           :labels {:color "#ffffff"}}
   :xAxis {:labels {:style {:color "#fff"}}}})


(def line-chart-config
  {:chart {:type            "line"
           :backgroundColor "transparent"

           :style           {:labels {
                                      :fontFamily "monospace"
                                      :color      "#FFFFFF"}}}
   :yAxis {:title  {:style {:color "#000000"}}
           :labels {:color "#ffffff"}}
   :xAxis {:labels {:style {:color "#fff"}}}})


(defn plot-bar [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge bar-chart-config config)]
    (.highcharts (js/$ (r/dom-node this))
                 (clj->js all-config))))


(defn plot-line [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge line-chart-config config)]
    (.highcharts (js/$ (r/dom-node this))
                 (clj->js all-config))))

(defn bar-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-bar
                   :component-did-update plot-bar}))


(defn line-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-line
                   :component-did-update plot-line}))

(doseq [{:keys [name render-fn]} [{:name :bar-chart :render-fn bar-chart}
                                  {:name :line-chart :render-fn line-chart}]]
  (widget-common/register-widget
    name
    (fn [data options]
      (.log js/console (str name) (str data) (str options))
      ;(.log js/console (str extract))
      [:div {:class "chart" :style {:height (get-in options [:viz :height]) :width "100%"}}
       [:div {:class "title-wrapper"}
        [:h3 {:class "title"
              :style {:background-color (get-in options [:viz :banner-color])}}
         (get-in options [:viz :title])]]

       [:div
        [:select {:on-click #(do
                               (.log js/console "clicked")
                               (.stopPropagation (.-event %)))}
         (map #(into ^{:key %} [:option] (:name %))
              (get-in data [:data (get-in options [:src :extract])]))]]

       [:div {:class (str (get-in options [:viz :style-name])) :style {:width "95%" :height "40%"}}
        [render-fn
         {:chart-options
          {:title  {:text (get data (get-in options [:src :name]))}

           :xAxis  {:title      {:text (get-in options [:viz :x-title])}
                    :categories (into [] (range (count (get-in data [:data
                                                                     (get-in options [:src :extract])
                                                                     (get-in options [:src :selector])
                                                                     (get-in options [:src :values])]))))}

           :yAxis  {:title {:text (get-in options [:viz :y-title])}}

           :series [{:name (:chart-title options)
                     :data (into [] (get-in data [:data
                                                  (get-in options [:src :extract])
                                                  (get-in options [:src :selector])
                                                  (get-in options [:src :values])]))}]}}]]])))
