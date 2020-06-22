(ns vanilla.scatter-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.scatter-service :as spec]
            [vanilla.scatter-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/scatter-service (testee/fetch-data)))
  (is (s/valid? ::spec/scatter-service (testee/fetch-data))))
