(ns vanilla.widgets.bar-chart
  (:require [reagent.core :as r]
    [reagent.ratom :refer-macros [reaction]]
    [cljsjs.highcharts]
    [cljsjs.jquery]
    [dashboard-clj.widgets.core :as widget-common]))


(defn- render
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


(defn- plot-bar [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge bar-chart-config config)]
    (.highcharts (js/$ (r/dom-node this))
                 (clj->js all-config))))


(defn- bar-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-bar
                   :component-did-update plot-bar}))


(widget-common/register-widget
  :bar-chart
  (fn [data options]
    (.log js/console (str (get-in data [:data
                                        (get-in options [:src :extract] :data)
                                        (get-in options [:src :selector] :selector)
                                        (get-in options [:src :name] :name)])))
    [:div {:class "chart" :style {:height (get-in options [:viz :height]) :width "100%"}}
     [:div {:class "title-wrapper"}
      [:h3 {:class "title"
            :style {:background-color (get-in options [:viz :banner-color] "lightblue")}}
       (get-in options [:viz :title])]]

     [:div
      [:select {:on-click #(do
                             (.log js/console "clicked")
                             (.stopPropagation (.-event %)))}
       (map #(into ^{:key %} [:option] (:name %))
            (get-in data [:data (get-in options [:src :extract] :data)]))]]

     [:div {:class (str (get-in options [:viz :style-name] "widget")) :style {:width "95%" :height "40%"}}
      [bar-chart
       {:chart-options
        {:title       {:text (get-in data
                                     [:data
                                      (get-in options [:src :extract] :data)
                                      (get-in options [:src :selector] :selector)
                                      (get-in options [:src :name] :name)])}

         :xAxis       {:title      {:text (get-in options [:viz :x-title] "x-axis")}
                       :categories (into [] (range (count (get-in data [:data
                                                                        (get-in options [:src :extract] :data)
                                                                        (get-in options [:src :selector] :selector)
                                                                        (get-in options [:src :values] :values)]))))}

         :yAxis       {:title {:text (get-in options [:viz :y-title] "y-axis")}}

         :plotOptions {:series  {:animation (-> options :viz :animation)}
                       :tooltip (-> options :viz :tooltip)}

         :series      [{:name (get-in options [:viz :chart-title] "data")
                        :data (into [] (get-in data [:data
                                                     (get-in options [:src :extract] :data)
                                                     (get-in options [:src :selector] :selector)
                                                     (get-in options [:src :values] :values)]))}]}}]]]))
