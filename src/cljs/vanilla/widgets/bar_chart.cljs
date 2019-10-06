(ns vanilla.widgets.bar-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [cljsjs.highcharts]
            [cljsjs.jquery]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]))


(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])


(def bar-chart-config
  {:chart {:type            :column
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


(defn bar-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-bar
                   :component-did-update plot-bar}))


(widget-common/register-widget
  :bar-chart
  (fn [data options]
    (let [dats (get-in data [:data (get-in options [:src :extract])])
          num  (count dats)]
      ;(.log js/console (str ":bar-chart " (into [] (map str (range (count (:values (first dats))))))))

      [basic/basic-widget data options

        [bar-chart
         {:chart-options
          {:zoomType    :x
           :title       {:text ""}

           :xAxis       {:title {:text (get-in options [:viz :x-title] "x-axis")}}

           :yAxis       {:title      {:text (get-in options [:viz :y-title] "y-axis")}
                         :color      (get-in options [:viz :line-colors])
                         :categories (into [] (map str (range (count (:values (first dats))))))}

           :plotOptions {:series  {:animation (-> options :viz :animation)}
                         :tooltip (-> options :viz :tooltip)
                         :column  {:pointPadding 0.2
                                   :borderWidth  0}}

           :series      (into []
                              (for [n (range num)]
                                {:name (get-in dats [n (get-in options [:src :name] :name)] (str "set " n))
                                 :data (into [] (get-in dats [n (get-in options [:src :values] :values)]))}))}}]])))

