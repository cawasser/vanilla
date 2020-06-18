(ns vanilla.arearange-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.arearange-service :as spec]
            [vanilla.arearange-service :as testee]
            [clojure.tools.logging :as log]))


;(s/def ::title ::vsu/is-string)
(deftest fetch-data-test1
  (is (s/valid? ::spec/title "title string")))


;(s/def ::data-format ::vsd/data-format-in-set)
(deftest fetch-data-test2
  (is (s/valid? ::spec/data-format :data-format/date-yl-yh)))


;(s/def ::src-x-title ::vsu/is-string)
(deftest fetch-data-test3
  (is (s/valid? ::spec/src-x-title "x title string")))


;(s/def ::src-y-title ::vsu/is-string)
(deftest fetch-data-test4
  (log/info (s/explain-str ::spec/src-y-title "y title string"))
  (is (s/valid? ::spec/src-y-title "y title string")))

;(s/def ::name ::vsu/is-string)
(deftest fetch-data-test5
  (is (s/valid? ::spec/name "name string")))


;(s/def ::datapoint-date ::vsu/is-integer)
(deftest fetch-data-test6
  (is (s/valid? ::spec/datapoint-date 1234)))

;(s/def ::datapoint-from ::vsu/is-float)
(deftest fetch-data-test7
  (is (s/valid? ::spec/datapoint-from 1.0)))

;(s/def ::datapoint-to   ::vsu/is-float)
(deftest fetch-data-test8
  (is (s/valid? ::spec/datapoint-to -3.4)))

(deftest fetch-data-test9
  (is (s/valid? ::spec/datapoint [123 1.1 2.2])))

;(s/def ::datapoint (s/tuple [::datapoint-date ::datapoint-from ::datapoint-to]))


;(s/def ::data (s/coll-of ::datapoint :kind vector?))
(deftest fetch-data-test10
  (is (s/valid? ::spec/data [[123 22.22 5.4]])))


;(s/def ::series-data (s/keys :req-un [::name ::data]))
(deftest fetch-data-test11
  (is (s/valid? ::spec/series-data {:name "sd-name-string" :data [[44444 5.4 3.3]]})))


;(s/def ::series (s/coll-of ::series-data :kind vector?))
(deftest fetch-data-test12
  (is (s/valid? ::spec/series [{:name "sd-name-string" :data [[44444 5.4 3.3]]}])))

;(s/def ::arearange-service (s/keys :req-un [::title ::data-format ::src-x-title ::src-y-title ::series]))
(deftest fetch-data-test13
  (log/info "explain " (s/explain-str ::spec/arearange-service {:title "title"
                                                                :data-format :data-format/date-yl-yh
                                                                :src-x-title "x title"
                                                                :src-y-title "y title"
                                                                :series [{:name "sd-name-string" :data [[44444 5.4 3.3]]}]}))
  (is (s/valid? ::spec/arearange-service {:title "title"
                                          :data-format :data-format/date-yl-yh
                                          :src-x-title "x title"
                                          :src-y-title "y title"
                                          :series [{:name "sd-name-string" :data [[44444 5.4 3.3]]}]})))

(deftest fetch-data-test
  (log/info (s/explain-str ::spec/arearange-service (testee/fetch-data)))
  (is (s/valid? ::spec/arearange-service (testee/fetch-data))))

(comment
  [])











