(ns dashboard-clj.events
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]
            [vanilla.widgets.widget-base :as wb]))


(rf/register-handler
  :initialize
  (fn
    [db [_ layout options widgets]]
    (.log js/console ":initialize handler")
    (merge db {:data-sources {}
               :layout layout
               :options options
               :widgets widgets})))



(rf/register-handler
  :update-data-source
  (fn [app-state [_ data-source new-val]]
    (assoc-in  app-state [:data-sources data-source] new-val)))




(rf/register-handler
  :add-widget
  (fn [app-state [_ new-widget]]
    (let [orig (:widgets app-state)
          c (conj orig new-widget)
          ret-val (assoc app-state :widgets (conj (:widgets app-state) new-widget))]

      (.log js/console (str ":add-widget handler " new-widget
                         " //// orig " orig
                         " //// new " (:widgets ret-val)))

      (.log js/console (str "calling build-widget " new-widget))

      (wb/build-widget new-widget)

      ret-val)))



