(ns vanilla.widgets.map
  (:require [reagent.core :as r :refer [atom]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]))


(widget-common/register-widget
  :map-container
  (fn [data options]
    ;(.log js/console ":simple-text" (str data) (str options))

    [basic/basic-widget data options

     [:div {:style {:width "100%"
                    :text-align :left
                    :border-style  (basic/debug-style options)}}
      [:p {:style {:fontSize    "50px"
                   :font-weight "bold"
                   :color       "blue"}}

       (get-in data [:data :text])]]]))
