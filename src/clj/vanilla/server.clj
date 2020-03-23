(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [vanilla.environment]
              [vanilla.edn-queue-source]
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
