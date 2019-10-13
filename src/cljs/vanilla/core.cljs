(ns vanilla.core
  (:require [dashboard-clj.core :as d]
            [dashboard-clj.layouts.grid-layout-responsive :as grid]
            [re-frame.core :as rf]
            [vanilla.widgets.simple-text]
            [vanilla.widgets.chart]
            [vanilla.widgets.bar-chart]
            [vanilla.widgets.dual-chart]
            [vanilla.widgets.pie-chart]
            [vanilla.widgets.side-by-side-chart]))


(def widgets [
              {:type        :dual-chart
               :name        :spectrum-dual-widget
               :data-source :spectrum-traces
               :options     {:src {:extract  :spectrum-data
                                   :selector 0
                                   :name     :name
                                   :values   :values
                                   :x-val    :x
                                   :y-val    :y}
                             :viz {:title        "Channels (stacked)"
                                   :chart-title  "dB"
                                   :x-title      "frequency"
                                   :y-title      "power"
                                   :banner-color "lightsalmon"
                                   :line-width   0.5
                                   :animation    false
                                   :style-name   "widget"
                                   :tooltip      {:followPointer true}}}}
                                   ;:debug        true}}}

              {:type        :line-chart
               :name        :spectrum-line-widget
               :data-source :spectrum-traces
               :options     {:src {:extract  :spectrum-data
                                   :selector 0
                                   :name     :name
                                   :values   :values
                                   :x-val    :x
                                   :y-val    :y}
                             :viz {:title        "Channels (line)"
                                   :chart-title  "dB"
                                   :x-title      "frequency"
                                   :y-title      "power"
                                   :banner-color "lightgreen"
                                   :line-width   0.5
                                   :animation    false
                                   :style-name   "widget"
                                   :tooltip      {:followPointer true}
                                   :icon         "timeline"}}}
                                   ;:debug        true}}}

              {:type        :bar-chart
               :name        :spectrum-bar-widget
               :data-source :spectrum-traces
               :options     {:src {:extract  :spectrum-data
                                   :selector 0
                                   :name     :name
                                   :values   :values
                                   :x-val    :x
                                   :y-val    :y}
                             :viz {:title        "Channels (bar)"
                                   :chart-title  "dB"
                                   :x-title      "frequency"
                                   :y-title      "power"
                                   :banner-color "yellow"
                                   :style-name   "widget"
                                   :animation    false
                                   :tooltip      {:followPointer true}}}}
                                   ;:debug        true}}}

              {:type        :simple-text
               :name        :time-widget
               :data-source :current-time
               :options     {:viz {:title        "Current Time"
                                   :banner-color "lightblue"
                                   :style        {:display :flex :align-items :center}
                                   :height       "100px"}}}

              {:type        :pie-chart
               :name        :pie-widget
               :data-source :usage-data
               :options     {:src {:extract  :usage-data
                                   :slice-at 60}
                             :viz {:title        "Usage Data"
                                   :banner-color "goldenrod"
                                   :animation    false}}}
                                   ;:debug        true}}}

              {:type        :side-by-side-chart
               :name        :usage-side-by-side-widget
               :data-source :usage-data
               :options     {:src {:extract  :usage-data
                                   :values   :usage-data
                                   :slice-at 50}
                             :viz {:title        "Usage Data (side-by-side)"
                                   :banner-color "lavender"
                                   :animation    false
                                   :tooltip      {:followPointer true}}}}])
                                   ;:debug        true}}}])





(def widget-layout {
                    :spectrum-line-widget      {:layout-opts {:position {:lg {:x 0 :y 0 :w 4 :h 2}
                                                                         :md {:x 0 :y 0 :w 4 :h 2}
                                                                         :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}
                    :spectrum-bar-widget       {:layout-opts {:position {:lg {:x 0 :y 2 :w 4 :h 2}
                                                                         :md {:x 0 :y 2 :w 4 :h 2}
                                                                         :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}
                    :spectrum-dual-widget      {:layout-opts {:position {:lg {:x 0 :y 4 :w 4 :h 3}
                                                                         :md {:x 0 :y 4 :w 4 :h 3}
                                                                         :sm {:x 0 :y 0 :w 2 :h 3 :static true}}}}
                    :time-widget               {:layout-opts {:position {:lg {:x 4 :y 0 :w 2 :h 1}
                                                                         :md {:x 4 :y 0 :w 2 :h 1}
                                                                         :sm {:x 0 :y 2 :w 2 :h 1 :static true}}}}
                    :usage-side-by-side-widget {:layout-opts {:position {:lg {:x 0 :y 6 :w 4 :h 2}
                                                                         :md {:x 0 :y 6 :w 4 :h 2}
                                                                         :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}
                    :pie-widget                {:layout-opts {:position {:lg {:x 4 :y 2 :w 2 :h 3}
                                                                         :md {:x 4 :y 2 :w 2 :h 3}
                                                                         :sm {:x 0 :y 2 :w 2 :h 3 :static true}}}}})

(def dashboard {
                :layout  :responsive-grid-layout
                :options {:layout-opts {:cols {:lg 6 :md 4 :sm 2 :xs 1 :xxs 1}}}
                :widgets (mapv #(merge % (get widget-layout (:name %))) widgets)})


;(defn home-page []
;  (let [new-layout (layout/setup-layout (get dashboard :layout) dashboard)]
;    [:div.container>div.content
;     [:h2 "Welcome!"]]))





;(defn start-dashboard[]
;  (rf/dispatch-sync [:initialize])
;  (d/register-global-app-state-subscription)
;  (d/connect-to-data-sources)
;  (r/render home-page (.getElementById js/document "dashboard")))
;
;
;(start-dashboard)

(d/start-dashboard dashboard "dashboard")
