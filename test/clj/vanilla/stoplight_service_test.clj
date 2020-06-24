(ns vanilla.stoplight-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.stoplight-service :as spec]
            [vanilla.stoplight-service :as testee]
            [clojure.tools.logging :as log]))

;(deftest fetch-data-test
;  (log/info (testee/fetch-data))
;  (is (= 1 1)))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/stoplight-service (testee/fetch-data)))
  (is (s/valid? ::spec/stoplight-service (testee/fetch-data))))
