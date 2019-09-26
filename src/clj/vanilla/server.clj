(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [environ.core :refer [env]]
              [vanilla.fetcher])
    (:gen-class))

(def datasources [{:name     :welcome-message
                   :read-fn  :vanilla.fetcher/fetch}
                  {:name     :current-time
                   :read-fn  :vanilla.fetcher/current-time
                   :params   []
                   :schedule {:in    [0 :seconds]
                              :every [5 :seconds]}}])

(defn start-dashboard[]
  (dash/start datasources {:port (Integer. (or (env :port) 5000))}))

(defn -main [& [port]]
  (start-dashboard))
