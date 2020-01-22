(ns vanilla.widgets.simple-text
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]))


(defn make-widget [name data options]

  ;(.log js/console ":simple-text" (str data) (str options))

   [:div {:style {:width "100%"
                        :text-align :center
                        :border-style  (basic/debug-style options)}}
    [:p {:style {:fontSize    "50px"
                       :font-weight "bold"
                       :color       "blue"}}

     (get-in data [:data :text])]])
