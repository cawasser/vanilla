(ns vanilla.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [ajax.core :as ajax]
            [vanilla.update-layout :as u]))


; data source management


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
  :update-data-source
  (fn-traced
    [app-state [_ data-source new-val]]
    (assoc-in  app-state [:data-sources data-source] new-val)))



; widget management

(rf/reg-event-db
  :widget-type
  (fn-traced [db [_ widget]]
    ;(prn (str ":widget-type " widget))
    (assoc-in db [:widget-types (:name widget)] widget)))



(rf/reg-event-db
  :next-id
  (fn-traced [db [_ id]]
    (assoc db :next-id id)))


(rf/reg-event-db
  :add-widget
  (fn-traced [db [_ new-widget-type data-source]]
    (let [next-id (:next-id db)
          widget-type (get-in db [:widget-types new-widget-type])
          named-widget (assoc widget-type
                         :key (str next-id)
                         :data-source data-source
                         :data-grid {:x 0 :y 0 :w 5 :h 15})]

      (do
        (prn ":add-widget " new-widget-type
          " //// widget-type " widget-type
          " //// named-widget " named-widget)
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
    ;(prn (str ":update-layout " layout))
    (assoc db :widgets (u/update-layout (:widgets db) (u/reduce-layouts layout)))))




; adding new widgets picker support

(rf/reg-event-db
  :init-selected-service
  (fn-traced [db _]
    ;(prn (str ":init-selected-service " (first (:services db))))
    (assoc db :selected-service (first (:services db)))))


(rf/reg-event-db
  :selected-service
  (fn-traced [db [_ s]]
    ;(prn (str ":selected-service " s))
    (assoc db :selected-service s)))


(rf/reg-event-db
  :selected-new-widget-type
  (fn-traced [db [_ w]]
    ;(prn (str ":selected-new-widget-type " w))
    (assoc db :selected-new-widget-type w)))



; highcharts types

(rf/reg-event-db
  :register-hc-type
  (fn-traced [db [_ type type-fn]]
    ;(prn "registering highcharts type " type)
    (assoc-in db [:hc-type type] type-fn)))





; support services


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

