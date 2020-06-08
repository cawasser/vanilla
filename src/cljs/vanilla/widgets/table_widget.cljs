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
            (for [{:keys [key]} header]
              ^{:key key} [:td (str (get b (keyword key)))])]))]]]))




(comment
  (def data @(re-frame.core/subscribe [:app-db :terminal-list-service]))
  (def body (get-in data [:data :series]))
  (def header (get-in data [:data :meta-data]))

  (for [b body]
    (for [{:keys [key]} header]
      (get b (keyword key))))

  (for [b body
        {:keys [key]} header]
    (get b (keyword key)))

  (for [b body]
    ^{:key (str b)}
    [:tr
     (for [{:keys [key]} header]
       [:td (str (get b (keyword key)))])])


  ())