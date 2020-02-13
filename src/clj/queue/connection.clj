(ns queue.connection
  (:require [com.stuartsierra.component :as component]
            [bunnicula.component.connection :as connection]
            [bunnicula.component.publisher :as publisher]
            [bunnicula.protocol :as protocol]
            [bunnicula.component.monitoring :as monitoring]
            [bunnicula.component.consumer-with-retry :as consumer]))


(def connection (connection/create {:host "127.0.0.1"
                                    :port 5672
                                    :username "guest"
                                    :password "guest"
                                    :vhost "/main"}))


