(ns dashboard-clj.events
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]
            [vanilla.widgets.widget-base :as wb]))


(rf/register-handler
  :initialize
  (fn
    [db [_ layout options widgets]]
    ;(.log js/console ":initialize handler")
    (merge db {:data-sources {}
               :layout layout
               :options options
               :widgets widgets})))



(rf/register-handler
  :update-data-source
  (fn [app-state [_ data-source new-val]]
    (assoc-in  app-state [:data-sources data-source] new-val)))



(defn- widget-exists? [v m]
  (= (:name m) v))

(rf/register-handler
  :add-widget
  (fn [app-state [_ new-widget]]
    (let [orig (:widgets app-state)
          c (conj orig new-widget)
          ret-val (assoc app-state :widgets (conj (:widgets app-state) new-widget))
          fnd (map (partial widget-exists? (:name new-widget)) orig)]

      ;(.log js/console (str "widget exists? " (:name new-widget) " / " fnd " / " (some true? fnd)))

      (if (some true? fnd)
        (do
          ;(.log js/console (str "not adding the widget " fnd))
          app-state)

        (do
          (.log js/console (str ":add-widget handler " new-widget
                             " //// orig " orig
                             " //// found " fnd
                             " //// new " (:widgets ret-val)))

          ;(.log js/console (str "calling build-widget " new-widget))

          ; HACK!

          ; TODO: hook up subscription

          ; TODO - should registry widget "types" just like chart "types"

          (wb/build-widget new-widget)

          ret-val)))))



(rf/register-handler
  :remove-widget
  (fn [app-state [_ widget-id]]

    (let [ret-val (assoc app-state :widgets (remove #(= (:name %) widget-id) (:widgets app-state)))]

      ;(.log js/console (str ":remove-widget handler " widget-id " //// " (:widgets ret-val)))

      ret-val)))




(rf/register-handler
  :update-widget-layout
  (fn [app-state [_ new-layout]]
    ; TODO - get new layout and update widgets that changed (new-layout)
    app-state))
