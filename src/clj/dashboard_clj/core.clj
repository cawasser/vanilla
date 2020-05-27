(ns dashboard-clj.core
  (:require [dashboard-clj.system :as system]
            [dashboard-clj.data-source :as ds]
            [vanilla.environment :refer [environment]]))


(defn start [ds-maps]

  (let [port (Integer. (or (environment :port) 5000))
        nrepl (not (environment :prod))
        nrepl-port (Integer. (or (environment :nrepl-port) 7000))
        dev-mode? (environment :dev)]

    (reset! ds/data-sources
      (apply merge
        (map (fn [x]
               {(:name x) (ds/new-data-source x)})
             ds-maps)))

    (system/start port nrepl nrepl-port (vals @ds/data-sources) dev-mode?)))
