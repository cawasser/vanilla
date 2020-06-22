(ns vanilla.bubble-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.bubble-service :as spec]
            [vanilla.bubble-service :as testee]
            [clojure.tools.logging :as log]))


(deftest fetch-data-test
  (log/info (s/explain-str ::spec/bubble-service (testee/fetch-data)))
  (is (s/valid? ::spec/bubble-service (testee/fetch-data))))

