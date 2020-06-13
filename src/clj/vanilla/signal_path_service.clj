(ns vanilla.signal-path-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [vanilla.db.materialized-view :as mv]
            [datascript.core :as d]))




(defn fetch-data []
  (log/info "Signal Path Service")

  {:title       "Signal Path Data - Grouped"
   :data-format :data-format/grouped-from-to-n
   :series (mv/signal-path-query)})




(comment
  (take 1 (mv/signal-path-query))

  (vanilla.subscription-manager/refresh-source :signal-path-service)

  ())

