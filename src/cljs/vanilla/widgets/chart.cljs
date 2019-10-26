(ns vanilla.widgets.chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.widget-base :as wb]
            [vanilla.widgets.make-chart :as mc]))


         ;:series      (into []
         ;                   (for [n (range num)]
         ;                     {:name (get-in dats [n (get-in options [:src :name] :name)] (str "set " n))
         ;                      :data (into [] (get-in dats [n (get-in options [:src :values] :values)]))}))}}])))


