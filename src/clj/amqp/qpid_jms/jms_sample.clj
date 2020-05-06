(ns amqp.qpid-jms.jms-sample
  (:import
    (javax.jms DeliveryMode Message)
    (org.apache.qpid.jms JmsQueue JmsSession JmsConnectionFactory)))
    






(def queue-name "queue")
(def queue (JmsQueue. queue-name))
(def connection-url "tcp://0.0.0.0:1111")



(defn create-factory
  []
  (let [factory (JmsConnectionFactory. connection-url)]
    factory))


(defn create-and-close-connection
  [factory]
  (let [connection (.createConnection factory)]
    (.start connection)
    (let [session (.createSession connection false JmsSession/AUTO_ACKNOWLEDGE)
          messageProd (.createProducer session queue)
          messageConsumer (.createConsumer session queue)
          test-message (.createTextMessage session "hello")]
      (.send messageProd
             test-message
             DeliveryMode/NON_PERSISTENT
             Message/DEFAULT_PRIORITY
             Message/DEFAULT_TIME_TO_LIVE)
      (let [rec-message (.receive messageConsumer 2000)]
        (prn (.getText rec-message))))
    (.close connection)))

(defn run-it-all []
  (let [factory (create-factory)]
    (create-and-close-connection factory)))

(run-it-all)


