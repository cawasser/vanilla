(ns vanilla.fetcher)

(defn fetch []
  {:title "Welcome"
   :text "Here's some text for you"})

(defn current-time []
  {:title "Time"
   :text  (.format (java.time.LocalDateTime/now) (java.time.format.DateTimeFormatter/ofPattern "hh:mm:ss"))})
