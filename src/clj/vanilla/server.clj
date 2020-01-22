(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [vanilla.environment]

              [vanilla.bubble-service]
              [vanilla.heatmap-service]
              [vanilla.network-service]
              [vanilla.sankey-service]
              [vanilla.scatter-service]
              [vanilla.stoplight-service]
              [vanilla.usage-12-hour-service]
              [vanilla.spectrum-traces-service]
              [vanilla.usage-data-service]
              [vanilla.power-data-service]
              [vanilla.current-time-service]
              [vanilla.table-service]

              [vanilla.service-deps :as deps])

    (:gen-class))



(defn start-dashboard[]
  (prn "server starting")
  (dash/start deps/datasources))

(defn -main [& [port]]
  (start-dashboard))
