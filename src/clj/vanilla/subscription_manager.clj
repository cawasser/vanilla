(ns vanilla.subscription-manager
  (:require [dashboard-clj.data-source :as ds]
            [dashboard-clj.components.system :as system]
            [vanilla.service-deps :as deps]))

(def subscribed-sources
  (atom {:subscribers {}
         :sources     {}}))

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
  ;(prn "add-empty-user " username)
  (swap! subscribed-sources assoc-in [:subscribers username] {:username username :sources #{}}))



(defn push-data!
  "Gets the websocket send-fn from the system-map to push out the updated data-source"
  [username source]
  (prn "push-data! " username source)
  (let [data-src (ds/new-data-source (get-servicedep source))
        new-data (apply (ds/resolve-fn (:read-fn data-src)) (:params data-src))
        event    (ds/data->event (:name data-src) new-data)]

    ;(prn "Pushing this event: " (first (second event)) " to " username)
    ((get-in @system/system [:websocket :chsk-send!]) username event)))



(defn add-subscribers
  "Updates the subscribed-sources atom with a user's list of sources.
   updates the map of sources with the users who have subscribed
   Executes websocket push to send the latest data to the client"
  [username sources]
  (prn "add-subscribers " username " to " sources)
  (reset! subscribed-sources
    (-> @subscribed-sources
      (assoc-in [:subscribers username :sources]
        (apply merge (get-in @subscribed-sources [:subscribers username :sources] #{}) sources))
      (assoc :sources (apply merge (:sources @subscribed-sources)
                        (map (fn [s]
                               {s (let [v (get-in @subscribed-sources [:sources s])]
                                    (if v
                                      (merge v username)
                                      #{username}))})
                          sources)))))
  (doall (map #(push-data! username %) sources))
  sources)



(defn refresh-source
  "forces a push-data! to all subscribers of the given source (keyword)

  especially useful in the REPL when modifying a data-source 'provider' function"

  [source]
  (for [u (get-in @subscribed-sources [:sources source])]
    (push-data! u source)))



(defn get-subbed-sources
  "Retrieves sources a user is subscribed to"
  [username]
  (:sources (get-in @subscribed-sources [:subscribers username])))



(defn remove-user
  "Removes a user from the subscribed sources atom and the use from any sources
   they had subscribed to"
  [username]
  (reset! subscribed-sources
    (-> @subscribed-sources
      (assoc :subscribers (dissoc (:subscribers @subscribed-sources) username))
      (assoc :sources (apply merge (:sources @subscribed-sources)
                        (map (fn [[k v :as s]]
                               {k (disj v username)})
                          (:sources @subscribed-sources)))))))

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


  (map (fn [s]
         {s "test"})
    [:source-1 :source-2])


  (map (fn [s]
         {s (let [v (get-in @subscribed-sources [:sources s])]
              (if v
                (merge v "test")
                ["test"]))})
    [:source-1 :source-2])

  (get-in @subscribed-sources [:sources :source-1])
  (merge nil "test")

  (-> @subscribed-sources
    (assoc-in [:subscribers "test" :sources] [:source-1 :source-2])
    (assoc :sources (into {} (map (fn [s]
                                    {s (let [v (get-in @subscribed-sources [:sources s])]
                                         (if v
                                           (merge v #{"test"})
                                           #{"test"}))})
                               [:source-1 :source-2]))))

  @subscribed-sources

  (add-empty-user "test")
  (add-subscribers "test" [:repl-test-data :terminal-location-service])

  (swap! subscribed-sources assoc-in [:subscribers username :sources] sources)

  (merge #{"one" "two"} "three")
  (merge #{"one" "two"} "one")

  (for [u (get-in @subscribed-sources [:sources :bubble-service])]
    u)



  (def t-subs (atom {:subscribers {}
                     :sources     {}}))

  (defn funky [username sources]
    (reset! t-subs
      (-> @t-subs
        (assoc-in [:subscribers username :sources]
          (apply merge (get-in @t-subs [:subscribers username :sources] #{}) sources))
        (assoc :sources (apply merge (:sources @t-subs)
                          (map (fn [s]
                                 {s (let [v (get-in @t-subs [:sources s])]
                                      (if v
                                        (merge v username)
                                        #{username}))})
                            sources))))))

  (-> @t-subs
    (assoc-in [:subscribers "chris" :sources]
      (apply merge (get-in @t-subs [:subscribers "chris" :sources] #{}) [:spectrum])))


  (-> @t-subs
    (assoc :sources (apply merge (:sources @t-subs)
                      (map (fn [s]
                             {s (let [v (get-in @t-subs [:sources s])]
                                  (if v
                                    (merge v "chris")
                                    #{"chris"}))})
                        [:spectrum]))))
  @t-subs
  (funky "chris" [:bubble])
  (funky "chris" [:spectrum])
  (funky "dave" [:spectrum])


  ;(defn tasty [username]
  ;  (reset! t-subs
  (-> @t-subs
    (assoc :subscribers (dissoc (:subscribers @t-subs) "chris")))

  (disj (:sources @t-subs) "chris")
  (map (fn [[k v :as s]]
         {k (disj v "chris")})
    (:sources @t-subs))

  (-> @t-subs
    (assoc :subscribers (dissoc (:subscribers @t-subs) "chris"))
    (assoc :sources (apply merge (:sources @t-subs)
                      (map (fn [[k v :as s]]
                             {k (disj v "chris")})
                        (:sources @t-subs)))))

  (remove-user "dave")
  (remove-user "steve")


  ())
  