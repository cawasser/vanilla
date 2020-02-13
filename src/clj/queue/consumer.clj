(ns queue.consumer
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [bunnicula.component.consumer-with-retry :as consumer]
            [bunnicula.component.monitoring :as monitoring]
            [queue.connection :as conn]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; PRIVATE STUFF
;

(def consumers (atom {}))


(defn- consumer-name [exchange queue]
  (str exchange "/" queue))


(defn- register-consumer
  "adds the 'service' to the 'consumers' atom, keyed by 'exchange', so we can find and reuse it later

   exchange - the name of the exchange handled by this consumer
   service - the 'stuart sierra component' that is managing this exchange"
  [exchange queue service]
  (swap! consumers assoc (consumer-name exchange queue) service))


(defn- find-consumer-for [exchange queue]
  (get @consumers (consumer-name exchange queue)))


(defn- create-consumer-service
  "creates the 'stuart sierra component' that manages and executes the consumer

   consumer - the bunnicula.consumer to turn into a 'stuart sierra component' for later use

   return - 'stuart sierra component' for later use"
  [consumer]
  (component/system-map
    :rmq-connection conn/connection
    :monitoring monitoring/BaseMonitoring
    :consumer (component/using
                consumer
                [:rmq-connection :monitoring])))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; support different serializers
;

(defmulti create-consumer (fn [exchange queue handler-fn msg_type] msg_type))

(defmethod create-consumer "edn" [exchange queue handler-fn msg_type]
  (consumer/create {:message-handler-fn handler-fn
                    :options            {:queue-name               queue
                                         :exchange-name            exchange
                                         :timeout-seconds          120
                                         :backoff-interval-seconds 60
                                         :consumer-threads         4
                                         :max-retries              3}}))

(defmethod create-consumer "pb" [exchange queue handler-fn msg_type]
  (consumer/create {:message-handler-fn handler-fn
                    :deserializer       (fn [m] m)
                    :options            {:queue-name               queue
                                         :exchange-name            exchange
                                         :timeout-seconds          120
                                         :backoff-interval-seconds 60
                                         :consumer-threads         4
                                         :max-retries              3}}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; PUBLIC INTERFACE

(defn create-consumer-for
  "create a consumer for a given exchange. creates one, of the given msg_type, if one is not
   found in the registry. if no msg_type provided, defaults to 'edn'

   exchange        - the name of the exchange. this is used to find (or create) the relevant publisher component
   queue           - name of the queue to consume from
   hander-fn       - the function that will be called for every message received from the exchange/queue
   msg_type (opt.) - a string denoting the type of decoding for the message content of this consumer

   returns - the 'stuart sierra component' used to consume the queue's content

   NOTE: the consumer component is automatically 'started' by this call if it needs to create the
   consumer, so you DON'T need to start it manually"

  ([exchange queue handler-fn msg-type]
   (let [p   (find-consumer-for exchange queue)
         typ (if (nil? msg-type) "edn" msg-type)]
     (try
       (if (not p)
         (let [new-p          (create-consumer exchange queue handler-fn typ)
               service        (create-consumer-service new-p)
               started-server (component/start-system service)]
           (register-consumer exchange queue started-server)
           started-server)
         p)
       (catch Exception e (do
                            (log/debug "Can't connect consumer: " (.getMessage e))
                            ())))))

  ([exchange queue handler-fn] (create-consumer-for exchange queue handler-fn "edn")))

(defn stop-consumer-for [exchange queue]
  (if-let [consumer (find-consumer-for exchange queue)]
    (component/stop-system consumer)))

(defn remove-consumer-for [exchange queue]
  (if-let [consumer (find-consumer-for exchange queue)]
    (reset! consumers (dissoc @consumers (consumer-name exchange queue)))))

(defn stop-and-remove-consumer-for [exchange queue]
  (stop-consumer-for exchange queue)
  (remove-consumer-for exchange queue))

(defn stop-and-remove-all-consumers []
  (do (map (fn [[k v]]
             (component/stop-system v)
             (swap! consumers dissoc k))
        @consumers)))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; RICH COMMENTS for the REPL
;

;
; edn messages
;
(comment

  (def exchange "my-exchange")
  (def queue "some.queue")
  (def typ "edn")
  (def msg-type "edn")
  (def handler-fn edn-handler)

  (find-consumer-for exchange queue)

  (create-consumer exchange queue handler-fn typ)
  (def new-p (create-consumer exchange queue handler-fn typ))

  (create-consumer-service new-p)
  (def service (create-consumer-service new-p))

  (def started-server (component/start-system service))

  (register-consumer exchange queue started-server)
  (stop-consumer-for exchange queue)
  (remove-consumer-for exchange queue)
  @consumers


  (create-consumer-for "my-exchange" "some.queue" edn-handler)


  @edn-messages-received
  (reset! edn-messages-received [])

  (stop-and-remove-consumer-for exchange queue)
  (stop-and-remove-consumer-for "my-exchange" "some.queue")

  ())


;
; protobuf messages
;
(comment
  (def exchange "pb-exchange")
  (def queue "person.queue")
  (def typ "pb")
  (def handler-fn (h/pb-handler "Person"))

  (create-consumer-for exchange queue handler-fn typ)
  (create-consumer-for exchange "message.queue" (h/pb-handler "Message") typ)

  @h/messages-received

  (stop-and-remove-all-consumers)

  ())

