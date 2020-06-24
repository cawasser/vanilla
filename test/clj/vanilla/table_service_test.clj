(ns vanilla.table-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.table-service :as spec]
            [vanilla.table-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/table-service (testee/fetch-data)))
  (is (s/valid? ::spec/table-service (testee/fetch-data))))

