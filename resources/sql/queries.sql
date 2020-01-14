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
  id                    VARCHAR(20) PRIMARY KEY,
  username              VARCHAR(30),
  name                  VARCHAR(30),
  ret_types             VARCHAR(30),
  basis                 VARCHAR(30),
  data_source           VARCHAR(30),
  type                  VARCHAR(30),
  icon                  VARCHAR(30),
  label                 VARCHAR(30),
  data_grid             VARCHAR(30),
  viz_style-name        VARCHAR(30),
  viz_y-title           VARCHAR(30),
  viz_x-title           VARCHAR(30),
  viz_line-width        VARCHAR(30),
  viz_data-labels       VARCHAR(30),
  viz_label-format      VARCHAR(30),
  viz_allow-decimals     VARCHAR(30),
  viz_banner-color      VARCHAR(30),
  viz_title             VARCHAR(30),
  viz_icon              VARCHAR(30),
  viz_slice-format      VARCHAR(30),
  viz_banner-text-color VARCHAR(30),);



-- :name drop-layout-table :!
-- :doc Drop layout table if exists
drop table if exists layout;


-- :name create-layout! :! :n
-- :doc creates a new layout record
INSERT INTO services
(id, username, name, ret_types, basis, data_source, type, icon, label, data_grid,
    viz_style-name, viz_y-title, viz_x-title, viz_line-width, viz_data-labels,
    viz_label-format, viz_allow-decimals, viz_banner-color, viz_title, viz_icon,
    viz_slice-format, viz_banner-text-color)
VALUES (:id, :username, :name, :ret_types, :basis, :data_source, :type, :icon, :label, :data_grid,
        :viz_style-name, :viz_y-title, :viz_x-title, :viz_line-width, :viz_data-labels,
        :viz_label-format, :viz_allow-decimals, :viz_banner-color, :viz_title, :viz_icon,
        :viz_slice-format, :viz_banner-text-color);


-- :name get-layout :? :*
-- :doc retrieves all widgets given a username
SELECT * FROM layout;


-- :name delete-layout! :! :n
-- :doc deletes a layout record given the id
DELETE FROM layout WHERE id = :id;


-- :name delete-all-layouts! :! :n
-- :doc deletes all layout records
DELETE FROM layout;
