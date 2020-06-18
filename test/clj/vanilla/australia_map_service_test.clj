(ns vanilla.australia-map-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.australia-map-service :as spec]
            [vanilla.australia-map-service :as testee]))

(deftest fetch-data-test
  ;(log/info (s/explain-str ::spec/arearange-service (testee/fetch-data)))
  (is (s/valid? ::spec/australia-map-service (testee/fetch-data))))
