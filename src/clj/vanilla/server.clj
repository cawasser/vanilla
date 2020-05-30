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
            [vanilla.db.excel-data :as excel])

  (:gen-class))



(defn start-dashboard []
  (log/info "server starting version: " (version/get-version
                                          "vanilla"
                                          "vanilla"
                                          "version number not found"))
  (excel/init-from-excel excel/filename excel/excel-defs)

  (dash/start deps/datasources))

(defn -main [& [port]]
  (start-dashboard))



(comment
  (use 'etaoin.api)
  (require '[etaoin.keys :as k])

  (def driver (chrome))
  (def driver (firefox))

  (go driver
    "localhost:5000")
  (wait-visible driver
    {:tag :button :fn/text "Login"})


  (click driver
    {:tag :button :fn/text "Login"})
  (wait-visible driver {:tag :button :fn/text "Signup"})

  (fill driver
    {:tag :input :name :username}
    "chris")
  (fill driver
    {:tag :input :name :password}
    "1234")
  (click driver
    {:tag :button :id :do-login})


  (screenshot driver "etaoin/test/post-login.png")


  (click driver
    {:tag :button :fn/text "Add Source"})

  (click driver
    {:tag :button :fn/text "Logout"})
  (click driver
    {:tag :button :id :do-logout})

  (get-url driver)

  (quit driver)

  ())