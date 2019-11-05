(ns vanilla.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [dashboard-clj.core :as d]
    [dashboard-clj.layouts.grid-layout-responsive :as grid]
    [vanilla.widgets.widget-base :as wb]
    [vanilla.widgets.simple-text]
    [vanilla.widgets.stoplight-widget]
    [vanilla.widgets.map]
    [vanilla.widgets.heatmap-chart]))


(def widgets [
              {:name        :spectrum-area-widget
               :basis       :chart
               :type        :area-chart
               :data-source :spectrum-traces
               :options     {:viz/title             "Channels (area)"
                             :viz/allowDecimals     false
                             :viz/x-title           "frequency"
                             :viz/y-title           "power"
                             :viz/banner-color      "blue"
                             :viz/banner-text-color "white"
                             :viz/style-name        "widget"
                             :viz/animation         false
                             :viz/tooltip           {:followPointer true}}}

              {:name        :bubble-widget
               :basis       :chart
               :type        :bubble-chart
               :data-source :bubble-service
               :options     {:viz/title             "Bubble"
                             :viz/banner-color      "darkgreen"
                             :viz/banner-text-color "white"
                             :viz/dataLabels        true
                             :viz/labelFormat       "{point.name}"
                             :viz/lineWidth         0
                             :viz/animation         false
                             :viz/data-labels       true}}

              {:name        :spectrum-column-widget
               :basis       :chart
               :type        :column-chart
               :data-source :spectrum-traces
               :options     {:viz/title        "Channels (column)"
                             :viz/banner-color "yellow"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}

              {:name        :spectrum-rose-widget
               :basis       :chart
               :type        :rose-chart
               :data-source :usage-24-hour-service
               :options     {:viz/title        "Channels (rose)"
                             :viz/banner-color "pink"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}

              {:name    :heatmap-widget
               :basis   :chart
               :type    :heatmap-chart
               ;:data-source :spectrum-traces
               :options {:viz/title        "Heat Map"
                         :viz/banner-color "lightgreen"
                         :viz/line-width   0.5
                         :viz/animation    false
                         :viz/style-name   "widget"
                         :viz/tooltip      {:followPointer true}
                         :viz/icon         "timeline"}}

              {:name        :spectrum-line-widget
               :basis       :chart
               :type        :line-chart
               :data-source :spectrum-traces
               :options     {:viz/title        "Channels (line)"
                             :viz/banner-color "lightgreen"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}

              {:name        :network-widget
               :basis       :chart
               :type        :network-chart
               :data-source :network-service
               :options     {:viz/title             "Network (network)"
                             :viz/banner-color      "black"
                             :viz/banner-text-color "white"
                             :viz/animation         false
                             :viz/data-labels       true}}

              {:name        :org-widget
               :basis       :chart
               :type        :org-chart
               :data-source :network-service
               :options     {:viz/title        "Network (org chart)"
                             :viz/banner-color "darkgray"
                             :viz/animation    false
                             :viz/data-labels  true}}

              {:name        :pie-widget
               :basis       :chart
               :type        :pie-chart
               :data-source :usage-data
               :options     {:viz/title        "Usage Data (pie)"
                             :viz/banner-color "goldenrod"
                             :viz/animation    false
                             :viz/dataLabels   true
                             :viz/labelFormat  "{point.name}"
                             :viz/slice-at     20}}

              {:name        :sankey-widget
               :basis       :chart
               :type        :sankey-chart
               :data-source :sankey-service
               :options     {:viz/title             "Sankey"
                             :viz/banner-color      "darkmagenta"
                             :viz/banner-text-color "white"
                             :viz/dataLabels        true
                             :viz/labelFormat       "{point.name}"
                             :viz/animation         false}}

              {:name        :time-widget
               :basis       :simple
               :type        :simple-text
               :data-source :current-time
               :options     {:viz/title        "Current Time"
                             :viz/banner-color "lightblue"
                             :viz/style        {}
                             :viz/height       "100px"}}

              {:name        :spectrum-dual-widget
               :basis       :stacked-chart
               :type        :line-column-stack
               :chart-types [:line-chart :column-chart]
               :data-source :spectrum-traces
               :options     {:viz/title        "Channels (stacked)"
                             :viz/banner-color "lightsalmon"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}}}

              {:type    :map-container
               :name    :map-widget
               ;:data-source :current-time
               :options {:viz/title        "Map Widget"
                         :viz/banner-color "lightblue"
                         :viz/style        {}
                         :viz/height       "500px"}}

              {:name        :usage-side-by-side-widget
               :basis       :side-by-side-chart
               :type        :bar-pie-sbs
               :chart-types [:bar-chart :pie-chart]
               :data-source :usage-data
               :options     {:viz/title             "Usage Data (side-by-side)"
                             :viz/banner-color      "lavender"
                             :viz/banner-text-color "black"
                             :viz/line-width        0.5
                             :viz/style-name        "widget"
                             :viz/animation         false
                             :viz/dataLabels        true
                             :viz/labelFormat       "{point.name}"
                             :viz/tooltip           {:followPointer true}}}

              {:name        :dependency-widget
               :basis       :chart
               :type        :dependency-chart
               :data-source :sankey-service
               :options     {:viz/title             "Dependency Wheel"
                             :viz/banner-color      "crimson"
                             :viz/banner-text-color "white"
                             :viz/dataLabels        true
                             :viz/animation         false}}

              {:type        :stoplight-widget
               :basis       :simple
               :name        :health-and-status-widget
               :data-source :health-and-status-data
               :options     {:viz/title        "Status"
                             :viz/banner-color "aqua"}}])


(def widget-layout
  {
   ;; Left column wide widgets
   :network-widget            {:layout-opts
                               {:position {:lg {:x 0 :y 0 :w 4 :h 4}
                                           :md {:x 0 :y 0 :w 4 :h 4}
                                           :sm {:x 0 :y 0 :w 4 :h 4 :static true}}}}
   :org-widget                {:layout-opts
                               {:position {:lg {:x 0 :y 4 :w 4 :h 4}
                                           :md {:x 0 :y 4 :w 4 :h 4}
                                           :sm {:x 0 :y 4 :w 4 :h 4 :static true}}}}
   :spectrum-dual-widget      {:layout-opts
                               {:position {:lg {:x 0 :y 8 :w 4 :h 3}
                                           :md {:x 0 :y 8 :w 4 :h 3}
                                           :sm {:x 0 :y 8 :w 2 :h 3 :static true}}}}
   :sankey-widget             {:layout-opts
                               {:position {:lg {:x 0 :y 11 :w 3 :h 3}
                                           :md {:x 0 :y 11 :w 3 :h 3}
                                           :sm {:x 0 :y 11 :w 3 :h 3 :static true}}}}
   :usage-side-by-side-widget {:layout-opts
                               {:position {:lg {:x 0 :y 14 :w 4 :h 2}
                                           :md {:x 0 :y 14 :w 4 :h 2}
                                           :sm {:x 0 :y 14 :w 2 :h 2 :static true}}}}
   :spectrum-area-widget      {:layout-opts
                               {:position {:lg {:x 0 :y 16 :w 4 :h 2}
                                           :md {:x 0 :y 16 :w 4 :h 2}
                                           :sm {:x 0 :y 16 :w 2 :h 2 :static true}}}}
   :heatmap-widget
                              {:layout-opts
                               {:position {:lg {:x 0 :y 18 :w 4 :h 4}
                                           :md {:x 0 :y 18 :w 4 :h 4}
                                           :sm {:x 0 :y 18 :w 4 :h 4 :static true}}}}
   :map-widget                {:layout-opts
                               {:position {:lg {:x 4 :y 5 :w 2 :h 3}
                                           :md {:x 4 :y 5 :w 2 :h 3}
                                           :sm {:x 0 :y 5 :w 2 :h 3 :static true}}}}

   ;; Right column small widgets
   :time-widget               {:layout-opts
                               {:position {:lg {:x 4 :y 0 :w 2 :h 1}
                                           :md {:x 4 :y 0 :w 2 :h 1}
                                           :sm {:x 0 :y 0 :w 2 :h 1 :static true}}}}
   :health-and-status-widget  {:layout-opts
                               {:position {:lg {:x 4 :y 1 :w 2 :h 2}
                                           :md {:x 4 :y 1 :w 2 :h 2}
                                           :sm {:x 0 :y 1 :w 2 :h 2 :static true}}}}
   :pie-widget                {:layout-opts
                               {:position {:lg {:x 4 :y 3 :w 2 :h 3}
                                           :md {:x 4 :y 3 :w 2 :h 3}
                                           :sm {:x 0 :y 3 :w 2 :h 3 :static true}}}}
   :bubble-widget             {:layout-opts
                               {:position {:lg {:x 4 :y 14 :w 2 :h 3}
                                           :md {:x 4 :y 14 :w 2 :h 3}
                                           :sm {:x 0 :y 14 :w 2 :h 3 :static true}}}}
   :spectrum-line-widget      {:layout-opts
                               {:position {:lg {:x 4 :y 9 :w 2 :h 2}
                                           :md {:x 4 :y 9 :w 2 :h 2}
                                           :sm {:x 0 :y 9 :w 2 :h 2 :static true}}}}
   :dependency-widget {:layout-opts
                       {:position {:lg {:x 3 :y 11 :w 3 :h 3}
                                   :md {:x 3 :y 11 :w 3 :h 3}
                                   :sm {:x 3 :y 11 :w 3 :h 3 :static true}}}}
   :spectrum-column-widget {:layout-opts
                            {:position {:lg {:x 4 :y 16 :w 2 :h 2}
                                        :md {:x 4 :y 16 :w 2 :h 2}
                                        :sm {:x 0 :y 16 :w 2 :h 2 :static true}}}}
   :spectrum-rose-widget {:layout-opts
                          {:position {:lg {:x 4 :y 18 :w 2 :h 3}
                                      :md {:x 4 :y 18 :w 2 :h 3}
                                      :sm {:x 0 :y 18 :w 2 :h 3 :static true}}}}})



(def dashboard {
                :layout  :responsive-grid-layout
                :options {:layout-opts {:cols {:lg 6 :md 4 :sm 2 :xs 1 :xxs 1}}}
                :widgets (mapv #(merge % (get widget-layout (:name %))) widgets)})


(defn home-page []
  [:div.container>div.content
   [:h7.subtitle.is-6 "0.1.7-SNAPSHOT (static)"]])


(defn build-widget [{:keys [name basis type chart-types]}]
  ;(.log js/console (str "building widget " name " of " type
  ;                      " //// " basis "/" chart-types))

  (condp = basis
    :chart (wb/make-widget type (wb/get-config type))

    :stacked-chart (do
                     ;(.log js/console (str "calling make-stacked-widget " type
                     ;                      "/" chart-types))
                     (wb/make-stacked-widget type chart-types))

    :side-by-side-chart (do
                          ;(.log js/console (str "calling make-side-by-side-widget " type
                          ;                      "/" chart-types))
                          (wb/make-side-by-side-widget type chart-types))

    ()))                                                    ; default


(defn start-dashboard []
  (rf/dispatch-sync [:initialize])

  ; build all the required widgets

  ;(.log js/console (str "building widgets " widgets))
  (doall (map build-widget widgets))

  (d/register-global-app-state-subscription)
  (d/connect-to-data-sources)
  (r/render home-page (.getElementById js/document "app"))
  (d/start-dashboard dashboard "dashboard"))


(start-dashboard)
