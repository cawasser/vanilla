(ns vanilla.fetcher
  (:require [vanilla.db :as db]))

(defn spectrum-traces []
  (prn "Spectrum Traces")

  {:title "Spectrum Traces"
   :spectrum-data (db/spectrum-data)})

(defn usage-data []
  (prn "Usage Data")

  {:title "Usage Data"
   :usage-data (db/usage-data)})

(defn current-time []
  (prn  "current-time service")

  {:title "Time"
   :text  (.format (java.time.LocalDateTime/now)
                   (java.time.format.DateTimeFormatter/ofPattern "hh:mm:ss"))})



(defn power-data []
   (prn "Power Data")

   {:title "Power Data"
    :usage-data (db/power-data)})

(defn heatmap-data []
   (prn "Heatmap Data")

   {:title "Heatmap Data"
    :usage-data (db/heatmap-data)})
