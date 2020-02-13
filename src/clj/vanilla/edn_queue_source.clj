(ns vanilla.edn-queue-source
  (:require [clojure.tools.logging :as log]
            [vanilla.queue-consumer :as qc]
            [vanilla.processing :as proc]
            [dashboard-clj.data-source :as ds]
            [clojure.core.async :as async]
            [dashboard-clj.components.websocket :as ws]))



(defn send-message [data]
  (log/info "sending edn-queue-source data UPDATE " data)

  ; TODO: how do we hook into the websocket?
  (ws/send-to-all! (ds/data->event :edn-queue-data data)))



(defn start-listener [exchange queue]
  (let [edn-processing-fn (fn [body parsed envelope components]
                            (send-message {:title       "Queued Message"
                                           :data-format :data-format/string
                                           :exchange    exchange
                                           :queue       queue
                                           :text        parsed})
                            :ack)]

    (log/info "starting edn-queue-source LISTENER")

    (qc/start-consumer exchange queue (proc/edn-handler edn-processing-fn))))


