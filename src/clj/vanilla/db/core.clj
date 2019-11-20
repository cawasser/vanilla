(ns vanilla.db.core
  (:require
    [hugsql.core :as hugsql]
    [next.jdbc :as jdbc]
    [next.jdbc.sql :as sql]
    [com.stuartsierra.component :as component]))



;
; https://www.hugsql.org
;
; https://github.com/seancorfield/usermanager-example
;


(hugsql/def-db-fns "sql/queries.sql")



(def ^:private vanilla-db
  "SQLite database connection spec."
  {:dbtype "sqlite" :dbname "vanilla_db"})



(defrecord Database [db-spec                                ; configuration
                     datasource]                            ; state

  component/Lifecycle
  (start [this]
    (if datasource
      this                                                    ; already initialized
      (assoc this :datasource (jdbc/get-datasource db-spec))))

  (stop [this]
    (assoc this :datasource nil)))



(defn setup-database [] (map->Database {:db-spec vanilla-db}))



(comment

  (hugsql/def-sqlvec-fns "sql/queries.sql")

  (create-users-table-sqlvec vanilla-db)
  (create-users-table vanilla-db)


  (create-user! vanilla-db {:id    "1000" :first_name "Chris" :last_name "Wasser"
                            :email "wasser@vanilla.org" :pass "123456"})


  (get-users vanilla-db)
  (get-user vanilla-db {:id "1000"})
  (get-user vanilla-db {:id "2000"})

  (delete-user! vanilla-db {:id "1000"})

  (drop-users-table vanilla-db)

  ())
