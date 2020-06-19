(ns vanilla.beam-location-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.beam-location-service :as spec]
            [vanilla.beam-location-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (testee/fetch-data "Ka"))
  ;(log/info (s/explain-str ::spec/beam-location-service (testee/fetch-data "Ka")))
  (is (s/valid? ::spec/beam-location-service (testee/fetch-data "Ka"))))

