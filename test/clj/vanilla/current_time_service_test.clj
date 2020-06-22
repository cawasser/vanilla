(ns vanilla.current-time-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.current-time-service :as spec]
            [vanilla.current-time-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/current-time-service (testee/fetch-data)))
  (is (s/valid? ::spec/current-time-service (testee/fetch-data))))

