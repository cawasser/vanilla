(ns vanilla.widgets.chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]))


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

(def line-chart-config
  {:chart   {:type            :line
             :backgroundColor "transparent"
             :style           {:labels {
                                        :fontFamily "monospace"
                                        :color      "#FFFFFF"}}}
   :yAxis   {:title  {:style {:color "#000000"}}
             :labels {:color "#ffffff"}}
   :xAxis   {:labels {:style {:color "#fff"}}}
   :credits {:enabled false}})


(defn- plot-line [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge-with clojure.set/union line-chart-config config)]

    (.log js/console (str "plot-line "))

    (js/Highcharts.Chart. (r/dom-node this)
                          (clj->js all-config))))

(defn line-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-line
                   :component-did-update plot-line}))

(defn embed-line [data options]
  (fn [data options]
    (let [dats (get-in data [:data (get-in options [:src :extract])])
          num  (count dats)]

      ;(.log js/console (str ":line-chart " dats))))

      [line-chart
       {:chart-options
        {:chart       {:zoomType "x"}
         :title       {:text ""}

         :xAxis       {:title {:text (get-in options [:viz :x-title] "x-axis")}}

         :yAxis       {:title {:text (get-in options [:viz :y-title] "y-axis")}}

         :plotOptions {:line    {:lineWidth (get-in options [:viz :line-width] 1)}
                       :series  {:animation (get-in options [:viz :animation] false)}
                       :tooltip (get-in options [:viz :tooltip] {})}


         :series      (into []
                            (for [n (range num)]
                              {:name (get-in dats [n (get-in options [:src :name] :name)] (str "set " n))
                               :data (into [] (get-in dats [n (get-in options [:src :values] :values)]))}))}}])))


(widget-common/register-widget
  :line-chart
  (fn [data options]

    [basic/basic-widget data options

     [:div {:style {:width "95%" :height "100%"}}

      [embed-line {:data {:spectrum-data (spectrum-data)}} options]]]))
