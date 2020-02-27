(ns vanilla.widgets.simple-text
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]))


(defn make-widget [widget source options]

  ;(prn ":simple-text " data
  ;    " //// options " options)
  (let [data (rf/subscribe [:app-db source])]
   [:div {:style {:width "100%"
                        :text-align :center
                        :border-style  (basic/debug-style options)}}
    [:p {:style {:fontSize    "50px"
                       :font-weight "bold"
                       :color       "blue"}}

     (get-in @data [:data :text])]]))
