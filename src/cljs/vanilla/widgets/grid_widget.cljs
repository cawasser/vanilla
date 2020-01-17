(ns vanilla.widgets.grid-widget
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]))



(defn make-widget [name data options]

  (prn ":grid make-widget" name
       " //// data " data
       " //// (options) " options)

  ( let [header (:meta-data data)
         body (:series data)]

    [:table-container
     [:table.is-hoverable {:style {:width          "100%"
                                   :border-spacing "15px"
                                   :table-layout   :fixed}}
      [:theader]

      [:tbody]]]))

