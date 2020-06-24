(ns vanilla.terminal-list-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.terminal-list-service :as spec]
            [vanilla.terminal-list-service :as testee]
            [clojure.tools.logging :as log]))

;(deftest fetch-data-test
;  (log/info (testee/fetch-data))
;  (is (= 1 1)))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/terminal-list-service (testee/fetch-data)))
  (is (s/valid? ::spec/terminal-list-service (testee/fetch-data))))

