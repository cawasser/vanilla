(ns vanilla.fetcher
  (:require [vanilla.db :as db]))

(defn spectrum-traces []
  (prn "Spectrum Traces")

  {:title           "Spectrum Traces"
   :data-format     :data-format/x-y
   :series          (db/spectrum-data)
   :src/chart-title "dB"
   :src/x-title     "frequency"
   :src/y-title     "power"})


(defn usage-data []
  (prn "Usage Data")

  {:title       "Usage Data"
   :data-format :data-format/name-y
   :src/x-title "Fruit"
   :src/y-title "Qty."
   :series      (db/usage-data)})

(defn current-time []
  (prn "current-time service")

  {:title       "Time"
   :data-format :data-format/string
   :text        (.format (java.time.LocalDateTime/now)
                         (java.time.format.DateTimeFormatter/ofPattern "hh:mm:ss"))})



(defn power-data []
  (prn "Power Data")

  {:title       "Power Data"
   :data-format :data-format/label-y
   :series      (db/power-data)})

(defn heatmap-data []
  (prn "Heatmap Data")

  {:title       "Heatmap Data"
   :data-format :data-format/x-y-n
   :series      (db/heatmap-data)})

;
; Use this function to create a Combo Chart
; Note that it is pulling info from two places
;
(defn fetch-entity3 []
  (prn "fetching entity3")

  {:title       "Resource Consumption"
   :data-format #{:data-format/summary :data-format/x-y}
   :series      (merge (db/entity3-data-total) db/entity3-data-by-consumer)})

;
; Use this function to create an Area Chart
;
(defn fetch-entity4 []
  (prn "fetching entity4")

  {:title       "Resource Capacity"
   :data-format :data-format/x
   :series      (db/entity4-data)})
