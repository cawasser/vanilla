(ns vanilla.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [dashboard-clj.core :as d]
    [vanilla.subscriptions :as subs]
    [vanilla.widget-defs :as defs]
    [ajax.core :refer [GET POST] :as ajax]
    [cljs-uuid-utils.core :as uuid]

    [day8.re-frame.tracing :refer-macros [fn-traced]]

    [vanilla.add-widget-modal :as modal]
    [vanilla.login-modal :as login]
    [clojure.edn :as edn]

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

(rf/reg-event-db
  :initialize
  (fn-traced
    [db _]
    (prn (str ":initialize handler "))
    (merge db {:data-sources {}
               :hc-type {}
               :chosen-bg-color {:r 150 :g 150 :b 150 :a 1.0}
               :chosen-txt-color "white"})))

(rf/reg-event-db
  :widget-type
  (fn-traced [db [_ widget]]
             ;(prn (str ":widget-type " widget))
             (assoc-in db [:widget-types (:name widget)] widget)))


;; Helper function to set-layout to define function called for each value
(def conversion {:ret_types edn/read-string
                 :name edn/read-string
                 :basis edn/read-string
                 :data-grid edn/read-string
                 :type edn/read-string
                 :data-source edn/read-string
                 :options edn/read-string})

(rf/reg-event-db
  :set-layout
  (fn-traced [db [_ layout-data]]
   ;(prn "Set-layout start: " (:layout layout-data))
     (if (not (empty? (:layout layout-data)))             ; if its not initial page load with empty layout-table
       (let [data (:layout layout-data)
             converted-data (mapv (fn [{:keys [ret_types key name basis data-grid type data-source options] :as original}]
                                    (assoc original
                                      :ret_types ((:ret_types conversion) ret_types)
                                      :name ((:name conversion) name)
                                      :basis ((:basis conversion) basis)
                                      :data-grid ((:data-grid conversion) data-grid)
                                      :type ((:type conversion) type)
                                      :data-source ((:data-source conversion) data-source)
                                      :options ((:options conversion) options)))
                                  data)]     ; Will need to address how we actually want to handle widget id's

       ;(prn ":set-layout CONVERTED: " converted-data
       ;     "///// nextid: " highestNextid)

        (assoc db :widgets converted-data
                :next-id (uuid/uuid-string (uuid/make-random-uuid))))
        ; else it is initial page load with empty layout-table
        (assoc db :widgets []
                 :next-id (uuid/uuid-string (uuid/make-random-uuid))))))

(rf/reg-event-db
  :set-version
  (fn-traced [db [_ version]]
             ;(prn ":set-version " version)
             (assoc db :version (:version version))))


(rf/reg-event-db
  :set-services
  (fn-traced [db [_ services]]
             ;(prn ":set-services " services)
             (assoc db :services (:services services))))




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
; VERSION NUMBER
;
;

(defn get-version []
  (GET "/version" {:headers         {"Accept" "application/transit+json"}
                   :response-format (ajax/json-response-format {:keywords? true})
                   :handler         #(rf/dispatch-sync [:set-version %])}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; LAYOUT SAVING
;
;

(defn get-layout [user]
  (prn "getting layout")
  (if (some? user)    ;if a user is logged in, go get their widgets, otherwise clear screen
    (GET "/layout" {:headers          {"Accept" "application/transit+json"}
                    :response-format  (ajax/json-response-format {:keywords? true})
                    :params           {:username user}
                    :handler          #(rf/dispatch-sync [:set-layout %])})
    (rf/dispatch-sync [:set-layout []])))





(def width 1536)
(def height 1024)
(def rows 50)


(defn- widgets-grid []
  [grid/Grid {:id          "dashboard-widget-grid"
              :cols        {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}
              :width       width
              :row-height  (/ height rows)
              :breakpoints {:lg 2048 :md 1024 :sm 768 :xs 480 :xxs 0}
              :data        @(rf/subscribe [:widgets])
              :on-change   #();prn (str "layout change. prev " %1 " //// new " %2))
              :item-props  {:class "widget-component"}}])




(defn top-right-buttons
  "Determine whether buttons in the top right of the dashboard show either:
  - A login button
  - An add widget button alongside a logout button"
  []
  (get-layout @(rf/subscribe [:get-current-user]))
  (if (some? @(rf/subscribe [:get-current-user]))
    [:div.level-right.has-text-right
     [modal/add-widget-button]
     [login/logout-button]]
    [:div.level-right.has-text-right
     [login/login-button]]))

(defn home-page
  "This is the start of the UI for our SPA"
  []
  [:div {:width "100%"}
   [:div.container
    [:div.content {:width "100%"}
     [:div.container.level.is-fluid {:width "100%"}
      [:div.level-left.has-text-left
       [modal/version-number]]
      [top-right-buttons]]]]
   [widgets-grid]])



(defn start-dashboard
  "Initialize all the data needed for the start of our SPA, ends by calling the UI to generate"
  []

  ;(prn "calling :initialize")
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

  (doseq [w defs/widgets]
    (rf/dispatch-sync [:widget-type w]))

  (d/connect-to-data-sources)

  (r/render home-page (.getElementById js/document "app")))


;(start-dashboard)
