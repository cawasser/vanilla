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


(def widget-cards
  [{:keywrd :line-widget, :ret_types [:data-format/x-y], :icon "/images/line-widget.png", :label "Line"}
   {:keywrd :area-widget, :ret_types [:data-format/x-y], :icon "/images/area-widget.png", :label "Area"}
   {:keywrd :bar-widget, :ret_types [:data-format/x-y], :icon "/images/bar-widget.png", :label "Bar"}
   {:keywrd :column-widget, :ret_types [:data-format/x-y], :icon "/images/column-widget.png", :label "Column"}
   {:keywrd :pie-widget, :ret_types [:data-format/x-y], :icon "/images/pie-widget.png", :label "Pie"}
   {:keywrd :vari-pie-widget,
    :ret_types [:data-format/x-y-n],
    :icon "/images/vari-pie-widget.png",
    :label "Variable Pie"}
   {:keywrd :rose-widget, :ret_types [:data-format/x-y-n], :icon "/images/rose-widget.png", :label "Wind Rose"}
   {:keywrd :stoplight-widget,
    :ret_types [:data-format/entity],
    :icon "/images/stoplight-widget.png",
    :label "Stoplight"}
   {:keywrd :map-widget, :ret_types [:data-format/lat-lon-n], :icon "/images/map-widget.png", :label "Map"}
   {:keywrd :sankey-widget, :ret_types [:data-format/x-y], :icon "/images/sankey-widget.png", :label "Sankey"}
   {:keywrd :deps-widget, :ret_types [:data-format/x-y], :icon "/images/deps-widget.png", :label "Dependencies"}
   {:keywrd :network-widget, :ret_types [:data-format/x-y], :icon "/images/network-widget.png", :label "Network"}
   {:keywrd :org-widget, :ret_types [:data-format/x-y], :icon "/images/org-widget.png", :label "Org Chart"}
   {:keywrd :heatmap-widget, :ret_types [:data-format/x-y-n], :icon "/images/heatmap-widget.png", :label "Heatmap"}])



(defn get-services []
  (GET "/services" {:headers {"Accept" "application/transit+json"}
                    :response-format (ajax/json-response-format {:keywords? true})
                    :handler #(rf/dispatch-sync [:set-services %])}))



(defn- selected-service [services selected]
  (let [ret-val (first (filter #(= selected (:name %)) services))]

    (.log js/console (str "selected-service " services
                       ", selected " selected
                       ", ret-val " ret-val))

    ret-val))


(defn- filter-widgets [widgets selected]
  (.log js/console (str "filter-widgets " selected
                     ", ret_types " (keyword (:ret_types selected))))

  (let [ret-val (filter #(if (some #{(keyword (:ret_types selected))}
                               (:ret_types %)) true false) widgets)]

    (.log js/console (str "filter-widgets " selected
                       ", ret_types " (:ret_types selected)
                       ", ret-val " ret-val))

    ret-val))




(defn service-list [services selected]
  [:div.container
   [:table-container
    [:table.is-hoverable
     [:thead
      [:tr [:th "Name"] [:th "Description"]]]
     [:tbody
      (doall
        (for [{:keys [name doc_string]} @services]
          (do
            ^{:key name}
            [:tr {:class (if (= @selected name) "is-selected" "")
                  :style {:background-color (if (= @selected name) "lightgreen" "")}
                  :on-click #(do
                               (reset! selected name)
                               (.log js/console (str "selected: " @selected)))}
             [:td name] [:td doc_string]])))]]]])


(defn widget-card [name img]
  [:div.card
   [:div.image
    [:figure.image.is-128x128
     [:img {:src img}]]]
   [:div.card-content
    [:p.subtitle.is-7.has-text-centered name]]])



(defn widget-list [widgets s]
  (let [widget-cards (filter-widgets widgets s)]

    (.log js/console (str "widget-list " widgets
                       ", selected " s
                       ", cards " widget-cards))
    [:table>tbody
     [:tr
      (for [{:keys [keywrd icon name]} widget-cards]
        ^{:key keywrd}[:td [widget-card name icon]])]]))



(defn add-widget-model [is-active]
  (let [services (rf/subscribe [:services])
        selected (r/atom "")]
    (fn []
      [:div.modal (if @is-active {:class "is-active"})
       [:div.modal-background]
       [:div.modal-card
        [:header.modal-card-head
         [:p.modal-card-title "Add Data Source"]
         ;[:p @selected]
         ;[:p @services]
         [:button.delete {:aria-label "close"
                          :on-click #(reset! is-active false)}]]

        [:section.modal-card-body
         [service-list services selected]]

        (.log js/console (str "add-widget-model " (selected-service @services @selected)))

        [:section.modal-card-body
         [widget-list widget-cards (selected-service @services @selected)]]

        [:footer.modal-card-foot
         [:button.button.is-success {:on-click #(reset! is-active false)} "Add"]
         [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]])))


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
