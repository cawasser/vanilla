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

    [vanilla.home-page :as home]
    [vanilla.login :as login]

    ;[vanilla.add-widget :as add-wid]
    ;[vanilla.widgets.configure-widget :as wc]
    ;[vanilla.grid :as grid]

    [vanilla.version :as ver]


    [reitit.core :as re]
    [goog.events :as events]
    [goog.history.EventType :as HistoryEventType]
    [clojure.string :as string]



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
    [vanilla.widgets.vari-pie-chart]
    [vanilla.widgets.continent-map]
    [vanilla.widgets.australia-map])
  (:import goog.History))




(enable-console-print!)

(rf/reg-event-db
  :initialize
  (fn-traced
    [db _]
    (prn (str ":initialize handler "))
    (merge db {:data-sources {}
               :hc-type {}
               ;:chosen-bg-color {:r 150 :g 150 :b 150 :a 1.0}
               ;:chosen-txt-color "white"
               :configure-widget ""})))

(rf/reg-event-db
  :widget-type
  (fn-traced [db [_ widget]]
             ;(prn (str ":widget-type " widget))
             (assoc-in db [:widget-types (:name widget)] widget)))


;(rf/reg-event-db
;  :set-version
;  (fn-traced [db [_ version]]
;             ;(prn ":set-version " version)
;             (assoc db :version (:version version))))


(rf/reg-event-db
  :set-services
  (fn-traced [db [_ services]]
             ;(prn ":set-services " services)
             (assoc db :services (:services services))))




;;;;;;;
;
; Routing & Page Navigation

(rf/reg-event-db
  :navigate
  (fn-traced
    [db [_ route]]
    (assoc db :route route)))


(rf/reg-sub
  :page
  :<- [:route]
  (fn [route _]
    (-> route :data :name)))

(rf/reg-sub
  :route
  (fn [db _]
    (-> db :route)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SERVICES LIST NUMBER
;
;

(defn get-services []
  (GET "/services" {:headers         {"Accept" "application/transit+json"}
                    :response-format (ajax/json-response-format {:keywords? true})
                    :handler         #(rf/dispatch-sync [:set-services %])}))




;;;;;;;;;;
;
;    Navbar stuff
;


(defn nav-link
  "Take in a link, title/label, and page data so it can return a nav-link item"
  [uri title page]
  [:a.navbar-item
   {:href   uri
    :active (when (= page @(rf/subscribe [:page])) "active")}
   title])

(defn navbar
  ""
  []
  [:nav.navbar.is-info
   [:div.container
    [:div.navbar-brand
     [:img.navbar-item {:src "/images/bh_icon.png"
                        :alt "Black Hammer"
                        :height 90
                        :width 200}]]
    [:div.nav-menu.navbar-menu
     [:div.navbar-end
      [nav-link "#/" "Home" :home]
      (if (some? @(rf/subscribe [:get-current-user]))
        [nav-link "#/logout" "Log Out" :logout]
        [nav-link "#/login" "Login" :login])]]]])



;;;;;;;;;;;
;
;    Routing and Page management

(def pages
  {:home #'home/home-page
   :login #'login/login-page
   :logout #'login/logout-sequence})

(def router
  (re/router
    [["/" :home]
     ["/login" :login]
     ["/logout" :logout]]))

(defn page
  []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])


;; -------------------------
;; History
;; must be called after routes have been defined
;(defn hook-browser-navigation!
;  ""
;  ;; TODO - What does this do exactly?
;  []
;  ;(let [hist (history/History)])
;  (doto (History.)
;    (events/listen
;      HistoryEventType/NAVIGATE    ;; Nav type
;      (fn [event]                  ;; Nav callback
;        (let [uri (or (not-empty (string/replace (.-token event) #"^.*#" "")) "/")]
;          (rf/dispatch
;            [:navigate (re/match-by-path router uri)]))))
;    (.setEnabled true)))


;;;;;;;;;;;
;
;   Initialize the application

(defn mount-components
  "Right now we may not need to clear sub cache - find this out"
  []
  ;(rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))


(defn start-dashboard
  "Initialize all the data needed for the start of our SPA, ends by calling the UI to generate"
  []

  ;(prn "calling :initialize")
  (rf/dispatch-sync [:initialize])




  (ver/get-version)
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
  (vanilla.widgets.continent-map/register-type)
  (vanilla.widgets.australia-map/register-type)

  (doseq [w defs/widgets]
    (rf/dispatch-sync [:widget-type w]))

  (d/connect-to-data-sources)

  ;; Set up page navigation and routing
  (rf/dispatch-sync [:navigate (re/match-by-name router :home)])

  ;(hook-browser-navigation!)

  (mount-components))
  ;(r/render home-page (.getElementById js/document "app")))



;(start-dashboard)
