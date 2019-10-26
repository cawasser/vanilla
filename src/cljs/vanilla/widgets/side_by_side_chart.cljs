(ns vanilla.widgets.side-by-side-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]
            [vanilla.widgets.pie-chart :as pie]
            [vanilla.widgets.chart :as line]
            [vanilla.widgets.bar-chart :as bar]))


;(widget-common/register-widget
;  :side-by-side-chart
;  (fn [data options]
;
;    ;(.log js/console (str ":side-by-side-chart " data))
;
;    [basic/basic-widget data options
;
;     [:div.columns {:style {:height "100%" :width "100%" :marginTop "10px"}}
;
;      [:div.column.is-two-thirds {:style {:height "200px"}}
;       (let [dats (util/pie->bar data
;                                 options
;                                 (get-in options [:src :slice-at]))]
;         [bar/embed-bar data options (:series dats)])]
;
;      [:div.column.is-one-third {:style {:height "200px"}}
;       [pie/embed-pie data options]]]]))
;

