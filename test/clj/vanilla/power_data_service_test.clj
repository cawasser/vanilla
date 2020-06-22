(ns vanilla.power-data-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.power-data-service :as spec]
            [vanilla.power-data-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  ;(log/info (testee/fetch-data))
  (log/info (s/explain-str ::spec/power-data-service (testee/fetch-data)))
  (is (s/valid? ::spec/power-data-service (testee/fetch-data))))

