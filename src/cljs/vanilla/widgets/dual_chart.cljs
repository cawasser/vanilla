(ns vanilla.widgets.dual-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]
            [vanilla.widgets.chart :as line]
            [vanilla.widgets.column-chart :as column]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.widget-base :as wb]))



;(widget-common/register-widget
;  :dual-chart
;  (fn [data options]
;
;    (.log js/console (str "dual-chart " data ", " options))
;
;    [basic/basic-widget data options
;
;     [:div
;      ;[:div#line {:style {:width "95%" :height "65%"}}
;      ; (mc/embed-chart (wb/line-chart-config options) (util/line->bar data options) options)]
;
;      [:div#bar {:style {:width "95%" :height "65%"}}
;       (mc/embed-chart (wb/column-chart-config options)
;        (util/line->bar data options) options)]]]))
;

