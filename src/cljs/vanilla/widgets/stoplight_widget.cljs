(ns vanilla.widgets.stoplight-widget
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]))


(defn- stoplight [id val]

  ; (prn "stoplight " id ", " val)

  (let [c (condp = val
            :up "green"
            :warning "yellow"
            :fault "red")]
    ^{:key id}
    [:button.button
     {:style    {:background-color c
                 :width            "20%"
                 :border-spacing   "5px"}
      :on-click #(.log js/console (str "clicked " id))}
     id]))


(defn- stoplight-row [data]

  ;(prn "stoplight-row " data)

  ^{:key (str data)}
  [:buttons
   (for [[k v] data]
     (doall

       ;(prn "stoplight-row> " k "-" v)

       (stoplight k v)))])



(defn make-widget [name source options]

  ;(prn ":stoplight make-widget" name
  ;  " //// data " data
  ;  " //// (options) " options)
  (let [data (rf/subscribe [:app-db source])]
    [:table-container
     [:table.is-hoverable {:style {:width          "100%"
                                   :border-spacing "15px"
                                   :table-layout   :fixed}}
      [:tbody
       (doall
         (for [d (partition-all 5 (get-in @data [:data :series]))]
           (stoplight-row d)))]]]))
