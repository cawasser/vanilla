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


(def widget-cards [[:line-widget "/images/line-widget.png" "Line"]
                   [:area-widget "/images/area-widget.png" "Area"]
                   [:bar-widget "/images/bar-widget.png" "Bar"]
                   [:column-widget "/images/column-widget.png" "Column"]
                   [:pie-widget "/images/pie-widget.png" "Pie"]
                   [:vari-pie-widget "/images/vari-pie-widget.png" "Variable Pie"]
                   [:rose-widget "/images/rose-widget.png" "Wind Rose"]
                   [:stoplight-widget "/images/stoplight-widget.png" "Stoplight"]
                   [:map-widget "/images/map-widget.png" "Map"]
                   [:sankey-widget "/images/sankey-widget.png" "Sankey"]
                   [:deps-widget "/images/deps-widget.png" "Dependencies"]
                   [:network-widget "/images/network-widget.png" "Network"]
                   [:org-widget "/images/org-widget.png" "Org Chart"]
                   [:heatmap-widget "/images/heatmap-widget.png" "Heatmap"]])


(defn get-services []
  (GET "/services" {:headers {"Accept" "application/transit+json"}
                    :response-format (ajax/json-response-format {:keywords? true})
                    :handler #(rf/dispatch-sync [:set-services %])}))


(defn service-list [services]
  [:ul
   (for [s services]
     ^{:key s }[:li (str (:name s) "     " (:doc_string s))])])


(defn widget-card [name img]
  [:div.card
   [:div.image
    [:figure.image.is-128x128
     [:img {:src img}]]]
   [:div.card-content
    [:p.subtitle.is-7.has-text-centered name]]])



(defn add-widget-model [is-active]
  (let [services (rf/subscribe [:services])]
    [:div.modal (if @is-active {:class "is-active"})
     [:div.modal-background]
     [:div.modal-card
      [:header.modal-card-head
       [:p.modal-card-title "Add Data Source"]
       [:button.delete {:aria-label "close"
                        :on-click #(reset! is-active false)}]]

      [:section.modal-card-body
       [service-list @services]]

      [:section.modal-card-body
       [:table>tbody
        [:tr
         (for [[id img name] widget-cards]
           ^{:key id}[:td [widget-card name img]])]]]

      [:footer.modal-card-foot
       [:button.button.is-success {:on-click #(reset! is-active false)} "Add"]
       [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]]))


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
  (get-services)

  (d/register-global-app-state-subscription)
  (d/connect-to-data-sources)
  (r/render home-page (.getElementById js/document "app"))
  (d/start-dashboard dashboard "dashboard"))


(start-dashboard)
