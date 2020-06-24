(ns vanilla.usage-12-hour-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.usage-12-hour-service :as spec]
            [vanilla.usage-12-hour-service :as testee]
            [clojure.tools.logging :as log]))

;(deftest fetch-data-test
;  (log/info (testee/fetch-data))
;  (is (= 1 1)))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/usage-12-hour-service (testee/fetch-data)))
  (is (s/valid? ::spec/usage-12-hour-service (testee/fetch-data))))

