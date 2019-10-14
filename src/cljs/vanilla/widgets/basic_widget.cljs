(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [cljsjs.highcharts]
            ;[cljsjs.jquery]
            [dashboard-clj.widgets.core :as widget-common]))



(defn basic-widget [data options custom-content]
  [:div {:class "chart container"
         :style {:height (get-in options [:viz :height] "100%")
                 :width "100%"}}
   [:div {:class "title-wrapper"}
    [:h3 {:class "title"
          :style {:background-color
                  (get-in options [:viz :banner-color] "lightblue")}}
     (get-in options [:viz :title])]]

   [:div {:class (str (get-in options [:viz :style-name] "widget"))
          :style {:width "100%"
                  :height "80%"
                  :marginRight "50px"
                  :marginTop "5px"
                  :cursor :default
                  :border-style (if (get-in options [:viz :debug] false)
                                  :dotted
                                  :none)
                  :align-items :stretch
                  :display :flex}
          :on-mouse-down #(.stopPropagation %)}

    custom-content]])

