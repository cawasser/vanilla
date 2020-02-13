(ns vanilla.queue-consumer
  (:require [queue.consumer :as qc]
            [vanilla.processing :as proc]))


(defn start-consumer
  [exchange queue handler-fn]
  (if (nil? (qc/create-consumer-for exchange queue handler-fn "edn"))
    false
    true))

