(ns vanilla.service-deps)



(def datasources
  [{:name     :spectrum-traces
    :read-fn  :vanilla.fetcher/spectrum-traces
    :schedule {:in    [0 :seconds]
               :every [10 :seconds]}}

   {:name     :usage-data
    :read-fn  :vanilla.fetcher/usage-data}
    ;:schedule {:in    [0 :seconds]
    ;           :every [15 :seconds]}}

   {:name     :current-time
    :read-fn  :vanilla.fetcher/current-time}
    ;:params   []
    ;:schedule {:in    [0 :seconds]
    ;           :every [5 :seconds]}}

   {:name :sankey-service
    :read-fn :vanilla.sankey-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [5 :seconds]}}

   {:name :bubble-service
    :read-fn :vanilla.bubble-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [5 :seconds]}}

   {:name :network-service
    :read-fn :vanilla.network-service/fetch-data}
   ;:schedule {:in    [0 :seconds]
   ;           :every [5 :seconds]}}

   {:name     :power-data
    :read-fn  :vanilla.fetcher/power-data}
    ;:schedule {:in    [0 :seconds]
    ;           :every [30 :seconds]}}

   {:name     :heatmap-data
    :read-fn  :vanilla.fetcher/heatmap-data}
    ;:schedule {:in    [0 :seconds]
    ;           :every [10 :seconds]}}

   {:name :health-and-status-data
    :read-fn :vanilla.stoplight-service/fetch-data}
    ;:schedule {:in    [0 :seconds]
    ;           :every [10 :seconds]}}

   {:name :usage-24-hour-service
    :read-fn :vanilla.usage-24-hour-service/fetch-data}

   {:name :scatter-service-data
    :read-fn :vanilla.scatter-service/fetch-data}])
