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

;
; Use this function to create a Combo Chart
; Note that it is pulling info from two places
;
(defn fetch-entity3 []
  (prn "fetching entity3")

  {:title "Resource Consumption"
   :entity3-data (merge (db/entity3-data-total) db/entity3-data-by-consumer)})

;
; Use this function to create an Area Chart
;
(defn fetch-entity4 []
  (prn "fetching entity4")

  {:title "Resource Capacity"
   :entity4-data (db/entity4-data)})
