(ns vanilla.subscription-manager
  (:require [dashboard-clj.data-source :as ds]
            [dashboard-clj.components.system :as system]
            [vanilla.service-deps :as deps]))

(def subscribed-sources
  (atom {}))

;(def datasource-subscribers
;  (atom []))
;
;(def username-uids
;  (atom []))

(defn get-servicedep
  "Retrieves the service-dep map from the list of datasources"
  [service]
  (->> deps/datasources
       (filter #(= (:name %) service))
       first))


(defn add-empty-user
  "Called when a websocket connection opens to a new client. Adds empty subscription map"
  [username]
  (swap! subscribed-sources assoc username {:username username :sources []}))

(defn push-data!
  "Gets the websocket send-fn from the system-map to push out the updated data-source"
  [username source]
  (let [data-src (ds/new-data-source (get-servicedep source))
        new-data (apply (ds/resolve-fn (:read-fn data-src)) (:params data-src))
        event (ds/data->event (:name data-src) new-data)]

    ;(prn "Pushing this event: " (first (second event)) " to " username)
    ((get-in @system/system [:websocket :chsk-send!]) username event)))

(defn add-subscribers
  "Updates the subscribed-sources atom with a users list of sources.
   Executes websocket push to send the latest data to the client"
  [username sources]
  ;(prn "Subscribing: " username " to " sources)
  (swap! subscribed-sources assoc-in [username :sources] sources)
  (doall (map #(push-data! username %) sources))

  sources)

(defn get-subbed-sources
  "Retrieves sources a user is subscribed to"
  [username]
  (:sources (get @subscribed-sources username)))


(defn remove-user
  "Removes a user from the subscribed sources atom"
  [username]
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

  (apply (ds/resolve-fn (:read-fn (ds/new-data-source (get-servicedep :spectrum-traces)))) nil)
  (push-data! "austin" :network-service)
  (map #(push-data! "test" %) [:spectrum-traces :bubble-service :australia-map-service])

  (ds/fetch
    (ds/new-data-source {:name    :spectrum-traces
                         :read-fn :vanilla.spectrum-traces-service/fetch-data}))

  (mapv #(ds/fetch (ds/new-data-source (get-servicedep %)))
        [:spectrum-traces :bubble-service :australia-map-service])


  (defn assoc-by-fn [data keyfn datum]
    (assoc data (keyfn datum) datum))

  (assoc-by-fn @subscribed-sources :username {:username "test" :sources []})


  (add-empty-user "test")
  (add-subscribers "test" [:spectrum-traces :bubble-service :australia-map-service])
  (remove-user "test")


  (get-subbed-sources "austin")
  (some #(= :spectrum-traces %) (get-subbed-sources "austin"))


  @subscribed-sources
  (reset! subscribed-sources {})



  ()
  )