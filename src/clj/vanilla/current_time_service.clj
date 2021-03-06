(ns vanilla.current-time-service)


(defn fetch-data []
  (prn "current-time Service")

  {:title "Current Time"
   :data-format :data-format/string
   :text        (.format (java.time.LocalDateTime/now)
                         (java.time.format.DateTimeFormatter/ofPattern "hh:mm:ss"))})



(comment

  (vanilla.subscription-manager/refresh-source :current-time)

  ())