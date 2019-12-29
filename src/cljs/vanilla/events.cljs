(ns vanilla.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [ajax.core :as ajax]
            [vanilla.update-layout :as u]))


; widget management


(rf/reg-event-db
  :initialize
  (fn
    [db [_ layout options widgets]]
    (prn (str ":initialize handler " widgets))
    (merge db {:data-sources {}
               :hc-type {}})))



(rf/reg-event-db
  :update-data-source
  (fn [app-state [_ data-source new-val]]
    (assoc-in  app-state [:data-sources data-source] new-val)))



(rf/reg-event-db
  :next-id
  (fn-traced [db [_ id]]
    (assoc db :next-id id)))


(rf/reg-event-db
  :init-widgets
  (fn-traced [db [_ widgets]]
    (prn (str ":init-widgets " widgets))
    (assoc db
      :widgets widgets
      :next-id (inc (count widgets)))))


(rf/reg-event-db
  :add-widget
  (fn-traced [db [_ new-widget]]
    (let [next-id (:next-id db)
          named-widget (assoc new-widget :key (str next-id))]

      (do
        (prn ":add-widget " named-widget)
        (assoc db
          :widgets (conj (:widgets db) named-widget)
          :next-id (inc next-id))))))



(rf/reg-event-db
  :remove-widget
  (fn-traced [db [_ widget-id]]
    (assoc db :widgets (remove #(= (:key %) widget-id) (:widgets db)))))



(rf/reg-event-db
  :update-layout
  (fn-traced [db [_ layout]]
    (prn (str ":update-layout " layout))
    (assoc db :widgets (u/update-layout (:widgets db) (u/reduce-layouts layout)))))




; highcharts types

(rf/reg-event-db
  :register-hc-type
  (fn-traced [db [_ type type-fn]]
    (prn "registering highcharts type " type)
    (assoc-in db [:hc-type type] type-fn)))




; support services


(rf/reg-event-db
  :set-version
  (fn [db [_ version]]
    ;(.log js/console (str ":set-version " version))
    (assoc db :version (:version version))))


(rf/reg-event-db
  :set-services
  (fn [db [_ services]]
    ;(.log js/console (str ":set-services " services))
    (assoc db :services (:services services))))

