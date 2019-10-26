(ns vanilla.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]

    [dashboard-clj.core :as d]
    [dashboard-clj.layouts.grid-layout-responsive :as grid]

    [vanilla.widgets.widget-base :as wb]

    [vanilla.widgets.simple-text]
    [vanilla.widgets.chart]
    [vanilla.widgets.area-chart]
    [vanilla.widgets.bar-chart]
    [vanilla.widgets.bubble-chart]
    [vanilla.widgets.column-chart]
    [vanilla.widgets.dual-chart]
    [vanilla.widgets.network-graph-chart]
    [vanilla.widgets.org-chart]
    [vanilla.widgets.pie-chart]
    [vanilla.widgets.sankey-chart]
    [vanilla.widgets.side-by-side-chart]))


(def widgets [
              ;{:type        :area-chart
              ; :name        :spectrum-area-widget
              ; :data-source :spectrum-traces
              ; :options     {:src {:extract  :spectrum-data
              ;                     :selector 0
              ;                     :name     :name
              ;                     :values   :values
              ;                     :x-val    :x
              ;                     :y-val    :y}
              ;               :viz {:title             "Channels (area)"
              ;                     :chart-title       "dB"
              ;                     :allowDecimals     "false"
              ;                     :x-title           "frequency"
              ;                     :y-title           "power"
              ;                     :banner-color      "blue"
              ;                     :banner-text-color "white"
              ;                     :style-name        "widget"
              ;                     :animation         false
              ;                     :tooltip           {:followPointer true}}}}
              ;
              ; {:type        :dual-chart
              ;  :name        :spectrum-dual-widget
              ;  :data-source :spectrum-traces
              ;  :options     {:src {:extract  :spectrum-data
              ;                      :selector 0
              ;                      :name     :name
              ;                      :values   :values
              ;                      :x-val    :x
              ;                      :y-val    :y}
              ;                :viz {:title        "Channels (stacked)"
              ;                      :chart-title  "dB"
              ;                      :x-title      "frequency"
              ;                      :y-title      "power"
              ;                      :banner-color "lightsalmon"
              ;                      :line-width   0.5
              ;                      :animation    false
              ;                      :style-name   "widget"
              ;                      :tooltip      {:followPointer true}}}}

              {:type        :column-chart
               :name        :spectrum-column-widget
               :data-source :spectrum-traces
               :options     {:viz/title        "Channels (bar)"
                             :viz/banner-color "yellow"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}

              {:type        :line-chart
               :name        :spectrum-line-widget
               :data-source :spectrum-traces
               :options     {:viz/title        "Channels (line)"
                             :viz/banner-color "lightgreen"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}])

;   {:type        :simple-text
;    :name        :time-widget
;    :data-source :current-time
;    :options     {:viz {:title        "Current Time"
;                        :banner-color "lightblue"
;                        :style        {}
;                        :height       "100px"}}}

;   {:type        :pie-chart
;    :name        :pie-widget
;    :data-source :usage-data
;    :options     {:src {:extract  :usage-data
;                        :slice-at 60}
;                  :viz {:title        "Usage Data"
;                        :banner-color "goldenrod"
;                        :animation    false}}}

;{:type        :side-by-side-chart
; :name        :usage-side-by-side-widget
; :data-source :usage-data
; :options     {:src {:extract  :spectrum-data
;                     :selector 0
;                     :name     :name
;                     :values   :values
;                     :x-val    :x
;                     :y-val    :y}
;               :viz {:title             "Channels (side-by-side)"
;                     :chart-title       "dB"
;                     :allowDecimals     "false"
;                     :x-title           "frequency"
;                     :y-title           "power"
;                     :banner-color      "lavender"
;                     :banner-text-color "white"
;                     :line-width        0.5
;                     :icon              "timeline"
;                     :style-name        "widget"
;                     :animation         false
;                     :tooltip           {:followPointer true}}}}])
;
;
;
;   {:type        :sankey-chart
;    :name        :sankey-widget
;    :data-source :sankey-service
;    :options     {:src {:extract :data
;                        :keys    :keys}
;                  :viz {:title             "Sankey"
;                        :banner-color      "darkmagenta"
;                        :banner-text-color "white"
;                        :animation         false}}}

;   {:type        :bubble-chart
;    :name        :bubble-widget
;    :data-source :bubble-service
;    :options     {:src {:extract :data}
;                  :viz {:title             "Bubble"
;                        :banner-color      "darkgreen"
;                        :banner-text-color "white"
;                        :animation         false
;                        :data-labels       true}}}

;   {:type        :org-chart
;    :name        :org-widget
;    :data-source :network-service
;    :options     {:src {:extract :data}
;                  :viz {:title        "Network"
;                        :banner-color "darkgray"
;                        :animation    false
;                        :data-labels  true}}}

;   {:type        :network-graph-chart
;    :name        :network-widget
;    :data-source :network-service
;    :options     {:src {:extract :data}
;                  :viz {:title             "Network"
;                        :banner-color      "black"
;                        :banner-text-color "white"
;                        :animation         false
;                        :data-labels       true}}}])


(def widget-layout
  {
   ;; Left column wide widgets
   :spectrum-dual-widget      {:layout-opts
                               {:position {:lg {:x 0 :y 4 :w 4 :h 3}
                                           :md {:x 0 :y 4 :w 4 :h 3}
                                           :sm {:x 0 :y 0 :w 2 :h 3 :static true}}}}
   :usage-side-by-side-widget {:layout-opts
                               {:position {:lg {:x 0 :y 6 :w 4 :h 2}
                                           :md {:x 0 :y 6 :w 4 :h 2}
                                           :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}
   :spectrum-area-widget      {:layout-opts
                               {:position {:lg {:x 0 :y 8 :w 4 :h 2}
                                           :md {:x 0 :y 8 :w 4 :h 2}
                                           :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}
   :network-widget            {:layout-opts
                               {:position {:lg {:x 0 :y 0 :w 4 :h 4}
                                           :md {:x 0 :y 0 :w 4 :h 4}
                                           :sm {:x 0 :y 0 :w 4 :h 4 :static true}}}}
   :org-widget                {:layout-opts
                               {:position {:lg {:x 0 :y 0 :w 4 :h 4}
                                           :md {:x 0 :y 0 :w 4 :h 4}
                                           :sm {:x 0 :y 0 :w 4 :h 4 :static true}}}}
   :sankey-widget             {:layout-opts
                               {:position {:lg {:x 0 :y 5 :w 4 :h 3}
                                           :md {:x 0 :y 5 :w 4 :h 3}
                                           :sm {:x 0 :y 5 :w 4 :h 3 :static true}}}}

   :bubble-widget             {:layout-opts
                               {:position {:lg {:x 4 :y 5 :w 2 :h 3}
                                           :md {:x 4 :y 5 :w 2 :h 3}
                                           :sm {:x 0 :y 5 :w 2 :h 3 :static true}}}}

   ;; Right column small widgets
   :time-widget               {:layout-opts
                               {:position {:lg {:x 4 :y 0 :w 2 :h 1}
                                           :md {:x 4 :y 0 :w 2 :h 1}
                                           :sm {:x 0 :y 2 :w 2 :h 1 :static true}}}}
   :pie-widget                {:layout-opts
                               {:position {:lg {:x 4 :y 2 :w 2 :h 3}
                                           :md {:x 4 :y 2 :w 2 :h 3}
                                           :sm {:x 0 :y 2 :w 2 :h 3 :static true}}}}
   :spectrum-line-widget      {:layout-opts
                               {:position {:lg {:x 4 :y 5 :w 2 :h 2}
                                           :md {:x 4 :y 5 :w 2 :h 2}
                                           :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}

   :spectrum-column-widget    {:layout-opts
                               {:position {:lg {:x 4 :y 7 :w 2 :h 2}
                                           :md {:x 4 :y 7 :w 2 :h 2}
                                           :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}})


(def dashboard {
                :layout  :responsive-grid-layout
                :options {:layout-opts {:cols {:lg 6 :md 4 :sm 2 :xs 1 :xxs 1}}}
                :widgets (mapv #(merge % (get widget-layout (:name %))) widgets)})


(defn home-page []
  [:div.container>div.content
   [:h7.subtitle.is-6 "v0.1.3-SNAPSHOT (static)"]])


(defn build-widget [{:keys [name type]}]
  (wb/make-widget type (wb/get-config type)))


(defn start-dashboard []
  (rf/dispatch-sync [:initialize])

  ; build all the required widgets

  (.log js/console (str "building widgets " widgets))
  (doall (map build-widget widgets))

  (d/register-global-app-state-subscription)
  (d/connect-to-data-sources)
  (r/render home-page (.getElementById js/document "app"))
  (d/start-dashboard dashboard "dashboard"))


(start-dashboard)
