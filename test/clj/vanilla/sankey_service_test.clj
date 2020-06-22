(ns vanilla.sankey-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.sankey-service :as spec]
            [vanilla.sankey-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/sankey-service (testee/fetch-data)))
  (is (s/valid? ::spec/sankey-service (testee/fetch-data))))

