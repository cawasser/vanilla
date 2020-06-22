(ns vanilla.continent-map-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.continent-map-service :as spec]
            [vanilla.continent-map-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/continent-map-service (testee/fetch-data)))
  (is (s/valid? ::spec/continent-map-service (testee/fetch-data))))

