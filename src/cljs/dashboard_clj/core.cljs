(ns dashboard-clj.core
  (:require-macros [reagent.ratom :refer [reaction]]
                   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [reagent.core :as r :refer [atom]]
            [cljs.core.async :as async :refer (<! >! put! chan)]
            [taoensso.sente  :as sente :refer (cb-success?)]
            [cljsjs.react-grid-layout]
            [dashboard-clj.layouts.core :as layout]
            [dashboard-clj.subscriptions :as subs]
            [re-frame.core :as rf]
            [vanilla.events]))


(defn connect-to-data-sources []
  (let [{:keys  [chsk ch-recv send-fn state]} (sente/make-channel-socket! "/chsk" {:type :auto})]
    (asyncm/go-loop []
      (let [{:keys [event id ?data send-fn]} (async/<! ch-recv)]
        (when (= (get ?data 0) :data-source/event)
          (let [[_ [ds-name ds-data]] ?data]
            (rf/dispatch [:update-data-source ds-name ds-data])))
        (when (and
               (= id :chsk/state)
               (= (:first-open? ?data) true))
          (send-fn [:dashboard-clj.core/sync])))
      (recur))))


(defn start-dashboard[dashboard element_id]
  (rf/dispatch-sync [:initialize])
  (subs/register-global-app-state-subscription) ; TODO - eliminate register-global-app-state-subscription
  (connect-to-data-sources))


  ;(let [new-layout (layout/setup-layout (get dashboard :layout) dashboard)]
  ;  (r/render new-layout (.getElementById js/document element_id))))
