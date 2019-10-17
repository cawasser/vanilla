(ns vanilla.widgets.sankey-chart
  (:require [reagent.core :as r]
          [reagent.ratom :refer-macros [reaction]]
          [dashboard-clj.widgets.core :as widget-common]
          [vanilla.widgets.basic-widget :as basic]
          [vanilla.widgets.util :as util]))

(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])


(def sankey-chart-config
  {:chart   {:type            "sankey"
             :backgroundColor "transparent"}

             ;:style           {:labels {
             ;                           :fontFamily "monospace"
             ;                           :color      "#FFFFFF"}}}
   :credits {:enabled false}})


(defn- plot-sankey [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge-with clojure.set/union sankey-chart-config config)]

    (.log js/console (str "plot-sankey " all-config))

    (js/Highcharts.Chart. (r/dom-node this)
                          (clj->js all-config))))


(defn sankey-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-sankey
                   :component-did-update plot-sankey}))


(defn embed-sankey [data options]
  (let []

    (.log js/console (str "embed-sankey " data))

    [sankey-chart
     {:chart-options
      {:title       {:text ""}
       :plotOptions {:series {:animation (get-in options [:viz :animation] false)}}
       :series      [{:type "sankey"
                      :keys (get-in data [:data (get-in options [:src :keys])])
                      :data (get-in data [:data (get-in options [:src :extract])])}]}}]))


(widget-common/register-widget
  :sankey-chart
  (fn [data options]
    (let []

      [basic/basic-widget data options
       [:div {:style {:width "95%" :height "100%"}}

        [embed-sankey data options]]])))
