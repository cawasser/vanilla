(ns dashboard-clj.core
  (:require-macros [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [taoensso.sente  :as sente :refer (cb-success?)]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [goog.object :as gobject]
            [vanilla.subscriptions]))

(rf/reg-event-db
  :update-data-source
  (fn-traced
    [app-state [_ data-source new-val]]
    (assoc-in  app-state [:data-sources data-source] new-val)))

(def ?csrf-token js/csrfToken)

; For debugging, no need to print the csrf token to the browser console
;(if ?csrf-token
;  (prn "CSRF token detected in HTML, great! " ?csrf-token)
;  (prn "CSRF token NOT detected in HTML, default Sente config will reject requests "))

; used to pull apart :last-ws-error value -> #object[Event [object Event]] that js-clj returned nil for
(defn obj->clj [x]
  (reduce (fn [r k] (assoc r k (gobject/get x k)))
          {}
          (js-keys x)))


(defn connect-to-data-sources []
  (let [{:keys  [chsk ch-recv send-fn state]} (sente/make-channel-socket-client! "/chsk"
                                                                                  ?csrf-token
                                                                                  {:type :auto
                                                                                   :client-id @(rf/subscribe [:get-current-user])})]

    (async/go-loop []
      (let [{:keys [event id ?data send-fn]} (async/<! ch-recv)]

        ;(prn "received some data " ?data)
        ;  " //// id " id
        ;  " //// data " ?data)

        ; This was used to debug a websocket handshake error.  Leaving for future reference
        ; the obj-clj function was hard to find and worked when js-clj didnt.
        ;(when (= id :chsk/state)
        ;  (prn "Event Object:  " (obj->clj (get-in (get ?data 1) [:last-ws-error :ev]))))

        (when (= (get ?data 0) :data-source/event)
          (let [[_ [ds-name ds-data]] ?data]
            ;(prn "received data " ds-name)
            (rf/dispatch [:update-data-source ds-name ds-data])))

        (when (and
               (= id :chsk/state)
               (= (:first-open? ?data) true))
          ;(prn "first opening websocket " ?data)
          (send-fn [:dashboard-clj.core/sync])))
      (recur))))
