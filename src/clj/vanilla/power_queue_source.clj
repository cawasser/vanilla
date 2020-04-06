(ns vanilla.power-queue-source
  (:require [clojure.tools.logging :as log]
            [vanilla.queue-consumer :as qc]
            [vanilla.processing :as proc]
            [dashboard-clj.data-source :as ds]
            [clojure.core.async :as async]
            [dashboard-clj.components.websocket :as ws]))



(defn send-message [data]
  (log/info "sending power-queue-source data UPDATE " data)

  (ws/send-to-all! (ds/data->event :power-queue-service data)))



(defn start-listener [exchange queue]
  (let [power-processing-fn (fn [body parsed envelope components]
                              (send-message parsed)
                            ;(send-message {:title       "Queued Message"
                            ;               :data-format :data-format/string
                            ;               :exchange    exchange
                            ;               :queue       queue
                            ;               :text        parsed})
                            :ack)]

    (log/info "starting power-queue-source LISTENER")

    (qc/start-consumer exchange queue (proc/pwr-handler power-processing-fn))))


