(ns vanilla.terminal-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.materialized-view :as mv]))






(defn fetch-data []
  (log/info "Terminal Locations")

  {:title "Terminal Locations"
   :data-format :data-format/cont-n
   :data (mv/terminal-location-query)})



(comment

  (take 5 (mv/terminal-location-query))

  (vanilla.subscription-manager/refresh-source :terminal-location-service)

  ())