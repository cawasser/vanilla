(ns dashboard-clj.core
  (:require [dashboard-clj.system :as system]
            [dashboard-clj.data-source :as ds]
            [vanilla.environment :refer [environment]]))


(defn start [ds-maps]

  (let [data-sources (map #(ds/new-data-source %) ds-maps)
        port (Integer. (or (environment :port) 5000))
        nrepl (not (environment :prod))
        nrepl-port (Integer. (or (environment :nrepl-port) 7000))]

    (system/start port nrepl nrepl-port data-sources)))
