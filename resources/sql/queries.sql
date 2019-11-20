-- :name create-services-table
-- :command :execute
-- :result :raw
-- :doc Create characters table
create table services (
  id              VARCHAR(20) PRIMARY KEY,
  name            VARCHAR(30),
  ret_types       VARCHAR(30),
  doc_string      VARCHAR(300))



-- :name drop-users-table :!
-- :doc Drop users table if exists
drop table if exists services


-- :name create-service! :! :n
-- :doc creates a new user record
INSERT INTO services
(id, name, ret_types, doc_string)
VALUES (:id, :name, :ret_types, :doc_string)



-- :name get-services :? :*
-- :doc retrieves a user record given the id
SELECT * FROM services

