(ns vanilla.bubble-service-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.bubble-service :as spec]
            [vanilla.bubble-service :as testee]
            [clojure.tools.logging :as log]))

(def eltesto {:title "Bubble Data"
              :data-format :data-format/x-y-n
              :series  [{:name "Countries"
                         :data [{:x 95 :y 95 :z 13.8 :name "BE" :country "Belgium"}]}]})
(def eltesto1
  {:name "Countries"
   :data [{:x 95 :y 95 :z 13.8 :name "BE" :country "Belgium"}]})

(def eltesto2
  {:x 95 :y 95 :z 13.8 :name "BE" :country "Belgium"})


(deftest fetch-data-test
  (log/info (s/explain-str ::spec/bubble-service (testee/fetch-data)))
  (is (s/valid? ::spec/bubble-service (testee/fetch-data))))

