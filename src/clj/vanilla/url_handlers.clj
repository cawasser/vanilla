(ns vanilla.url-handlers
  (:require
    [vanilla.db.core :as db]
    [vanilla.subscription-manager :as subman]
    [clojure.core]))


;;;;;;;;;;;;;;;;
;
; HTML endpoint handlers

(defn get-services
  "Retrieves full service list from the db"
  []
  (db/get-services db/vanilla-db))

(defn subscribe-to-services
  "Handles subscribing a user to the vector of data sources currently on their dashboard"
  [user services]

  (let [distinct-str (into [] (distinct (clojure.core/read-string services)))]
    ;(prn "URL handler subscribing: " distinct-str)
    (subman/add-subscribers user (subman/clean-sources distinct-str))))


(def from-sqlite {:id :key :data_source :data-source :data_grid :data-grid})
(def to-sqlite {:key :id :data-source :data_source :data-grid :data_grid})


; fixup the key changes caused by SQLite with rename-keys
; :id -> :key
; :data_source -> :data-source
; :data_grid -> :data-grid
(defn get-layout
  "Retrieves a users layout from the db"
  [user]
  (map #(clojure.set/rename-keys % from-sqlite) (db/get-user-layout db/vanilla-db {:username user})))


(defn save-layout
  "Saves a users entire widget layout to the db"
  [new-layout]
  ;(prn "SAVE-LAYOUT Handler: " new-layout)
  ;read in the stringed json, order the maps by keys, then remove all keys
  (let [ordered (map #(vals %) (map #(into (sorted-map) %) (clojure.core/read-string new-layout)))]
    ;(prn "SAVE-LAYOUT Handler: " ordered)
    (db/save-layout! db/vanilla-db {:layout ordered})))

(defn update-widget
  "Updates a single widget for a user"
  [widget]
  (db/create-layout! db/vanilla-db (assoc (clojure.set/rename-keys (clojure.core/read-string widget) to-sqlite) :username "testHuman")))

(defn delete-widget
  "Removes a widget by its uuid"
  [id]
  (db/delete-layout! db/vanilla-db {:id id}))


(defn create-user
  "Makes a call to the hugsql functions that create a user"
  [credentials]
  ;; credentials
  ;  (prn "uri handlers - " credentials)
  (db/create-new-user! db/vanilla-db credentials))

(defn verify-user-password
    "Take in a user and password and determine if they are in the database"
    [credentials]
    ;(prn "url handlers verify creds - " credentials)
    (some? (db/verify-credentials db/vanilla-db credentials)))

(defn get-requested-user
  "Makes a call to the hugsql functions that return a requested user if it exist"
  [username]
  (db/get-user  db/vanilla-db {:username username}))

(defn get-all-users
  "Returns all users in the db"
  []
  (db/get-users db/vanilla-db))



(comment


  (create-user
    {:username "chad-uri-handler"
     :pass "123"})

  (get-requested-user "chad")
  (get-requested-user "chad-uri-handler")

  (verify-user-password
    {:username "chad"
     :pass "123"})

  (verify-user-password
                      {:username "chad"
                       :pass "321"})

  (get-all-users))


