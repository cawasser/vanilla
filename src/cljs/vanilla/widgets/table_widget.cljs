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

  (def data
    {:data {:title "Data Table",
            :data-format :data-format/entities,
            :meta-data [{:key "name", :name "Name", :editable true}
                        {:key "country", :name "Country", :editable true}
                        {:key "email", :name "Email", :editable true}
                        {:key "operating-since", :name "Operating Since", :editable true}],
            :series [{:name "user one",
                      :country "USA",
                      :email "user_one@none.com",
                      :operating-since "2000"}
                     {:name "user two", :country "USA", :email "user_two@none.com", :operating-since "2001"}
                     {:name "user three", :country "USA", :email "user_three@none.com", :operating-since "2002"}]}

    :options {:viz/title "Table", :viz/banner-color {:r 0, :g 255, :b 255, :a 1}}})


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