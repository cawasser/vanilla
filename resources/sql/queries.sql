-- src/princess_bride/db/sql/characters.sql
-- The Princess Bride Characters

-- :name create-users-table
-- :command :execute
-- :result :raw
-- :doc Create characters table
create table users (
  id              VARCHAR(20) PRIMARY KEY,
  first_name      VARCHAR(30),
  last_name       VARCHAR(30),
  email           VARCHAR(30),
  pass            VARCHAR(300))



-- :name drop-users-table :!
-- :doc Drop users table if exists
drop table if exists users


-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)


-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id



-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name get-users :? :*
-- :doc retrieves a user record given the id
SELECT * FROM users

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users WHERE id = :id
