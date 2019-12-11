(ns dashboard-clj.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]))



(defn register-global-app-state-subscription[]
  (rf/register-sub
    :app-db
    (fn [db [sid & db-path]]
      (reaction
        (get-in @db (flatten [:data-sources db-path]))))))



(rf/register-sub
  :widgets
  (fn [db _]
    (reaction
      (.log js/console ":widgets subscription")
      (get @db :widgets))))



(rf/register-sub
  :options
  (fn [db _]
    (reaction
      (get @db :options))))




(rf/register-sub
  :layout
  (fn [db _]
    (reaction
      (get @db :layout))))

