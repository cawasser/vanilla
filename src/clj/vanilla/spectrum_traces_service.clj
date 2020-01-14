(ns vanilla.spectrum-traces-service
  (:require [vanilla.db :as db]))

(defn fetch-data []
  (prn "Spectrum Traces")

  {:title           "Spectrum Traces"
   :data-format     :data-format/y
   :series          (db/spectrum-data)
   :src/chart-title "dB"
   :src/x-title     "frequency"
   :src/y-title     "power"})
