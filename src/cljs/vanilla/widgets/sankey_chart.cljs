(ns vanilla.widgets.sankey-chart
  (:require [reagent.core :as r]
          [reagent.ratom :refer-macros [reaction]]
          ;[cljsjs.highcharts]
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
        all-config (merge sankey-chart-config config)]

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




;(js/Highcharts.Chart.
;  "container"
;  {:title  {:text "Highcharts Sankey Diagram"}
;
;   :series [{
;             :type "sankey"
;             :name "Sankey demo series"
;             :keys ["from" "to" "weight"]
;             :data [["Brazil" "Portugal" 5]
;                    ["Brazil" "France" 1]
;                    ["Brazil" "Spain" 1]
;                    ["Brazil" "England" 1]
;                    ["Canada" "Portugal" 1]
;                    ["Canada" "France" 5]
;                    ["Canada" "England" 1]
;                    ["Mexico" "Portugal" 1]
;                    ["Mexico" "France" 1]
;                    ["Mexico" "Spain" 5]
;                    ["Mexico" "England" 1]
;                    ["USA" "Portugal" 1]
;                    ["USA" "France" 1]
;                    ["USA" "Spain" 1]
;                    ["USA" "England" 5]
;                    ["Portugal" "Angola" 2]
;                    ["Portugal" "Senegal" 1]
;                    ["Portugal" "Morocco" 1]
;                    ["Portugal" "South Africa" 3]
;                    ["France" "Angola" 1]
;                    ["France" "Senegal" 3]
;                    ["France" "Mali" 3]
;                    ["France" "Morocco" 3]
;                    ["France" "South Africa" 1]
;                    ["Spain" "Senegal" 1]
;                    ["Spain" "Morocco" 3]
;                    ["Spain" "South Africa" 1]
;                    ["England" "Angola" 1]
;                    ["England" "Senegal" 1]
;                    ["England" "Morocco" 2]
;                    ["England" "South Africa" 7]
;                    ["South Africa" "China" 5]
;                    ["South Africa" "India" 1]
;                    ["South Africa" "Japan" 3]
;                    ["Angola" "China" 5]
;                    ["Angola" "India" 1]
;                    ["Angola" "Japan" 3]
;                    ["Senegal" "China" 5]
;                    ["Senegal" "India" 1]
;                    ["Senegal" "Japan" 3]
;                    ["Mali" "China" 5]
;                    ["Mali" "India" 1]
;                    ["Mali" "Japan" 3]
;                    ["Morocco" "China" 5]
;                    ["Morocco" "India" 1]
;                    ["Morocco" "Japan" 3]]

