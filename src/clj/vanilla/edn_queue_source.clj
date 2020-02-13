(ns vanilla.edn-queue-source
  (:require [clojure.tools.logging :as log]
            [vanilla.queue-consumer :as qc]
            [vanilla.processing :as proc]))



(defn send-message [data]
  (log/info "sending edn-queue-source data UPDATE " (:text data)))

; TODO: how do we hook into the websocket?
;(ws/send-to-all! parsed)



(defn start-listener [exchange queue]
  (let [edn-processing-fn (fn [body parsed envelope components]
                            (log/info "sending edn-queue-source data UPDATE")

                            (send-message {:title       "Queued Message"
                                           :data-format :data-format/string
                                           :exchange    exchange
                                           :queue       queue
                                           :text        parsed})
                            :ack)]

    (log/info "starting edn-queue-source LISTENER")

    (qc/start-consumer exchange queue (proc/edn-handler edn-processing-fn))))


