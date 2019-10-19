(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]))



(defn debug-style [options]
  (if (get-in options [:viz :debug] false)
    :dotted
    :none))



(defn basic-widget [data options custom-content]
  [:div {:class "chart container"
         :style {:height (get-in options [:viz :height] "100%")
                 :width "100%"}}
   [:div {:class "title-wrapper"}
    [:h3 {:class "title"
          :style {:background-color
                  (get-in options [:viz :banner-color] "lightblue")
                  :color (get-in options [:viz :banner-text-color] "black")}}
     (get-in options [:viz :title])]]

   [:div {:class (str (get-in options [:viz :style-name] "widget"))
          :style {:width "100%"
                  :height "80%"
                  :marginRight "50px"
                  :marginTop "5px"
                  :cursor :default
                  :border-style (debug-style options)
                  :align-items :stretch
                  :display :flex}
          :on-mouse-down #(.stopPropagation %)}

    custom-content]])

