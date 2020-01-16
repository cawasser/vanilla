-- :name create-services-table
-- :command :execute
-- :result :raw
-- :doc Create services table
create table services (
  id              VARCHAR(20) PRIMARY KEY,
  keyword         VARCHAR(30),
  name            VARCHAR(30),
  ret_type        VARCHAR(30),
  read_fn         VARCHAR(128),
  doc_string      VARCHAR(300));



-- :name drop-services-table :!
-- :doc Drop services table if exists
drop table if exists services;


-- :name create-service! :! :n
-- :doc creates a new service record
INSERT INTO services
(id, keyword, name, ret_type, read_fn, doc_string)
VALUES (:id, :keyword, :name, :ret_type, :read_fn, :doc_string);



-- :name create-services! :! :n
-- :doc creates multiple new service record
INSERT INTO services (id, keyword, name, ret_type, read_fn, doc_string)
values :tuple*:services;




-- :name get-services :? :*
-- :doc retrieves a service record given the id
SELECT * FROM services;


-- :name delete-service! :! :n
-- :doc deletes a service record given the id
DELETE FROM services WHERE id = :id;


-- :name delete-all-services! :! :n
-- :doc deletes all service records
DELETE FROM services;
