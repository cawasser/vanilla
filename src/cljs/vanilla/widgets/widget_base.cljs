(ns vanilla.widgets.widget-base
    (:require [reagent.core :as r]
              [reagent.ratom :refer-macros [reaction]]
              [dashboard-clj.widgets.core :as widget-common]
              [vanilla.widgets.basic-widget :as basic]
              [vanilla.widgets.util :as util]))

; DOESNT WORK
(defn render
  []
  [:div {:style {:width "100%" :height "100%"}}])



(defn chart-config [chartType]
    {:chart   {:type            chartType
               :backgroundColor "transparent"
               :style           {:labels {
                                          :fontFamily "monospace"
                                          :color      "#FFFFFF"}}}
     :yAxis   {:title  {:style {:color "#000000"}}
               :labels {:color "#ffffff"}}
     :xAxis   {:labels {:style {:color "#fff"}}}
     :credits {:enabled false}})