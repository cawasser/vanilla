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
            [trptcolin.versioneer.core :as version]
            [vanilla.db.excel-data :as excel]
            [vanilla.db.sv-telemetry :as telemetry]
            [vanilla.db.materialized-view :as mv])

  (:gen-class))



(defn start-dashboard []
  (log/info "server starting version: " (version/get-version
                                          "vanilla"
                                          "vanilla"
                                          "version number not found"))
  (excel/init-from-excel excel/filename excel/excel-defs)
  (telemetry/init-telemetry telemetry/filename telemetry/column-map)
  (mv/get-state)

  (dash/start deps/datasources))

(defn -main [& [port]]
  (start-dashboard))
