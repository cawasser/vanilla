(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [environ.core :refer [env]]
              [vanilla.fetcher]
              [vanilla.sankey-service]
              [vanilla.bubble-service]
              [vanilla.network-service]
              [vanilla.stoplight-service]
              [vanilla.usage-24-hour-service]
              [vanilla.service-deps :as deps]
              [vanilla.scatter-service]
              [vanilla.usage-24-hour-service])
    (:gen-class))



(defn start-dashboard[]
  (prn "server starting")
  (dash/start deps/datasources {:port (Integer. (or (env :port) 5000))}))

(defn -main [& [port]]
  (start-dashboard))
