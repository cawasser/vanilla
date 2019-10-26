(ns vanilla.widgets.column-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]))


(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])


(def column-chart-config
  {:chart   {:type            "column"
             :backgroundColor "transparent"

             :style           {:labels {
                                        :fontFamily "monospace"
                                        :color      "#FFFFFF"}}}
   :credits {:enabled false}
   :yAxis   {:title  {:style {:color "#000000"}}
             :labels {:color "#ffffff"}}
   :xAxis   {:labels {:style {:color "#fff"}}}})


(defn- plot-column [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge-with clojure.set/union column-chart-config config)]

    ;(.log js/console (str "plot-column " all-config))

    (js/Highcharts.Chart. (r/dom-node this)
                          (clj->js all-config))))


(defn column-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-column
                   :component-did-update plot-column}))


(defn embed-column [data options series]
  (let [dats (get-in data [:data (get-in options [:src :extract])])
        num  (count dats)]

    ;(.log js/console (str "embed-column " data))

    [column-chart
     {:chart-options
      {:chart       {:zoomType "x"}

       :title       {:text ""}

       :xAxis       {:title {:text (get-in options [:viz :x-title] "x-axis")}}

       :yAxis       {:title      {:text (get-in options [:viz :y-title] "y-axis")}
                     :color      (get-in options [:viz :line-colors])
                     :categories (into [] (map str (range (count (:values (first dats))))))}

       :plotOptions {:series  {:animation (get-in options [:viz :animation] false)}
                     :tooltip (get-in options [:viz :tooltip] {})
                     :column  {:pointPadding 0.2
                               :borderWidth  0
                               :dataLabels   {:enabled
                                              (get-in options [:viz :data-labels] false)}}}

       :series      series}}]))


;(widget-common/register-widget
;  :column-chart
;  (fn [data options]
;    (let []
;
;      [basic/basic-widget data options
;       [:div {:style {:width "95%" :height "100%"}}
;
;        [embed-column data options (util/line->bar data options)]]])))
;
