(ns vanilla.service-deps
  (:require [vanilla.bubble-service]
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
            [vanilla.terminal-location-service]
            [vanilla.signal-path-service]
            [vanilla.beam-location-service]))

(def empty-sources
  (atom []))

(def datasources
  [{:name    :spectrum-traces
    :read-fn :vanilla.spectrum-traces-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [10 :seconds]}}

   {:name    :terminal-location-service
    :read-fn :vanilla.terminal-location-service/fetch-data}

   {:name    :beam-location-service
    :read-fn :vanilla.beam-location-service/fetch-data}

   {:name :task-service
    :read-fn :vanilla.task-service/fetch-data}

   {:name    :signal-path-service
    :read-fn :vanilla.signal-path-service/fetch-data}

   {:name    :usage-data
    :read-fn :vanilla.usage-data-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [15 :seconds]}}

   {:name    :energy-use-service
    :read-fn :vanilla.energy-use-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [15 :seconds]}}

   {:name    :sankey-service
    :read-fn :vanilla.sankey-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [5 :seconds]}}

   {:name    :bubble-service
    :read-fn :vanilla.bubble-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [5 :seconds]}}

   {:name    :network-service
    :read-fn :vanilla.network-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [5 :seconds]}}

   {:name    :power-data
    :read-fn :vanilla.power-data-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [30 :seconds]}}

   {:name    :heatmap-data
    :read-fn :vanilla.heatmap-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [10 :seconds]}}

   {:name    :health-and-status-data
    :read-fn :vanilla.stoplight-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [10 :seconds]}}

   {:name    :usage-12-hour-service
    :read-fn :vanilla.usage-12-hour-service/fetch-data}

   {:name    :scatter-service-data
    :read-fn :vanilla.scatter-service/fetch-data}

   {:name    :current-time
    :read-fn :vanilla.current-time-service/fetch-data}

   {:name    :table-service
    :read-fn :vanilla.table-service/fetch-data}

   {:name    :continent-map-service
    :read-fn :vanilla.continent-map-service/fetch-data}

   {:name    :australia-map-service
    :read-fn :vanilla.australia-map-service/fetch-data}

   {:name    :arearange-service
    :read-fn :vanilla.arearange-service/fetch-data}

   {:name :carousel-service
    :read-fn :vanilla.carousel-service/fetch-data}])


;:params   []
;:schedule {:in    [0 :seconds]
;           :every [5 :seconds]}}])
