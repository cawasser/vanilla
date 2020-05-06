(ns vanilla.widgets.table-widget
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]))


(defn make-widget [name data options]

  ;(prn ":grid make-widget" name
  ;     " //// data " data
  ;     " //// (options) " options)

  (let [header (get-in data [:data :meta-data])
        body   (get-in data [:data :series])]

    ;(prn "header " header
    ;     " //// body " body)

    [:div.table-container
     [:table.table.is-hoverable
      [:thead
       [:tr
        (for [h header]
          ^{:key (str (:name h))} [:th (str (:name h))])]]

      [:tbody
       (doall
         (for [b body]
           ^{:key (str b)}
           [:tr
            (for [[k v] b]
              (do
                [:td (str v)]))]))]]]))