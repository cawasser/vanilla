(ns vanilla.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [dashboard-clj.core :as d]
    [dashboard-clj.layouts.grid-layout-responsive :as grid]
    [vanilla.widgets.widget-base :as wb]
    [vanilla.widgets.simple-text]))


(def widgets [
              ;{:name        :spectrum-area-widget
              ; :type        :area-chart
              ; :name        :spectrum-area-widget
              ; :data-source :spectrum-traces
              ; :options     {:viz/title             "Channels (area)"
              ;               :viz/chart-title       "dB"
              ;               :viz/allowDecimals     "false"
              ;               :viz/x-title           "frequency"
              ;               :viz/y-title           "power"
              ;               :viz/banner-color      "blue"
              ;               :viz/banner-text-color "white"
              ;               :viz/style-name        "widget"
              ;               :viz/animation         false
              ;               :viz/tooltip           {:followPointer true}}}
              ;
              ;{:name        :bubble-widget
              ; :type        :bubble-chart
              ; :data-source :bubble-service
              ; :options     {:viz/title             "Bubble"
              ;               :viz/banner-color      "darkgreen"
              ;               :viz/banner-text-color "white"
              ;               :viz/animation         false
              ;               :viz/data-labels       true}}
              ;
              ;{:name        :spectrum-column-widget
              ; :type        :column-chart
              ; :data-source :spectrum-traces
              ; :options     {:viz/title        "Channels (bar)"
              ;               :viz/banner-color "yellow"
              ;               :viz/line-width   0.5
              ;               :viz/animation    false
              ;               :viz/style-name   "widget"
              ;               :viz/tooltip      {:followPointer true}
              ;               :viz/icon         "timeline"}}
              ;
              {:name        :spectrum-line-widget
               :type        :line-chart
               :data-source :spectrum-traces
               :options     {:viz/title        "Channels (line)"
                             :viz/banner-color "lightgreen"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}])

              ;{:name        :network-widget
              ; :type        :network-chart
              ; :data-source :network-service
              ; :options     {:viz/title             "Network"
              ;               :viz/banner-color      "black"
              ;               :viz/banner-text-color "white"
              ;               :viz/animation         false
              ;               :viz/data-labels       true}}
              ;
              ;{:name        :org-widget
              ; :type        :org-chart
              ; :data-source :network-service
              ; :options     {:viz/title        "Network"
              ;               :viz/banner-color "darkgray"
              ;               :viz/animation    false
              ;               :viz/data-labels  true}}
              ;
              ;{:name        :pie-widget
              ; :type        :pie-chart
              ; :data-source :usage-data
              ; :options     {:viz/title        "Usage Data"
              ;               :viz/banner-color "goldenrod"
              ;               :viz/animation    false
              ;               :viz/slice-at 60}}
              ;
              ;{:name        :sankey-widget
              ; :type        :sankey-chart
              ; :data-source :sankey-service
              ; :options     {:viz/title             "Sankey"
              ;               :viz/banner-color      "darkmagenta"
              ;               :viz/banner-text-color "white"
              ;               :viz/animation         false}}
              ;
              ;{:name        :time-widget
              ; :type        :simple-text
              ; :data-source :current-time
              ; :options     {:viz/title        "Current Time"
              ;               :viz/banner-color "lightblue"
              ;               :viz/style        {}
              ;               :viz/height       "100px"}}])

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
   [:h7.subtitle.is-6 "0.1.4-SNAPSHOT (static)"]])


(defn build-widget [{:keys [name type]}]
  (.log js/console (str "building widget " name " of " type))

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
