-- :name create-services-table
-- :command :execute
-- :result :raw
-- :doc Create services table
create table services (
  id              VARCHAR(20) PRIMARY KEY,
  name            VARCHAR(30),
  ret_types       VARCHAR(30),
  doc_string      VARCHAR(300))



-- :name drop-services-table :!
-- :doc Drop services table if exists
drop table if exists services


-- :name create-service! :! :n
-- :doc creates a new service record
INSERT INTO services
(id, name, ret_types, doc_string)
VALUES (:id, :name, :ret_types, :doc_string)



-- :name get-services :? :*
-- :doc retrieves a service record given the id
SELECT * FROM services


-- :name delete-service! :! :n
-- :doc deletes a service record given the id
DELETE FROM services WHERE id = :id
