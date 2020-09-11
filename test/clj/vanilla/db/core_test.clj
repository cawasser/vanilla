(ns vanilla.db.core-test
  (:require
    [clojure.test :refer :all]
    [vanilla.db.core :as db]
    [next.jdbc.sql :as sql]))


;;;;;;;;;;
;; Helpers

;; Create a shorthand for the database spec
(def db-spec db/vanilla-db)



;;;;


;;;;;;;;;;;;;;;;;
;; SQL statements

(deftest sql-gets

  (testing "SQL - Select all from services table"
    (is
      (some? (sql/query db-spec ["SELECT * FROM services"]))))

  (testing "SQL - Select all from users table"
    (is
      (some? (sql/query db-spec ["SELECT * FROM users"]))))

  (testing "SQL - Select all from layout table"
    (is
      (some? (sql/query db-spec ["SELECT * FROM layout"])))))

;;;;


;;;;;;;;;;;;
;; Mock data
;;

(def mock-user
  {:username "test"
   :pass     "password"})

(def mock-layout
  {:layout [[":chart"
             "{:x 0, :y 0, :w 5, :h 15}"
             ":spectrum-traces"
             "/images/area-widget.png"
             "07f9d07a-d315-4459-80f3-5e93ea0b4350"
             "Area"
             ":area-widget"
             "#:viz{:style-name \"widget\", :animation false, :x-title \"frequency\", :banner-text-color {:r 255, :g 255, :b 255, :a 1}, :title \"Channels (area)\", :allowDecimals false, :banner-color {:r 0, :g 0, :b 255, :a 1}, :y-title \"power\", :tooltip {:followPointer true}}"
             "[:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]"
             ":area-chart"
             "chad2"]]})

;;;;



;;;;;;;;;;;;;;;;;;;;;;;;
;; Database interactions

(deftest user-test

  (testing "User creation"
    (is
      (db/create-new-user! db-spec mock-user)))

  (testing "User retrieval"
    (is
      (db/get-user db-spec {:username (:username mock-user)})))

  (testing "Get all users"
    (is
      (db/get-users db-spec)))

  (testing "Delete user"
    (is
      (db/delete-user! db-spec {:username (:username mock-user)}))))


;;@TODO - is there a better test for services?
(deftest service-test

  (testing "Services exist"
    (is
      (some? (db/get-services db-spec)))))


(deftest layout-test
  "Create a mock layout, save it, retrieve it, delete it."

  (testing "Create a mock layout"
    (is
      (some? mock-layout)))

  (testing "Save a mock layout"
    (is
      (db/save-layout! db-spec mock-layout)))

  (testing "Retrieve the mock layout"
    (is
      (db/get-user-layout db-spec {:username (:username mock-layout)})))

  (testing "Delete the mock layout"
    (is
      (db/delete-layout! db-spec {:id (:id mock-layout)})))) ;; this is probably wrong, need to get layout then id




;;;;