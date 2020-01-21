(ns vanilla.widgets.table-widget
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]))


(defn make-widget [name data options]

  ;(prn ":grid make-widget" name
  ;     " //// data " data
  ;     " //// (options) " options)

  ( let [header (get (get data :data) :meta-data)
         body (get (get data :data) :series)]

    ;(prn "header " header
    ;     " //// body " body)

    [:div.container
      [:table-container
       [:table.is-hoverable {:style {:width          "100%"
                                     :border-spacing "15px"
                                     :table-layout   :fixed}}
        [:thead
         [:tr
          (for [h header]
            ^{:key (str (get h :name))} [:th (str (get h :name))])]]


        [:tbody
         (for [b body]
           [:tr
             (for [[k v] b]
               (do
                 ^{:key (str v)}[:td (str v)]))])]]]]))







