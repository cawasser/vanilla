(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [environ.core :refer [env]]
              [vanilla.fetcher]
              [vanilla.sankey-service]
              [vanilla.bubble-service]
              [vanilla.network-service]
              [vanilla.stoplight-service]
              [vanilla.usage-24-hour-service]
              [vanilla.scatter-service]
              [vanilla.usage-24-hour-service]

              [vanilla.service-deps :as deps])

    (:gen-class))



(defn start-dashboard[]
  (prn "server starting")
  (dash/start deps/datasources {:port (Integer. (or (env :port) 5000))
                                :nrepl-port (Integer. (or (env :nrepl-port) 7000))}))

(defn -main [& [port]]
  (start-dashboard))
