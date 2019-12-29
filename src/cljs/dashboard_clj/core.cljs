(ns dashboard-clj.core
  (:require-macros [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [taoensso.sente  :as sente :refer (cb-success?)]
            [re-frame.core :as rf]
            [vanilla.events]
            [vanilla.subscriptions]))


(defn connect-to-data-sources []
  (let [{:keys  [chsk ch-recv send-fn state]} (sente/make-channel-socket! "/chsk" {:type :auto})]

    (asyncm/go-loop []
      (let [{:keys [event id ?data send-fn]} (async/<! ch-recv)]

        ;(prn "received some data " event
        ;  " //// id " id
        ;  " //// data " ?data)

        (when (= (get ?data 0) :data-source/event)
          (let [[_ [ds-name ds-data]] ?data]
            (prn "received data " ds-name)
            (rf/dispatch [:update-data-source ds-name ds-data])))
        (when (and
               (= id :chsk/state)
               (= (:first-open? ?data) true))
          (prn "first opening " ?data)
          (send-fn [:dashboard-clj.core/sync])))
      (recur))))


