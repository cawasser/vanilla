(ns vanilla.carousel-service-test
  (:require [clojure.test :refer :all][clojure.spec-alpha2 :as s]
            [vanilla.specs.carousel-service :as spec]
            [vanilla.carousel-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/carousel-service (testee/fetch-data)))
  (is (s/valid? ::spec/carousel-service (testee/fetch-data))))

