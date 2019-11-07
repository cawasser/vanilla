(ns vanilla.events
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]))


;;;;;;;;;;;;;;;;;;;;;;;
;
; NOTE!
;
; this is a fairly old version of re-frame (0.7.0), driven by
; the dashboard-clj code, which itself, pretty old. We are
; continuing to use this version to simplify the functional
; upgrades we plan to make to dashboard-clj. Adding more re-frame
; is currently a lower priority.
;
; Unfortunately, in the meantime, if you look at the current re-frame docs
; you will find code that DOES NOT WORK in 0.7.0. re-frame has undergone
; significant changes between 0.7.0 and 0.10.x (as of 2019-11-03). Be Aware!
;
; When we finally upgrade re-frame, this notice should be removed.
;
;;;;;;;;;;;;;;;;;;;;;;;





; subscriptions

(rf/register-sub
  :version
  (fn [db _]
    ;(.log js/console (str ":version " @db))
    (reaction (get @db :version))))


(rf/register-sub
  :services
  (fn [db _]
    ;(.log js/console (str ":services " @db))
    (reaction (get @db :services))))



; handlers


(rf/register-handler
  :set-version
  (fn [db [_ version]]
    ;(.log js/console (str ":set-version " version))
    (assoc db :version (:version version))))


(rf/register-handler
  :set-services
  (fn [db [_ services]]
    ;(.log js/console (str ":set-services " services))
    (assoc db :services (:services services))))

