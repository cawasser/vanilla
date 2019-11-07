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
    [ajax.core :refer [GET POST] :as ajax]
    [vanilla.widgets.heatmap-chart]))



(def dashboard {:layout  :responsive-grid-layout
                :options {:layout-opts {:cols {:lg 6 :md 4 :sm 2 :xs 1 :xxs 1}}}
                :widgets (mapv #(merge % (get wlo/widget-layout (:name %))) defs/widgets)})


(defn add-widget-model [is-active]
  [:div.modal (if @is-active {:class "is-active"})
   [:div.modal-background]
   [:div.modal-card
    [:header.modal-card-head
     [:p.modal-card-title "Modal title"]
     [:button.delete {:aria-label "close"
                      :on-click #(reset! is-active false)}]]

    [:section.modal-card-body
     [:p "we'll put the list of available data services here"]
     [:p "And we'll put the appropriate list of widgets here"]]

    [:footer.modal-card-foot
     [:button.button.is-success {:on-click #(reset! is-active false)} "Add"]
     [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]])


(defn get-version []
  (GET "/version" {:headers {"Accept" "application/transit+json"}
                   :response-format (ajax/json-response-format {:keywords? true})
                   :handler #(rf/dispatch-sync [:set-version %])}))


(defn version-number []
  (let [version (rf/subscribe [:version])
        is-active (r/atom false)]
    (fn []
      [:container.level
       [:div.level-left.has-text-left
        [:h7.subtitle.is-6 @version]]
       [:div.level-right.has-text-right
        [:button.button.is-link {:on-click #(swap! is-active not)} "Add"]]
       [add-widget-model is-active]])))



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
