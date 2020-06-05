(ns vanilla.widgets.table-widget
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]))


(defn make-widget [name data options]

  ;(prn "table-widget" name
  ;     " //// data " data
  ;     " //// (options) " options)

  (let [header (get-in data [:data :meta-data])
        body   (get-in data [:data :series])]

    ;(prn "header " header
    ;     " //// body " body)s

    [:div.table-container {:style {:width "100%" :height "100%"}}
     [:table.table.is-hoverable {:style {:width "100%" :height "100%"}}
      [:thead
       [:tr
        (for [[idx h] (map-indexed vector header)]
          ^{:key idx} [:th (str (:name h))])]]

      [:tbody
       (doall
         (for [[idx b] (map-indexed vector body)]
           ^{:key idx}
           [:tr
            (for [[k v] b]
              (do
                ^{:key k} [:td (str v)]))]))]]]))