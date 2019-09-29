(ns vanilla.fetcher
  (:require [vanilla.db :as db]))

(defn fetch []
  (prn "fetch service")

  {:title "Welcome"
   :text "Here's some text for you"
   :spectrum-data db/spectrum-data})

(defn current-time []
  (prn  "current-time service")

  {:title "Time"
   :text  (.format (java.time.LocalDateTime/now)
                   (java.time.format.DateTimeFormatter/ofPattern "hh:mm:ss"))})
