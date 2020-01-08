(ns vanilla.app
  (:require [vanilla.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/start-dashboard)