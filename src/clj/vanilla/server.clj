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
              [vanilla.continent-map-service]
              [vanilla.australia-map-service]
              [vanilla.arearange-service]
              [vanilla.energy-use-service]
              [vanilla.task-service]
              [vanilla.carousel-service]

              [vanilla.service-deps :as deps]
              [clojure.tools.logging :as log]
              [trptcolin.versioneer.core :as version])

    (:gen-class))



(defn start-dashboard[]
  (log/info "server starting version: " (version/get-version
                                          "vanilla"
                                          "vanilla"
                                          "version number not found"))
  (dash/start deps/datasources))

(defn -main [& [port]]
  (start-dashboard))
