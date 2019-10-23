(ns vanilla.widgets.area-chart
    (:require [reagent.core :as r]
              [reagent.ratom :refer-macros [reaction]]
              [dashboard-clj.widgets.core :as widget-common]
              [vanilla.widgets.basic-widget :as basic]
              [vanilla.widgets.util :as util]
              [vanilla.widgets.widget-base :as wb]
              [vanilla.widgets.make-chart :as mc]))

(defn spectrum-data []
  [{:name   "trace-1"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}
   {:name   "trace-2"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}
   {:name   "trace-3"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}])

(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])


(defn- plot-area [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge (wb/chart-config "area") config)]

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

    
    [area-chart
     {:chart-options
      {:zoomType    :x
       :title       {:text ""}

       :xAxis       {:title   {:text (get-in options [:viz :x-title] "x-axis")}
                     :allowDecimals (get-in options [:viz :allowDecimals] false)
                     :categories (into [] (map str (range (count (:values (first dats))))))}
                
       :yAxis       {:title   {:text (get-in options [:viz :y-title] "y-axis")}}
                     ;:labels {:formatter (into [] (map str (range (count (:values (first dats))))))}}

       :plotOptions {:series  {:animation (get-in options [:viz :animation] false)}
                     :tooltip (get-in options [:viz :tooltip] {})
                     :column  {:pointPadding 0.2
                               :borderWidth  0}}       ;TODO + marker options

       :series      series}}]))

(widget-common/register-widget
  :area-chart
  (fn [data options]
    (let []

      [basic/basic-widget data options
       [:div {:style {:width "95%" :height "100%"}}
        [embed-area {:data {:spectrum-data (spectrum-data)}} options (util/line->bar {:data {:spectrum-data (spectrum-data)}} options)]]])))

