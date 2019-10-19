(ns vanilla.widgets.network-graph-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]))


(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])


(def network-graph-chart-config
  {:chart   {:type            "networkgraph"
             :backgroundColor "transparent"}

   :style   {:labels {:fontFamily "monospace"
                      :color      "#FFFFFF"}}
   :credits {:enabled false}})


(defn- plot-network-graph [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge-with clojure.set/union network-graph-chart-config config)]

    (.log js/console (str "plot-network-graph " config ", " all-config))

    (js/Highcharts.Chart. (r/dom-node this)
                          (clj->js all-config))))


(defn network-graph-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-network-graph
                   :component-did-update plot-network-graph}))


(defn embed-network-graph [data options]
  (let []

    ;(.log js/console (str "embed-network-graph "
    ;                      (get-in data [:data (get-in options [:src :extract])])))

    [network-graph-chart
     {:chart-options
      {:title       {:text ""}
       :plotOptions {:networkgraph    {:keys ["from", "to"]}}
                     ;:layoutAlgorithm {:enableSimulation true
                     ;                  :integration      "verlet"
                     ;                  :linkLength       100}}

       :series      (map #(merge
                            {:dataLabels {:enabled    true
                                          :linkFormat ""}}
                            %)
                         (get-in data [:data (get-in options [:src :extract])]))}}]))


(widget-common/register-widget
  :network-graph-chart
  (fn [data options]
    (let []

      [basic/basic-widget data options
       [:div {:style {:width "95%" :height "100%"}}

        [embed-network-graph data options]]])))

