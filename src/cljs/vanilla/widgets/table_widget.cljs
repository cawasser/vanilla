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
            (for [{:keys [key]} header]
              [:td (str (get b (keyword key)))])]))]]]))




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

