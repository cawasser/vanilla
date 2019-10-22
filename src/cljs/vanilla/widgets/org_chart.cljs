(ns vanilla.widgets.org-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]))


(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])


(def org-graph-chart-config
  {:chart   {:type            "organization"
             :backgroundColor "transparent"}

   :style   {:labels {:fontFamily "monospace"
                      :color      "#FFFFFF"}}
   :credits {:enabled false}})


(defn- plot-org-graph [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge-with clojure.set/union org-graph-chart-config config)]

    (.log js/console (str "plot-org-graph " config ", " all-config))

    (js/Highcharts.Chart. (r/dom-node this)
                          (clj->js all-config))))


(defn org-graph-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-org-graph
                   :component-did-update plot-org-graph}))


(defn embed-org-graph [data options]
  (let []

    ;(.log js/console (str "embed-org-graph "
    ;                      (get-in data [:data (get-in options [:src :extract])])))

    [org-graph-chart
     {:chart-options
      {:title       {:text ""}
       :plotOptions {:type "organization"
                     :keys ["from", "to"]}
       ;:layoutAlgorithm {:enableSimulation true
       ;                  :integration      "verlet"
       ;                  :linkLength       100}}

       :series      (map #(merge
                            {:dataLabels {:enabled    true
                                          :linkFormat ""}
                             :type       "organization"}
                            %)
                         (get-in data [:data (get-in options [:src :extract])]))}}]))


(widget-common/register-widget
  :org-chart
  (fn [data options]
    (let []

      [basic/basic-widget data options
       [:div {:style {:width "95%" :height "100%"}}

        [embed-org-graph data options]]])))
