(ns vanilla.channel-power-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.channel-power-service :as spec]
            [vanilla.channel-power-service :as testee]
            [clojure.tools.logging :as log]))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/channel-power-service (testee/fetch-data "hello" "3000")))
  (is (s/valid? ::spec/channel-power-service (testee/fetch-data "hello" "3000"))))
