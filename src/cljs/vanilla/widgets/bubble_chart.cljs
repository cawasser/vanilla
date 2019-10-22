(ns vanilla.widgets.bubble-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]))


(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])


(def bubble-chart-config
  {:chart   {:type            "bubble"
             :backgroundColor "transparent"}

   :style   {:labels {:fontFamily "monospace"
                      :color      "#FFFFFF"}}
   :credits {:enabled false}})


(defn- plot-bubble [this]
  (let [config     (-> this r/props :chart-options)
        all-config (merge-with clojure.set/union bubble-chart-config config)]

    ;(.log js/console (str "plot-bubble " all-config))

    (js/Highcharts.Chart. (r/dom-node this)
                          (clj->js all-config))))


(defn bubble-chart
  [chart-options]
  (r/create-class {:reagent-render       render
                   :component-did-mount  plot-bubble
                   :component-did-update plot-bubble}))


(defn embed-bubble [data options]
  (let []

    ;(.log js/console (str "embed-bubble " data))

    [bubble-chart
     {:chart-options
      {:title  {:text ""}
       :plotOptions {:series {:dataLabels {:enabled (get-in options [:viz :data-labels] false)
                                           :format "{point.name}"}
                              :animation (get-in options [:viz :animation] false)}}
       :series (get-in data [:data (get-in options [:src :extract])])}}]))


(widget-common/register-widget
  :bubble-chart
  (fn [data options]
    (let []

      [basic/basic-widget data options
       [:div {:style {:width "95%" :height "100%"}}

        [embed-bubble data options]]])))



