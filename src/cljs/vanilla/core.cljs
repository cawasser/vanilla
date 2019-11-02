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
    [vanilla.events]
    [vanilla.widget-defs :as defs]
    [vanilla.widget-layout :as wlo]
    [ajax.core :refer [GET POST] :as ajax]))




(def dashboard {:layout  :responsive-grid-layout
                :options {:layout-opts {:cols {:lg 6 :md 4 :sm 2 :xs 1 :xxs 1}}}
                :widgets (mapv #(merge % (get wlo/widget-layout (:name %))) defs/widgets)})

(defn get-version []
  (GET "/version" {:headers {"Accept" "application/transit+json"}
                   :response-format (ajax/json-response-format {:keywords? true})
                   :handler #(rf/dispatch-sync [:set-version %])}))


(defn version-number []
  (let [version (rf/subscribe [:version])]
    (fn []
      [:h7.subtitle.is-6 @version])))



(defn home-page []
     (.log js/console "home-page")
     [:div.container>div.content
      [version-number]])





(defn start-dashboard []
  (rf/dispatch-sync [:initialize])

  ; build all the required widgets

  ;(.log js/console (str "building widgets " widgets))
  (doall (map wb/build-widget defs/widgets))

  (get-version)

  (d/register-global-app-state-subscription)
  (d/connect-to-data-sources)
  (r/render home-page (.getElementById js/document "app"))
  (d/start-dashboard dashboard "dashboard"))


(start-dashboard)
