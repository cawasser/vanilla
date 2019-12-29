(ns vanilla.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [dashboard-clj.core :as d]
    [vanilla.widget-cards :as cards]
    [vanilla.events]
    [vanilla.subscriptions :as subs]
    [vanilla.widget-defs :as defs]
    [vanilla.widget-layout :as wlo]
    [ajax.core :refer [GET POST] :as ajax]

    [vanilla.grid :as grid]

    ; needed to register all the highcharts types
    [vanilla.widgets.area-chart]
    [vanilla.widgets.bar-chart]
    [vanilla.widgets.bubble-chart]
    [vanilla.widgets.column-chart]
    [vanilla.widgets.dependency-chart]
    [vanilla.widgets.heatmap-chart]
    [vanilla.widgets.line-chart]
    [vanilla.widgets.network-graph-chart]
    [vanilla.widgets.org-chart]
    [vanilla.widgets.pie-chart]
    [vanilla.widgets.rose-chart]
    [vanilla.widgets.sankey-chart]
    [vanilla.widgets.scatter-chart]
    [vanilla.widgets.vari-pie-chart]))




(enable-console-print!)



(defn add-widget [new-widget]
  (prn "add-widget " new-widget)

  (rf/dispatch [:add-widget new-widget]))






(defn add-canned-widget []
  (prn "add-canned-widget")

  (add-widget (grid/new-widget)))




(enable-console-print!)




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SERVICES LIST NUMBER
;
;

(defn get-services []
  (GET "/services" {:headers         {"Accept" "application/transit+json"}
                    :response-format (ajax/json-response-format {:keywords? true})
                    :handler         #(rf/dispatch-sync [:set-services %])}))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SERVICES AND WIDGET PICKER
;
;
(defn- selected-service [services selected]
  (let [ret-val (first (filter #(= selected (:name %)) services))]

    ;(prn "selected-service " services
    ;  " //// selected " selected
    ;  " //// ret-val " ret-val)

    ret-val))


(defn- filter-widgets [widgets selected]
  ;(prn "filter-widgets " selected
  ;  " //// ret_types " (keyword (:ret_types selected)))

  (let [ret-val (filter #(if (some #{(keyword (:ret_types selected))}
                               (:ret_types %)) true false) widgets)]

    ;(prn (str "filter-widgets " selected
    ;       " //// ret_types " (keyword (:ret_types selected))
    ;       " //// ret-val " ret-val))

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
             ;(prn "selected: " @selected)
             [:td name] [:td doc_string]])))]]]])


(defn widget-card [keywrd label img chosen-widget]
  [:div.card {:class    (if (= @chosen-widget keywrd) "is-selected" "")
              :style    {:background-color (if (= @chosen-widget keywrd) "lightgreen" "")}
              :on-click #(do
                           ;(prn "widget-card " name
                           ;  " //// chosen-widget " @chosen-widget)

                           (reset! chosen-widget keywrd))}
   [:div.image
    [:figure.image.is-128x128
     [:img {:src img}]]]
   [:div.card-content
    [:p.subtitle.is-7.has-text-centered label]]])



(defn widget-list [widgets s chosen-widget]
  (let [widget-cards (filter-widgets widgets s)]

    ;(prn "widget-list " widgets
    ;  " //// selected " s
    ;  " //// chosen-widget " @chosen-widget
    ;  " //// cards " widget-cards)

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

        ;(prn "add-widget-modal " (selected-service @services @selected))

        [:section.modal-card-body
         [widget-list cards/widget-cards (selected-service @services @selected) chosen-widget]]

        [:footer.modal-card-foot
         [:button.button.is-success {:on-click #(do
                                                  (prn "adding widget " @chosen-widget)
                                                  (add-widget @chosen-widget)
                                                  (reset! is-active false))} "Add"]

         [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; VERSION NUMBER
;
;

(defn get-version []
  (GET "/version" {:headers         {"Accept" "application/transit+json"}
                   :response-format (ajax/json-response-format {:keywords? true})
                   :handler         #(rf/dispatch-sync [:set-version %])}))


(defn version-number []
  (let [version (rf/subscribe [:version])
        is-active (r/atom false)]
    (fn []
      [:container.level {:width "100%"}
       [:div.level-left.has-text-left
        [:h7.subtitle.is-6 @version]]
       [:div.level-right.has-text-right
        [:button.button.is-link {:on-click #(swap! is-active not)} "Add"]
        [:button.button.is-link {:on-click #(add-canned-widget)} "widget"]
        [:p "working"]]
       [add-widget-modal is-active]])))


(defn- widgets-log []
  [:p (str "widgets " @(rf/subscribe [:widgets]))])




(def width 1200)
(def height 300)
(def rows 10)
(defn- widgets-grid []
  [grid/Grid {:id          "dashboard-widget-grid"
              :cols        {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}
              :width       width
              :row-height  (/ height rows)
              :breakpoints {:lg 1200 :md 996 :sm 768 :xs 480 :xxs 0}
              :data        @(rf/subscribe [:widgets])
              :on-change   #();prn (str "layout change. prev " %1 " //// new " %2))
              :item-props  {:class "widget-component"}}])



(defn home-page []
  [:div {:width "100%"}
   [:div.container
    [:div.content {:width "100%"}
     [version-number]]]
     ;[widgets-log]
   [widgets-grid]])






(defn start-dashboard []

  (prn  "calling :next-id ")
  (rf/dispatch-sync [:next-id 1])

  (prn "calling :initialize")
  (rf/dispatch-sync [:initialize])

  (get-version)
  (get-services)

  ; TODO eliminate register-global-app-state-subscription (attach subscription in add-widget)
  (subs/register-global-app-state-subscription)

  ; TODO: replace individual HC registration calls with the data from the server
  (vanilla.widgets.area-chart/register-type)
  (vanilla.widgets.bar-chart/register-type)
  (vanilla.widgets.bubble-chart/register-type)
  (vanilla.widgets.column-chart/register-type)
  (vanilla.widgets.dependency-chart/register-type)
  (vanilla.widgets.heatmap-chart/register-type)
  (vanilla.widgets.line-chart/register-type)
  (vanilla.widgets.network-graph-chart/register-type)
  (vanilla.widgets.org-chart/register-type)
  (vanilla.widgets.pie-chart/register-type)
  (vanilla.widgets.rose-chart/register-type)
  (vanilla.widgets.sankey-chart/register-type)
  (vanilla.widgets.scatter-chart/register-type)
  (vanilla.widgets.vari-pie-chart/register-type)

  (d/connect-to-data-sources)
  (r/render home-page (.getElementById js/document "app")))


;(start-dashboard)
