(ns vanilla.usage-data-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.usage-data-service :as spec]
            [vanilla.usage-data-service :as testee]
            [clojure.tools.logging :as log]))

;(deftest fetch-data-test
;  (log/info (testee/fetch-data))
;  (is (= 1 1)))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/usage-data-service (testee/fetch-data)))
  (is (s/valid? ::spec/usage-data-service (testee/fetch-data))))

