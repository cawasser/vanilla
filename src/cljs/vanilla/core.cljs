(ns vanilla.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [dashboard-clj.core :as d]
    [vanilla.events]
    [vanilla.subscriptions :as subs]
    [vanilla.widget-defs :as defs]
    [ajax.core :refer [GET POST] :as ajax]

    [vanilla.add-widget-modal :as modal]

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
    [vanilla.widgets.vari-pie-chart]
    [vanilla.widgets.continent-map]
    [vanilla.widgets.australia-map]))




(enable-console-print!)




;;;;;;;;;;;;;;;;;;;;;
;
; HACK
;
; TODO - remove dummy "new-widget" stuff

(def next-widget-idx (atom 0))

(defn add-canned-widget [])
  ;(let [rand-widget (get defs/widgets @next-widget-idx)]
  ;
  ;  ;(prn "add-canned-widget " rand-widget ", " @next-widget-idx, ", " (count defs/widgets))
  ;
  ;  (add-widget (grid/fixup-new-widget rand-widget))
  ;  (if (< @next-widget-idx (dec (count defs/widgets)))
  ;    (swap! next-widget-idx inc)
  ;    (reset! next-widget-idx 0))))


;;;;;;;;;;;;;;;;;;;;;
;
; END HACK


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
; VERSION NUMBER
;
;

(defn get-version []
  (GET "/version" {:headers         {"Accept" "application/transit+json"}
                   :response-format (ajax/json-response-format {:keywords? true})
                   :handler         #(rf/dispatch-sync [:set-version %])}))



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



(defn home-page []
  [:div {:width "100%"}
   [:div.container
    [:div.content {:width "100%"}
     [modal/version-number]]]
   [widgets-grid]])



(defn start-dashboard []

  ;(prn  "calling :next-id ")
  (rf/dispatch-sync [:next-id 1])

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
  (vanilla.widgets.continent-map/register-type)
  (vanilla.widgets.australia-map/register-type)

  (doseq [w defs/widgets]
    (rf/dispatch-sync [:widget-type w]))

  (d/connect-to-data-sources)
  (r/render home-page (.getElementById js/document "app")))


;(start-dashboard)
