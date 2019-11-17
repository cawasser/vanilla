(ns vanilla.db.core
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "sql/queries.sql")

(def db ())

(comment

  (hugsql/def-sqlvec-fns "sql/queries.sql")

  (create-users-table db)


  (create-user! {:id    "1000" :first_name "Chris" :last_name "Wasser"
                 :email "wasser@vanilla.org" :pass "123456"})

  (create-users-table)


  (drop-users-table)

  ())
