(ns vanilla.widgets.simple-text
    (:require [reagent.core :as r :refer [atom]]
              [dashboard-clj.widgets.core :as widget-common]
              [vanilla.widgets.basic-widget :as basic]))


(widget-common/register-widget
 :simple-text
 (fn [data options]
   ;(.log js/console ":simple-text" (str data) (str options))

   [basic/basic-widget data options

    [:div {:style {:width "100%"
                   :display :flex :align-items :center}}    ;(get-in options [:viz :style] {})}
     [:p {:style {:fontSize    "50px"
                  :font-weight "bold"
                  :color       "blue"}}

      (get-in data [:data :text])]]]))
