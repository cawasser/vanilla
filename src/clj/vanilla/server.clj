(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [environ.core :refer [env]]
              [vanilla.fetcher])
    (:gen-class))

(def datasources [{:name     :spectrum-traces
                   :read-fn  :vanilla.fetcher/spectrum-traces
                   :schedule {:in    [0 :seconds]
                              :every [2 :seconds]}}

                  {:name     :usage-data
                   :read-fn  :vanilla.fetcher/usage-data
                   :schedule {:in    [0 :seconds]
                              :every [4 :seconds]}}

                  {:name     :current-time
                   :read-fn  :vanilla.fetcher/current-time
                   :params   []
                   :schedule {:in    [0 :seconds]
                              :every [5 :seconds]}}])

(defn start-dashboard[]
  (prn "server starting")
  (dash/start datasources {:port (Integer. (or (env :port) 5000))}))

(defn -main [& [port]]
  (start-dashboard))
