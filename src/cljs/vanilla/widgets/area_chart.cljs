(ns vanilla.widgets.area-chart
    (:require [reagent.core :as r]
              [reagent.ratom :refer-macros [reaction]]
              [cljsjs.highcharts]
      ;[cljsjs.jquery]
              [dashboard-clj.widgets.core :as widget-common]
              [vanilla.widgets.basic-widget :as basic]
              [vanilla.widgets.util :as util]))

(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])

(def area-chart-config
  {:chart   {:type            "area"
             :backgroundColor "transparent"
             :style           {:labels {
                                        :fontFamily "monospace"
                                        :color      "#FFFFFF"}}}
   :yAxis   {:title  {:style {:color "#000000"}}
             :labels {:color "#ffffff"}}
   :xAxis   {:labels {:style {:color "#fff"}}}
   :credits {:enabled false}})

(defn- plot-area [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge area-chart-config config)]

    (.log js/console (str "plot-area " all-config))

    (js/Highcharts.Chart. (r/dom-node this)
                          (clj->js all-config))))

(defn- area-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-area
                   :component-did-update plot-area}))

(defn embed-area [data options series]
  (let [dats (get-in data [:data (get-in options [:src :extract])])
        num  (count dats)]

    (.log js/console (str "embed-area " data))

    ;accessibility option?
    ;tooltip option dif
    [area-chart
     {:chart-options
      {:zoomType    :x
       :title       {:text ""}

       :xAxis       {:title {:text (get-in options [:viz :x-title] "x-axis")}}

       :yAxis       {:title      {:text (get-in options [:viz :y-title] "y-axis")}
                     :color      (get-in options [:viz :line-colors])
                     :categories (into [] (map str (range (count (:values (first dats))))))}

       :plotOptions {:series  {:animation (get-in options [:viz :animation] false)}
                     :tooltip (get-in options [:viz :tooltip] {})
                     :column  {:pointPadding 0.2
                               :borderWidth  0
                               :pointStart   {:enabled  ;TODO
                                              (get-in options [:viz :point-start] false)}
                                :marker}}       ;TODO + marker options

       :series      series}}]))

(widget-common/register-widget
  :area-chart
  (fn [data options]
    (let []

      [basic/basic-widget data options
       [:div {:style {:width "95%" :height "100%"}}
                                ;  TODO vvvvv implement
        [embed-area data options (util/area->bar data options)]]])))

