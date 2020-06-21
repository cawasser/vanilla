(ns vanilla.beam-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]
            [vanilla.db.materialized-view :as mv]))




;
; TODO: move code to beam-location-service
;
(defn get-beam-data
  "get all beams of a give band, by epoch

   - band  - a string value of the :band of interest

   returns a sequence of maps:

    ({:name <epoch-id>
      :data #{{:name <beam-id> :lat <lat> :lon <lon>
               :e {:diam <diam> :purpose <data-format>}}})

   see (query-thread) for additional information"

  [band]
  (mv/query-thread
    {:q-fn   (d/q '[:find [(pull ?e [*]) ...]
                    :where [?e :band ?band]
                    [?e :beam-id ?name]
                    :in $ ?band] @mv/conn band)
     :map-fn (map (fn [{:keys [epoch band beam-id lat lon radius beam-type satellite-id]}]
                    {:epoch        epoch
                     :name         (str (condp = satellite-id
                                          mv/sat-1 "1"
                                          mv/sat-2 "2"
                                          :default "?")
                                     "-" band "-" beam-id)
                     :satellite-id satellite-id
                     :lat          lat :lon lon
                     :e            {:diam (* radius 2) :purpose beam-type}}))}))



(defn fetch-data [band]
  (log/info "Beam Locations" band "band")

  {:title       "Beam Locations"
   :data-format :data-format/lat-lon-e
   :data        (get-beam-data band)})                      ;(mv/beam-query band)})


(comment

  (get-beam-data "Ka")

  (vanilla.subscription-manager/refresh-source :x-beam-location-service)
  (vanilla.subscription-manager/refresh-source :ka-beam-location-service)

  ())

