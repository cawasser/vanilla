(ns vanilla.widgets.stoplight-widget
  (:require [reagent.core :as r :refer [atom]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]))


(defn- stoplight [id val]

  (.log js/console (str "stoplight " id ", " val))

  (let [c (condp = val
            :up "green"
            :warning "yellow"
            :fault "red")]
    ^{:key id}
    [:td.has-text-centered {:style {:background-color c
                                    :width "20%"
                                    :border-spacing "5px"}}
     id]))


(defn- stoplight-row [data]

  (.log js/console (str "stoplight-row " data))

  ^{:key (str data)}
  [:tr
   (for [[k v] data]
     (doall

       (.log js/console (str "stoplight-row> " k "-" v))

       (stoplight k v)))])



(widget-common/register-widget
  :stoplight-widget
  (fn [data options]

    (.log js/console (str ":stoplight-widget " data ", " options))

    [basic/basic-widget data options

     [:div.container
      [:table-container
       [:table.is-hoverable {:style {:width          "100%"
                                     :border-spacing "15px"
                                     :table-layout   :fixed}}
        [:tbody
         (doall
           (for [d (partition-all 5 (get-in data [:data
                                                  (get-in options [:src :extract]
                                                          :data)]))]
             (stoplight-row d)))]]]]]))
