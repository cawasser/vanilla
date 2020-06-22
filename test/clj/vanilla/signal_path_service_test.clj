(ns vanilla.signal-path-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.signal-path-service :as spec]
            [vanilla.signal-path-service :as testee]
            [clojure.tools.logging :as log]))


;(deftest fetch-data-test
;  (log/info (testee/fetch-data))
;  (is (= 1 1)))


(deftest fetch-data-test
  (log/info (s/explain-str ::spec/signal-path-service (testee/fetch-data)))
  (is (s/valid? ::spec/signal-path-service (testee/fetch-data))))

