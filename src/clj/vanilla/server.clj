(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [environ.core :refer [env]]
              [vanilla.fetcher]
              [vanilla.sankey-service]
              [vanilla.bubble-service]
              [vanilla.network-service]
              [vanilla.stoplight-service])
    (:gen-class))

(def datasources [
                  {:name     :spectrum-traces
                   :read-fn  :vanilla.fetcher/spectrum-traces
                   :schedule {:in    [0 :seconds]
                              :every [2 :seconds]}
                   }

                  {:name     :usage-data
                   :read-fn  :vanilla.fetcher/usage-data
                   :schedule {:in    [0 :seconds]
                              :every [4 :seconds]}
                   }

                  {:name     :current-time
                   :read-fn  :vanilla.fetcher/current-time
                   :params   []
                   :schedule {:in    [0 :seconds]
                              :every [5 :seconds]}
                   }

                  {:name :sankey-service
                   :read-fn :vanilla.sankey-service/fetch-data}
                   :schedule {:in    [0 :seconds]
                              :every [5 :seconds]}
                  }

                  {:name :bubble-service
                   :read-fn :vanilla.bubble-service/fetch-data}
                   :schedule {:in    [0 :seconds]
                              :every [5 :seconds]}
}

                  {:name :network-service
                   :read-fn :vanilla.network-service/fetch-data}
                   :schedule {:in    [0 :seconds]
                              :every [5 :seconds]}
}

                  {:name     :power-data
                   :read-fn  :vanilla.fetcher/power-data
                   :schedule {:in    [0 :seconds]
                              :every [3 :seconds]}
                   }

                  {:name :health-and-status-data
                   :read-fn :vanilla.stoplight-service/fetch-data
                   :schedule {:in    [0 :seconds]
                              :every [10 :seconds]}
                   }

                  {:name     :heatmap-data
                   :read-fn  :vanilla.fetcher/heatmap-data
                   :schedule {:in    [0 :seconds]
                              :every [3 :seconds]}
                   }])


(defn start-dashboard[]
  (prn "server starting")
  (dash/start datasources {:port (Integer. (or (env :port) 5000))}))

(defn -main [& [port]]
  (start-dashboard))
