(ns vanilla.fetcher
  (:require [vanilla.db :as db]))

(defn fetch []
  (prn "Spectrum Traces")

  {:title "Spectrum Traces"
   :spectrum-data db/spectrum-data})



(defn current-time []
  (prn  "current-time service")

  {:title "Time"
   :text  (.format (java.time.LocalDateTime/now)
                   (java.time.format.DateTimeFormatter/ofPattern "hh:mm:ss"))})
