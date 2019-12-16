(ns vanilla.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [dashboard-clj.core :as d]
    [dashboard-clj.events]
    [dashboard-clj.subscriptions :as subs]
    [dashboard-clj.layouts.grid-layout-responsive :as grid]
    [vanilla.widget-cards :as cards]
    [vanilla.widgets.widget-base :as wb]
    [vanilla.widgets.simple-text]
    [vanilla.widgets.stoplight-widget]
    [vanilla.widgets.map]
    [vanilla.events]
    [vanilla.widget-defs :as defs]
    [vanilla.widget-layout :as wlo]
    [ajax.core :refer [GET POST] :as ajax]

    [dashboard-clj.layouts.core :as layout]))


(def default-layout {:layout-opts
                     {:position {:lg {:x 0 :y 0 :w 4 :h 4}
                                 :md {:x 0 :y 0 :w 4 :h 4}
                                 :sm {:x 0 :y 0 :w 4 :h 4 :static true}}}})




(defn add-widget [widget-name]
  (.log js/console (str "add-widget " widget-name))

  (let [widget (wb/get-widget widget-name)]

    (.log js/console (str "add-widget " widget
                       " //// merged " (merge default-layout widget)))

    (rf/dispatch-sync [:add-widget (merge widget default-layout)])))





(defn add-canned-widget []
  (.log js/console "add-canned-widget")

  (add-widget :bubble-chart))








(defn get-services []
  (GET "/services" {:headers         {"Accept" "application/transit+json"}
                    :response-format (ajax/json-response-format {:keywords? true})
                    :handler         #(rf/dispatch-sync [:set-services %])}))



(defn- selected-service [services selected]
  (let [ret-val (first (filter #(= selected (:name %)) services))]

    ;(.log js/console (str "selected-service " services
    ;                   " //// selected " selected))
    ;                   " //// ret-val " ret-val))

    ret-val))


(defn- filter-widgets [widgets selected]
  ;(.log js/console (str "filter-widgets " selected
  ;                   " //// ret_types " (keyword (:ret_types selected))))

  (let [ret-val (filter #(if (some #{(keyword (:ret_types selected))}
                               (:ret_types %)) true false) widgets)]

    ;(.log js/console (str "filter-widgets " selected
    ;                   " //// ret_types " (keyword (:ret_types selected))
    ;                   " //// ret-val " ret-val))

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
            [:tr {:class    (if (= @selected name) "is-selected" "")
                  :style    {:background-color (if (= @selected name) "lightgreen" "")}
                  :on-click #(do
                               (reset! selected name))}
             ;(.log js/console (str "selected: " @selected)))}
             [:td name] [:td doc_string]])))]]]])


(defn widget-card [keywrd label img chosen-widget]
  [:div.card {:class    (if (= @chosen-widget keywrd) "is-selected" "")
              :style    {:background-color (if (= @chosen-widget keywrd) "lightgreen" "")}
              :on-click #(do
                           (.log js/console (str "widget-card " name
                                              " //// chosen-widget " @chosen-widget))

                           (reset! chosen-widget keywrd))}
   [:div.image
    [:figure.image.is-128x128
     [:img {:src img}]]]
   [:div.card-content
    [:p.subtitle.is-7.has-text-centered label]]])



(defn widget-list [widgets s chosen-widget]
  (let [widget-cards (filter-widgets widgets s)]

    ;(.log js/console (str "widget-list " widgets
    ;                   " //// selected " s
    ;                   " //// chosen-widget " @chosen-widget))
    ;" //// cards " widget-cards))

    [:table>tbody
     [:tr

      ; TODO - HACK!!!! using dummy-name to get at the correct widget

      (for [{:keys [dummy-name icon label]} widget-cards]
        ^{:key dummy-name} [:td
                            [widget-card dummy-name label icon chosen-widget]])]]))



(defn add-widget-modal [is-active]
  (let [services (rf/subscribe [:services])
        selected (r/atom (:name (first @services)))
        chosen-widget (r/atom {})]                          ;(r/atom "")]
    (fn []
      [:div.modal (if @is-active {:class "is-active"})
       [:div.modal-background]
       [:div.modal-card
        [:header.modal-card-head
         [:p.modal-card-title "Add Data Source"]
         ;[:p (str "selected " @selected)]
         ;[:p (str @services)]
         [:button.delete {:aria-label "close"
                          :on-click   #(reset! is-active false)}]]

        [:section.modal-card-body
         [service-list services selected]]

        ;(.log js/console (str "add-widget-modal " (selected-service @services @selected)))

        [:section.modal-card-body
         [widget-list cards/widget-cards (selected-service @services @selected) chosen-widget]]

        [:footer.modal-card-foot
         [:button.button.is-success {:on-click #(do
                                                  (.log js/console (str "adding widget " @chosen-widget))
                                                  (add-widget @chosen-widget)
                                                  (reset! is-active false))} "Add"]

         [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]])))


(defn get-version []
  (GET "/version" {:headers         {"Accept" "application/transit+json"}
                   :response-format (ajax/json-response-format {:keywords? true})
                   :handler         #(rf/dispatch-sync [:set-version %])}))


(defn version-number []
  (let [version (rf/subscribe [:version])
        is-active (r/atom false)]
    (fn []
      [:container.level
       [:div.level-left.has-text-left
        [:h7.subtitle.is-6 @version]]
       [:div.level-right.has-text-right
        [:button.button.is-link {:on-click #(swap! is-active not)} "Add"]
        [:button.button.is-link {:on-click #(add-canned-widget)} "widget"]]
       [add-widget-modal is-active]])))


(defn- widgets-log []
  [:p (str "widgets " @(rf/subscribe [:widgets]))])


(defn- widgets-grid []
  (let [layout (rf/subscribe [:layout])
        options (rf/subscribe [:options])
        widgets (rf/subscribe [:widgets])]
    (fn []
      ; TODO - setup the layout manager first, the use the
      [layout/setup-layout @layout @options @widgets])))



(defn home-page []
  [:div.container>div.content
   [version-number]
   ;[widgets-log]
   [widgets-grid]])






(defn start-dashboard []
  (rf/dispatch-sync [:initialize
                     :responsive-grid-layout
                     {:layout-opts {:cols {:lg 8 :md 4 :sm 2 :xs 1 :xxs 1}}}
                     []])
                     ;(mapv #(merge % (get wlo/widget-layout (:name %))) defs/widgets)])



  ; build all the required widgets

  ;(.log js/console (str "building widgets " widgets))

  ; TODO: eliminate widget-base/build-widgets (personalization instead)
  (doall (map wb/build-widget defs/widgets))

  (get-version)
  (get-services)

  ; TODO eliminate register-global-app-state-subscription (attach subscription in add-widget)
  (subs/register-global-app-state-subscription)


  (d/connect-to-data-sources)
  (r/render home-page (.getElementById js/document "app")))
;(d/start-dashboard dashboard "dashboard"))


(start-dashboard)
