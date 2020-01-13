-- :name create-services-table
-- :command :execute
-- :result :raw
-- :doc Create services table
create table services (
  id              VARCHAR(20) PRIMARY KEY,
  keyword         VARCHAR(30),
  name            VARCHAR(30),
  ret_types       VARCHAR(30),
  read_fn         VARCHAR(128),
  doc_string      VARCHAR(300));



-- :name drop-services-table :!
-- :doc Drop services table if exists
drop table if exists services;


-- :name create-service! :! :n
-- :doc creates a new service record
INSERT INTO services
(id, keyword, name, ret_types, doc_string)
VALUES (:id, :keyword, :name, :ret_types, :read_fn, :doc_string);



-- :name create-services! :! :n
-- :doc creates multiple new service record
INSERT INTO services (id, keyword, name, ret_types, read_fn, doc_string)
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




-- TODO: add username to widgets

-- :name create-layout-table
-- :command :execute
-- :result :raw
-- :doc Create widget layout table
create table layout (
  id              VARCHAR(20) PRIMARY KEY,
  username        VARCHAR(30),
  name            VARCHAR(30),
  ret_types       VARCHAR(30),
  basis           VARCHAR(30),
  data_source     VARCHAR(30),
  type            VARCHAR(30),
  icon            VARCHAR(30),
  label           VARCHAR(30),
  data_grid       VARCHAR(30),
  options         VARCHAR(128));



-- :name drop-layout-table :!
-- :doc Drop layout table if exists
drop table if exists layout;


-- :name create-layout! :! :n
-- :doc creates a new layout record
INSERT INTO services
(id, username, name, ret_types, basis, data_source, type, icon, label, data_grid, options)
VALUES (:id, :username, :name, :ret_types, :basis, :data_source, :type, :icon, :label, :data_grid, :options);


-- :name get-layout :? :*
-- :doc retrieves all widgets given a username
SELECT * FROM layout;


-- :name delete-layout! :! :n
-- :doc deletes a layout record given the id
DELETE FROM layout WHERE id = :id;


-- :name delete-all-layouts! :! :n
-- :doc deletes all layout records
DELETE FROM layout;
