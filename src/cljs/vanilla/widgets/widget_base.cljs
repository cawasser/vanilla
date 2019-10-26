(ns vanilla.widgets.widget-base
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.util :as util]))


;;;;;;;;;;;;;;;;;
;
; One structure to specify each highcharts "type" along with a few
; small config things
;
;
(def chart-configs
  {
   :bar-chart {:chart/supported-formats [:data-format/y :data-format/x-y]
               :chart {:type     "bar"
                       :zoomType "x"}
               :yAxis {:min    0
                       :title  {:align "high"}
                       :labels {:overflow "justify"}}}

   :column-chart {:chart/supported-formats [:data-format/y :data-format/x-y]
                  :chart {:type "column"
                          :zoomType "x"}
                  :yAxis {:min    0
                          :title  {:align "high"}
                          :labels {:overflow "justify"}}}

   :line-chart {:chart/supported-formats [:data-format/y :data-format/x-y]
                :chart {:type     "line"
                        :zoomType "x"}
                :yAxis {:min    0
                        :title  {:align "high"}
                        :labels {:overflow "justify"}}}

   :pie-chart {:chart/supported-formats [:data-format/x-y]
               :chart {:type "pie"}}})




(defn get-config [type]
  (let [config (get chart-configs type {})]

    (.log js/console (str "get-config " type ", " config))

    config))




(defn make-widget [id chart-config]

  (.log js/console (str "make-widget " id ", " chart-config))

  (widget-common/register-widget

    id

    (fn [data options]

      [basic/basic-widget data options
       [:div {:style {:width "95%" :height "100%"}}
        [mc/make-chart chart-config data options]]])))

