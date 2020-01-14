(ns vanilla.widgets.stoplight-widget
  (:require [reagent.core :as r :refer [atom]]
            ;[dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]))


(defn- stoplight [id val]

  ;(.log js/console (str "stoplight " id ", " val))

  (let [c (condp = val
            :up "green"
            :warning "yellow"
            :fault "red")]
    ^{:key id}
    [:button.button {:style {:background-color c
                             :width "20%"
                             :border-spacing "5px"}
                     :on-click #(.log js/console (str "clicked " id))}
     id]))


(defn- stoplight-row [data]

  ;(.log js/console (str "stoplight-row " data))

  ^{:key (str data)}
  [:buttons
   (for [[k v] data]
     (doall

       ;(.log js/console (str "stoplight-row> " k "-" v))

       (stoplight k v)))])

(defn register-type []
  (widget-common/register-widget
    :stoplight-widget
    (fn [data options]

      (.log js/console (str ":stoplight-widget register-type " data ", " options))

      [basic/basic-widget data options

       [:div.container
        [:table-container
         [:table.is-hoverable {:style {:width          "100%"
                                       :border-spacing "15px"
                                       :table-layout   :fixed}}
          [:tbody
           (doall
             (for [d (partition-all 5 (get-in data [:data :series]))]
               (stoplight-row d)))]]]]])))
