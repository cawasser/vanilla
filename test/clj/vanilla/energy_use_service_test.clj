(ns vanilla.energy-use-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.energy-use-service :as spec]
            [vanilla.energy-use-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/energy-use-service (testee/fetch-data)))
  (is (s/valid? ::spec/energy-use-service (testee/fetch-data))))

