(ns vanilla.beam-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]
            [vanilla.db.materialized-view :as mv]))







(defn fetch-data [band]
  (log/info "Beam Locations" band "band")

  {:title       "Beam Locations"
   :data-format :data-format/lat-lon-e
   :data        (mv/beam-query band)})


(comment

  (vanilla.subscription-manager/refresh-source :x-beam-location-service)
  (vanilla.subscription-manager/refresh-source :ka-beam-location-service)

  ())

