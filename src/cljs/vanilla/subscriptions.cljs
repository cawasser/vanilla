(ns vanilla.subscriptions
  (:require [re-frame.core :as rf]))




(rf/reg-sub
  :version
  (fn [db _]
    ;(prn (str ":version " db))
    (get db :version)))


(rf/reg-sub
  :current-user
  (fn [db _]
    ;(prn (str ":version " db))
    (get db :current-user)))




(rf/reg-sub
  :services
  (fn [db _]
    ;(prn (str ":services " db))
    (get db :services)))


; widget-type registry and adding new widgets

(rf/reg-sub
  :widget-type
  (fn [db [_ type]]
    (get-in db [:widget-types type])))


(rf/reg-sub
  :all-widget-types
  (fn [db _]
    (get db :widget-types)))


(rf/reg-sub
  :widgets
  (fn [db _]
    (:widgets db)))

(rf/reg-sub
  :next-id
  (fn [db _]
    (:next-id db)))


; data source registry

(defn register-global-app-state-subscription[]
  (rf/reg-sub
    :app-db
    (fn [db [sid & db-path]]
      ;(prn ":app-db " db-path)
      (get-in db (flatten [:data-sources db-path])))))


(rf/reg-sub
  :hc-type
  (fn [db [_ type]]
    ;(prn ":hc-type subscription " type)
    (get-in db [:hc-type type])))



; adding new widgets picker

(rf/reg-sub
  :selected-service
  (fn [db [_]]
    ;(prn ":selected-service subscription ")
    (get db :selected-service)))

(rf/reg-sub
  :selected-widget
  (fn [db [_]]
    ;(prn ":selected-widget subscription ")
    (get db :selected-widget)))


; Login stuff

(rf/reg-sub
  :get-current-user
  (fn [db _]
    (get db :current-user)))

