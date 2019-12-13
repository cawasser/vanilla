(ns dashboard-clj.layouts.grid-layout
  (:require [cljsjs.react-grid-layout]
            [dashboard-clj.layouts.core :as layout-common]
            [dashboard-clj.widgets.core :as widget-common]
            [reagent.core :refer [atom] :as r]))


(def layout-defaults
  {
   :className "layout"
   :cols 4
   :rowHeight 300
   :width 1200})



(def grid-layout-adapter (r/adapt-react-class js/ReactGridLayout))



(defn widget-wrapper[w]
  [:div {:key (:name w) :_grid (get-in w [:layout-opts :position]) :class "widget" :style (:style w)}
   (widget-common/setup-widget w)])




(layout-common/register-layout
 :grid-layout
 (fn [widgets options]

   ;(.log js/console (str ":grid-layout"))

   [grid-layout-adapter (merge layout-defaults (:layout-opts options))
    (doall (for [widget  widgets]
             (widget-wrapper widget)))]))
