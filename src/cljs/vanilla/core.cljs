(ns vanilla.core
  (:require [dashboard-clj.core :as d]
            [dashboard-clj.layouts.grid-layout-responsive :as grid]
            [vanilla.widgets.simple-text]
            [re-frame.core :as rf]))

(def widgets [{:type        :simple-text
               :name        :sample-widget
               :data-source :welcome-message
               :options     {:data    {:style {:font-weight      "bold"
                                               :font-size        "large"
                                               :background-color "red"
                                               :color            "white"
                                               :border-style     "solid"
                                               :border-radius    "5px"
                                               :border-width     "2px"}}
                             :wrapper {:style {:border-style  "solid"
                                               ;:border-radius "5px"
                                               :border-width  "5px"}}}}
              {:type        :simple-text
               :name        :time-widget
               :data-source :current-time
               :options     {}}])

(def widget-layout {
                    :sample-widget {:layout-opts {:position {:lg {:x 0 :y 0 :w 2 :h 2}
                                                             :md {:x 0 :y 0 :w 2 :h 2}
                                                             :sm {:x 0 :y 0 :w 2 :h 2 :static true}}}}
                    :time-widget   {:layout-opts {:position {:lg {:x 2 :y 0 :w 2 :h 2}
                                                             :md {:x 2 :y 0 :w 2 :h 2}
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
