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



(def vanilla-db
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

  (create-services-table-sqlvec vanilla-db)
  (create-services-table vanilla-db)

  (create-service! vanilla-db {:id    "1000" :name ":spectrum-traces" :ret_types "[:x-y]"
                               :doc_string "returns power over frequency"})


  (get-services vanilla-db)

  (drop-services-table vanilla-db)

  ())
