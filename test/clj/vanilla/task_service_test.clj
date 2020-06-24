(ns vanilla.task-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.task-service :as spec]
            [vanilla.task-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info "need to figure out how to handle keywords with the same name.")
  (is (= 0 1)))

;(deftest fetch-data-test
;  (log/info (s/explain-str ::spec/task-service (testee/fetch-data)))
;  (is (s/valid? ::spec/task-service (testee/fetch-data))))

