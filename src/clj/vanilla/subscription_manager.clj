(ns vanilla.subscription-manager
  (:require [dashboard-clj.data-source :as ds]
            [vanilla.service-deps :as deps]))

(def subscribed-sources
  (atom {}))

;(def datasource-subscribers
;  (atom []))
;
;(def username-uids
;  (atom []))

(defn get-servicedep [service]
  (->> deps/datasources
       (filter #(= (:name %) service))
       first))


(defn add-empty-user [username]
  (swap! subscribed-sources assoc username {:username username :sources []}))

(defn add-subscribers [username sources]
  ;(prn "Subscribing: " username " to " sources)
  (swap! subscribed-sources assoc-in [username :sources] sources)
  (mapv #(ds/fetch (ds/new-data-source (get-servicedep %))) sources)

  sources)

(defn get-subbed-sources [username]
  (:sources (get @subscribed-sources username)))


(defn remove-user [username]
  (swap! subscribed-sources dissoc username))

;(defn add-user-from-uid [uid]
;  (let [split (str/split uid #"/")
;        username (first split)]
;    (swap! username-uids conj {:username username :uid uid})))

;(defn get-usermap [value]
;  (->> @username-uids
;       (filter #(= (:username %) value))
;       first))




(comment

  (get @ds/data-sources :spectrum-traces)
  (get-servicedep :spectrum-traces)
  (ds/fetch (ds/new-data-source (get-servicedep :spectrum-traces)))

  (ds/fetch
    (ds/new-data-source {:name    :spectrum-traces
                         :read-fn :vanilla.spectrum-traces-service/fetch-data}))

  (mapv #(ds/fetch (ds/new-data-source (get-servicedep %)))
        [:spectrum-traces :bubble-service :australia-map-service])


  (defn assoc-by-fn [data keyfn datum]
    (assoc data (keyfn datum) datum))

  (assoc-by-fn @subscribed-sources :username {:username "test" :sources []})


  (add-empty-user "test")
  (add-subscribers "test" [:spectrum-traces :test-service :australia-map-service])
  (remove-user "test")


  (get-subbed-sources "austin")
  (some #(= :spectrum-traces %) (get-subbed-sources "austin"))


  @subscribed-sources
  (reset! subscribed-sources {})



  ()
  )