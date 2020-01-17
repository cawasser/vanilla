(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [vanilla.environment]
              [vanilla.fetcher]

              [vanilla.bubble-service]
              [vanilla.heatmap-service]
              [vanilla.network-service]
              [vanilla.sankey-service]
              [vanilla.scatter-service]
              [vanilla.stoplight-service]
              [vanilla.current-time-service]
              [vanilla.grid-service]
              [vanilla.usage-24-hour-service]

              [vanilla.service-deps :as deps])

    (:gen-class))



(defn start-dashboard[]
  (prn "server starting")
  (dash/start deps/datasources))

(defn -main [& [port]]
  (start-dashboard))
