(ns vanilla.widgets.widget-base
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.make-chart :as mc]

            [vanilla.widgets.area-chart]
            [vanilla.widgets.bar-chart]
            [vanilla.widgets.bubble-chart]
            [vanilla.widgets.column-chart]
            [vanilla.widgets.line-chart]
            [vanilla.widgets.network-graph-chart]
            [vanilla.widgets.org-chart]
            [vanilla.widgets.pie-chart]
            [vanilla.widgets.vari-pie-chart]
            [vanilla.widgets.sankey-chart]
            [vanilla.widgets.scatter-chart]
            [vanilla.widgets.rose-chart]
            [vanilla.widgets.sankey-chart]
            [vanilla.widgets.heatmap-chart]))



(defn get-config [type]
  (let [config (get-in @mc/type-registry [type :chart-options] {})]

    ;(.log js/console (str "get-config " type ", " config))

    config))


(defn make-widget [name id chart-config]

  ;(.log js/console (str "make-widget " id ", " chart-config))

  (widget-common/register-widget

    id

    (fn [data options]

      [basic/basic-widget name data options
       [:div {:style {:width "95%" :height "100%"}}
        [mc/make-chart chart-config data options]]])))


(defn make-stacked-widget [name id [top-chart bottom-chart]]

  ;(.log js/console (str "make-stacked-widget " id
  ;                      "/" top-chart "/" bottom-chart))

  (widget-common/register-widget

    id

    (fn [data options]

      ;(.log js/console (str "composing stacked-chart " id
      ;                      " //// (data)" data
      ;                      " //// (options)" options))

      [basic/basic-widget name data options

       [:div {:style {:width "95%" :height "65%"}}
        [mc/make-chart (get-config top-chart) data options]

        [:div {:style {:width "100%" :height "65%"}}
         [mc/make-chart (get-config bottom-chart) data options]]]])))


(defn make-side-by-side-widget [name id [left-chart right-chart]]

  ;(.log js/console (str "make-side-by-side-widget " id
  ;                      "/" left-chart "/" right-chart))

  (widget-common/register-widget

    id

    (fn [data options]

      ;(.log js/console (str "side-by-side-chart " id "/" left-chart "/" right-chart
      ;                      " //// (data)" data
      ;                      " //// (options)" options))

      [basic/basic-widget name data options

       [:div.columns {:style {:height "100%" :width "100%" :marginTop "10px"}}

        [:div.column.is-two-thirds {:style {:height "80%"}}
         [mc/make-chart (get-config left-chart) data options]]

        [:div.column.is-one-third {:style {:height "80%"}}
         [mc/make-chart (get-config right-chart) data options]]]])))



(defn build-widget [{:keys [name basis type chart-types]}]
  ;(.log js/console (str "building widget " name " of " type
  ;                      " //// " basis " //// " chart-types))

  (condp = basis
    :chart (make-widget name type (get-config type))

    :stacked-chart (do
                     ;(.log js/console (str "calling make-stacked-widget " type
                     ;                      "/" chart-types))
                     (make-stacked-widget name type chart-types))

    :side-by-side-chart (do
                          ;(.log js/console (str "calling make-side-by-side-widget " type
                          ;                      "/" chart-types))
                          (make-side-by-side-widget name type chart-types))

    ()))
