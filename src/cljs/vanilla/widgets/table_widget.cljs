(ns vanilla.widgets.table-widget
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]))


(defn make-widget [name data options]

  ;(prn ":grid make-widget" name
  ;     " //// data " data
  ;     " //// (options) " options)

  (let [header (get-in data [:data :meta-data])
        body   (take 7 (get-in data [:data :series]))]

    ;(prn "header " header
    ;     " //// body " body)

    [:div.container
     [:div.table-container
      [:table.table.dark-mode.is-fullwidth
       {:style {;:width          "100%"
                ;:height         "100%"
                :border-spacing "15px"
                :table-layout   :auto}}

       [:thead
        [:tr
         (for [h header]
           ^{:key (str (get h :name))} [:th {:style {:color "lightgray"}} (str (get h :name))])]]

       [:tbody
        (for [b body]
          [:tr
           (for [[k v] b]
             (do
               ^{:key (str v)} [:td (str v)]))])]]]]))