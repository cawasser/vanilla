(ns vanilla.widgets.simple-text
    (:require [reagent.core :as r :refer [atom]]
              [dashboard-clj.widgets.core :as widget-common]))


(widget-common/register-widget
 :simple-text
 (fn [data options]
   [:div
    [:div {:class "title-wrapper"}
     [:h3 {:class "title"} (get-in data [:data :title])]]
    [:div {:class "simple-text-widget"}
     [:div {:class "data"}
      [:p
       (get-in data [:data :text])]]]]))
