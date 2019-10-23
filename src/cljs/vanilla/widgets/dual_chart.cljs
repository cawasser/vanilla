(ns vanilla.widgets.dual-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]
            [vanilla.widgets.chart :as line]
            [vanilla.widgets.column-chart :as column]
            [vanilla.widgets.make-chart :as mc]))


(defn spectrum-data []
  [{:name   "trace-1"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}
   {:name   "trace-2"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}
   {:name   "trace-3"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}])



(widget-common/register-widget
  :dual-chart
  (fn [data options]
    (.log js/console (str "SIDEBYSIDE " data))
    [basic/basic-widget data options

     [:div {:style {:width "95%" :height "65%"}}
      [mc/embed-chart "area-chart" {:data {:spectrum-data (spectrum-data)}} options (util/line->bar {:data {:spectrum-data (spectrum-data)}} options)]

      [:div {:style {:width "95%" :height "65%"}}
       [mc/embed-chart "line-chart" {:data {:spectrum-data (spectrum-data)}} options (util/line->bar {:data {:spectrum-data (spectrum-data)}} options)]]]]))


