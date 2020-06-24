(ns vanilla.spectrum-traces-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.spectrum-traces-service :as spec]
            [vanilla.spectrum-traces-service :as testee]
            [clojure.tools.logging :as log]))

;(deftest fetch-data-test
;  (log/info (testee/fetch-data))
;  (is (= 1 1)))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/spectrum-traces-service (testee/fetch-data)))
  (is (s/valid? ::spec/spectrum-traces-service (testee/fetch-data))))
