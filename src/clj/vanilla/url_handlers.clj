(ns vanilla.url-handlers
  (:require
    [vanilla.db.core :as db]
    [clojure.core]))


;;;;;;;;;;;;;;;;
;
; HTML endpoint handlers


(defn get-services []
  (db/get-services db/vanilla-db))


(def from-sqlite {:id :key :data_source :data-source :data_grid :data-grid})
(def to-sqlite {:key :id :data-source :data_source :data-grid :data_grid})

; fixup the key changes caused by SQLite with rename-keys
; :id -> :key
; :data_source -> :data-source
; :data_grid -> :data-grid
(defn get-layout []
  (map #(clojure.set/rename-keys % from-sqlite) (db/get-layout db/vanilla-db)))

(defn save-layout [new-layout]
  (let [renamed (map #(clojure.set/rename-keys % to-sqlite) new-layout)]
    (prn "SAVE-LAYOUT route: " new-layout
      " //// renames " renamed)
    (db/save-layout! db/vanilla-db renamed)))


(defn create-user
  "Makes a call to the hugsql functions that create a user"
  [credentials]
  ;; credentials
    (prn "uri handlers - " credentials)
    (db/create-new-user! db/users-db credentials))

(defn verify-user-password
    "Take in a user and password and determine if they are in the database"
    [credentials]
    (prn "url handlers verify creds - " credentials)
    (some? (db/verify-credentials db/users-db credentials)))

(defn get-requested-user
  "Makes a call to the hugsql functions that return a requested user if it exist"
  [username]
  (db/get-user  db/users-db {:username username}))

(defn get-all-users
  "Returns all users in the db"
  []
  (db/get-users db/users-db))



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

  (get-all-users)


  )