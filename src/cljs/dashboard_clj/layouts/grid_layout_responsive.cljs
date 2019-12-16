(ns dashboard-clj.layouts.grid-layout-responsive
  (:require [cljsjs.react-grid-layout]
            [dashboard-clj.layouts.core :as layout-common]
            [dashboard-clj.widgets.core :as widget-common]
            [reagent.core :refer [atom] :as r]))



(def responsive-grid-layout (.-Responsive js/ReactGridLayout))
(def width-provider (.-WidthProvider js/ReactGridLayout))
(def responsive-grid-layout-adapter (r/adapt-react-class (width-provider. responsive-grid-layout)))


(defn calculate-layout [widgets]
  (let [ret-val (into {}
                  (for [brp   [:lg :md, :sm :xs :xxs]
                        :let  [brp-pos
                               (into [] (for [widget widgets
                                              :let [pos (get-in widget [:layout-opts :position brp])]
                                              :when (not (nil? pos))]
                                          (merge pos {:i (:type widget)})))]
                        :when (not-empty brp-pos)]
                    {brp brp-pos}))]

    (.log js/console (str "calculate-layout (responsive) " widgets
                       " //// ret-val " ret-val))

    ret-val))


(def default-layout-opts {:className "layout"})


(defn widget-wrapper[w]
  [:div {:key (:name w) :class "widget"}
   (widget-common/setup-widget w)])


(layout-common/register-layout
 :responsive-grid-layout
 (fn [widgets options]

   ;(.log js/console (str ":responsive-grid-layout"))

   [responsive-grid-layout-adapter
    (merge default-layout-opts (:layout-opts options) {:layouts (calculate-layout widgets)})
    (doall (for [widget  widgets]
             (widget-wrapper widget)))]))



;calculate-layout (responsive) [{:name :bubble-widget,
;                                :basis :chart,
;                                :type :bubble-chart,
;                                :data-source :bubble-service,
;                                :options {:viz/title "Bubble",
;                                          :viz/banner-color "darkgreen",
;                                          :viz/banner-text-color "white",
;                                          :viz/dataLabels true,
;                                          :viz/labelFormat "{point.name}",
;                                          :viz/lineWidth 0,
;                                          :viz/animation false,
;                                          :viz/data-labels true},
;                                :layout-opts {:position {:lg {:x 0, :y 0, :w 4, :h 4},
;                                                         :md {:x 0, :y 0, :w 4, :h 4},
;                                                         :sm {:x 0, :y 0, :w 4, :h 4, :static true}}}}]
;//// ret-val {:lg [{:x 0, :y 0, :w 4, :h 4, :i :bubble-chart}],
;              :md [{:x 0, :y 0, :w 4, :h 4, :i :bubble-chart}],
;              :sm [{:x 0, :y 0, :w 4, :h 4, :static true, :i :bubble-chart}]}

