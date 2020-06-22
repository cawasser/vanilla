(ns vanilla.network-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.network-service :as spec]
            [vanilla.network-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/network-service (testee/fetch-data)))
  (is (s/valid? ::spec/network-service (testee/fetch-data))))

