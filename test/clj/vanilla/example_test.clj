(ns vanilla.example-test
  (:require [clojure.test :refer :all]))

(deftest example-passing-test
  (is (= 1 1)))


(deftest another-test
  (is (= true false)))
