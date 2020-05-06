(ns vanilla.subscription-manager
  (:require [clojure.string :as str] ))

(def subscribed-sources
  (atom {}))

(def datasource-subscribers
  (atom []))

(def username-uids
  (atom []))



(defn add-empty-user [username]
  (swap! subscribed-sources assoc username {:username username :sources []}))

(defn add-subscribers [username sources]
  (prn "Subscribing: " username " to " sources)
  (swap! subscribed-sources assoc-in [username :sources] sources)
  true)

(defn get-subbed-sources [username]
  (:sources (get @subscribed-sources username)))


;(defn add-user-from-uid [uid]
;  (let [split (str/split uid #"/")
;        username (first split)]
;    (swap! username-uids conj {:username username :uid uid})))

;(defn get-usermap [value]
;  (->> @username-uids
;       (filter #(= (:username %) value))
;       first))




(comment

  (defn assoc-by-fn [data keyfn datum]
    (assoc data (keyfn datum) datum))

  (assoc-by-fn @subscribed-sources :username {:username "test" :sources []})

  (add-empty-user "test")
  (add-subscribers "test" [:spectrum-traces :test-service :australia-map-service])

  (get-subbed-sources "austin")

  (contains? (get-subbed-sources "austin") :spectrum-traces)
  (some #(= :spectru-traces %) (get-subbed-sources "austin"))



  @subscribed-sources
  (reset! subscribed-sources {})



  ()
  )