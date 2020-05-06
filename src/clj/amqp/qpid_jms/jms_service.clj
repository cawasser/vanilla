(ns amqp.qpid-jms.jms-service
  (:import
    (javax.jms Message DeliveryMode MessageConsumer
               MessageProducer TextMessage Connection
               BytesMessage)
    (org.apache.qpid.jms JmsConnectionFactory JmsQueue
                         JmsSession JmsConnection)))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Config settings - may want to expand this to an edn file
(def connection-url "amqp://0.0.0.0:1111")
(def queue-name "queue")
(def session-mode JmsSession/AUTO_ACKNOWLEDGE)
;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Common obects used throughout this namespace
(def queue (JmsQueue. queue-name))
(def factory (JmsConnectionFactory. connection-url))
;;;;


;;;;;;;;;;;;;;;;;
;; Send functions
; @TODO - so many different things to send, should we make a new function for everything or make it generic?
; @todo - Set up protobuf support
(defn send-bytes
  [bytes]
  (let [conn (.createConnection factory)]
    (.start conn)
    (let [session (.createSession conn false session-mode)
          messageProducer (.createProducer session queue)
          byte-message (.createBytesMessage session)]
      (.writeUTF byte-message (str bytes))
      (.send messageProducer
             byte-message
             DeliveryMode/NON_PERSISTENT
             Message/DEFAULT_PRIORITY
             Message/DEFAULT_TIME_TO_LIVE)
      (.close session))
    (.close conn)))

(defn send-text-message
  [message]
  (let [conn (.createConnection factory)]
    (.start conn)
    (let [session (.createSession conn false session-mode)
          messageProducer (.createProducer session queue)
          text-message (.createTextMessage session message)]
      (.send messageProducer
             text-message
             DeliveryMode/NON_PERSISTENT
             Message/DEFAULT_PRIORITY
             Message/DEFAULT_TIME_TO_LIVE)
      (.close session))
    (.close conn)))
;;;;


;;;;;;;;;;;;;;;;;;;;
;; Receive functions
;@TODO- Right now just prints stuff to console, doesn't even return data
;@TODO - lots of similar code below, might be worth making something reusable
(defn receive-text-message
  []
  (let [conn (.createConnection factory)]
    (.start conn)
    (let [session (.createSession conn false session-mode)
          messageConsumer (.createConsumer session queue)
          rec-message (.receiveNoWait messageConsumer)]
      (prn (.getBody rec-message TextMessage))
      (.close session))
    (.close conn)))

(defn receive-bytes
  []
  (let [conn (.createConnection factory)]
    (.start conn)
    (let [session (.createSession conn false session-mode)
          messageConsumer (.createConsumer session queue)
          byte-message (.receiveNoWait messageConsumer)]
      (prn (.readUTF byte-message))
      (.close session))
    (.close conn)))
;;;;




;;;;;;;;;;;;;
;; REPL Tests

(comment


  (send-bytes "chad")
  (receive-bytes)

  (send-message "chad")
  (receive-message)




  ())



(comment

  ;; connection can be made and defined but has to be closed before it times out in the broker
  ;; @TODO - Would be super helpful to find a way to generically make a connection
  (do
    (def conn (.createConnection factory))
    (.start conn))


  (def session (.createSession conn false session-mode))
  (def sess-que (.createQueue session "test"))
  (def messageConsumer (.createConsumer session sess-que))
  (def rec-message (.receiveNoWait messageConsumer))

  (prn (str (.getBody rec-message TextMessage)))
  (.close session)
  (.close conn))

