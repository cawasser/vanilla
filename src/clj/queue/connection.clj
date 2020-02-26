(ns queue.connection
  (:require [com.stuartsierra.component :as component]
            [bunnicula.component.connection :as connection]
            [bunnicula.component.publisher :as publisher]
            [bunnicula.protocol :as protocol]
            [bunnicula.component.monitoring :as monitoring]
            [bunnicula.component.consumer-with-retry :as consumer]
            [vanilla.environment :refer [environment]]))

(defonce conn-atom (atom ()))

(defn connection []
  (if (empty? @conn-atom)
    (do
      (prn "opening rabbitmq connection")
      (reset! conn-atom (connection/create {:host (environment :rabbit-host)
                                            :port (environment :rabbit-port)
                                            :username (environment :rabbit-username)
                                            :password (environment :rabbit-password)
                                            :vhost (environment :rabbit-vhost)}))))

  (prn "returning rabbitmq connection host: " (:host @conn-atom)
       "/// port: " (:port @conn-atom)
       "/// username: " (:username @conn-atom)
       "/// pw: " (:password @conn-atom)
       "/// vhost: " (:vhost @conn-atom))
  @conn-atom)