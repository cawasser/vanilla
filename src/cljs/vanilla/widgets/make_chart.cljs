(ns vanilla.widgets.make-chart
    (:require [reagent.core :as r]
              [reagent.ratom :refer-macros [reaction]]
              [dashboard-clj.widgets.core :as widget-common]
              [vanilla.widgets.basic-widget :as basic]
              [vanilla.widgets.util :as util]))
  (defn- render
    []
    [:div {:style {:width "100%" :height "100%"}}])
  
  
  (defn- plot [chart-config]
    (fn [this]
      (let [config     (-> this r/props :chart-options)
            all-config (merge-with clojure.set/union chart-config config)]
  
        (.log js/console (str "plot "
                              (:title chart-config) ","
                              config ", "
                              chart-config all-config))
  
        (js/Highcharts.Chart. (r/dom-node this)
                              (clj->js all-config)))))
  
  
  (defn- chart
    [chart-options]
    (r/create-class {:reagent-render       render
                     :component-did-mount  (plot chart-options)
                     :component-did-update (plot chart-options)}))
  
  
  
  
  (defn embed-chart [chart data options]
    (let []
  
      (.log js/console (str "embed-network-graph " (get-in data [(get-in options [:src :extract])])))
  
      [chart (:chart-options options)]))
  
  
  
  (defn register-widget [id chart-type & opt-data]
    (widget-common/register-widget
      id
      (fn [data options]
        (let []
  
          [basic/basic-widget data options
           [:div {:style {:width "95%" :height "100%"}}
  
            [chart-type {:data opt-data} options]]]))))
  
  