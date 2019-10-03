(ns vanilla.core
  (:require [dashboard-clj.core :as d]
            [dashboard-clj.layouts.grid-layout-responsive :as grid]
            [vanilla.widgets.simple-text]
            [vanilla.widgets.chart]
            [vanilla.widgets.bar-chart]
            [re-frame.core :as rf]))

(def widgets [
              {:type        :line-chart
               :name        :spectrum-line-widget
               :data-source :spectrum-traces
               :options     {:src {:extract  :spectrum-data
                                   :selector 0
                                   :name     :nil
                                   :values   :values
                                   :x-val    :x
                                   :y-val    :y}
                             :viz {:title        "Channels"
                                   :chart-title  "dB"
                                   :x-title      "frequency"
                                   :y-title      "power"
                                   :banner-color "lightgreen"
                                   :line-color   "red"
                                   :line-width   0.5
                                   :animation    false
                                   :style-name   "widget"
                                   :height       "500px"
                                   :tooltip      {:followPointer true}}}}


              {:type        :bar-chart
               :name        :spectrum-bar-widget
               :data-source :spectrum-traces
               :options     {:src {:extract  :spectrum-data
                                   :selector 0
                                   :name     :nil
                                   :values   :values
                                   :x-val    :x
                                   :y-val    :y}
                             :viz {:title        "Channels"
                                   :chart-title  "dB"
                                   :x-title      "frequency"
                                   :y-title      "power"
                                   :banner-color "yellow"
                                   :style-name   "widget"
                                   :height       "500px"}}}

              {:type        :simple-text
               :name        :time-widget
               :data-source :current-time
               :options     {:color "lightblue"}}])

(def widget-layout {
                    :spectrum-line-widget {:layout-opts {:position {:lg {:x 0 :y 0 :w 5 :h 2}
                                                                    :md {:x 0 :y 0 :w 5 :h 2}
                                                                    :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}
                    :spectrum-bar-widget  {:layout-opts {:position {:lg {:x 0 :y 2 :w 5 :h 2}
                                                                    :md {:x 0 :y 2 :w 5 :h 2}
                                                                    :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}
                    :time-widget          {:layout-opts {:position {:lg {:x 5 :y 0 :w 1 :h 2}
                                                                    :md {:x 5 :y 0 :w 1 :h 2}
                                                                    :sm {:x 0 :y 2 :w 2 :h 2 :static true}}}}})

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
