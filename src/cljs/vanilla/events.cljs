(ns vanilla.events
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]))



; subscriptions

(rf/register-sub
  :version
  (fn [db _]
    (.log js/console (str ":version " @db))
    (reaction (get @db :version))))




; handlers


(rf/register-handler
  :set-version
  (fn [db [_ version]]
    (.log js/console (str ":set-version " version))
    (assoc db :version (:version version))))
