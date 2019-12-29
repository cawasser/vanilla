(ns vanilla.subscriptions
  (:require [re-frame.core :as rf]))




(rf/reg-sub
  :version
  (fn [db _]
    ;(prn (str ":version " db))
    (get db :version)))



(rf/reg-sub
  :services
  (fn [db _]
    ;(prn (str ":services " db))
    (get db :services)))




(rf/reg-sub
  :widgets
  (fn [db _]
    (:widgets db)))



(rf/reg-sub
  :next-id
  (fn [db _]
    (:next-id db)))



(defn register-global-app-state-subscription[]
  (rf/reg-sub
    :app-db
    (fn [db [sid & db-path]]
      (get-in db (flatten [:data-sources db-path])))))




(rf/reg-sub
  :hc-type
  (fn [db [_ type]]
    (prn ":hc-type subscription " type)
    (get-in db [:hc-type type])))
