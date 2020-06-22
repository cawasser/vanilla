(ns vanilla.heatmap-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.heatmap-service :as spec]
            [vanilla.heatmap-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/heatmap-service (testee/fetch-data)))
  (is (s/valid? ::spec/heatmap-service (testee/fetch-data))))

