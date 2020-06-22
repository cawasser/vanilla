(ns vanilla.specs.bubble-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

;{:title "Bubble Data"
; :data-format :data-format/x-y-n
;
; :series  [{:name "Countries"
;            :data [{:x 95 :y 95 :z 13.8 :name "BE" :country "Belgium"}
;                   {:x 86.5 :y 102.9 :z 14.7 :name "DE" :country "Germany"}]}
;
;           {:name "Fruit"
;            :data [{:x 85.9 :y 64 :z 34 :name "Apples" :fruit "Apples"}
;                   {:x 25.29 :y 15 :z 14 :name "Oranges" :fruit "Oranges"}]}
;
;           {:name "MLB"
;            :keys ["x" "y" "z" "name" "franchise"]
;            :data [[17.63 18.01 6 "ARZ" "Arizona Diamondbacks"]
;                   [106.97 106.59 25 "ATL" "Atlanta Braves"]]}]}

(s/def ::title ::vsu/is-string)

(s/def ::data-format ::vsd/data-format-in-set)

(s/def ::name ::vsu/is-string)
(s/def ::fruit ::vsu/is-string)
(s/def ::country ::vsu/is-string)

(s/def ::x (s/or :a ::vsu/is-double :b ::vsu/is-integer))
(s/def ::y (s/or :a ::vsu/is-double :b ::vsu/is-integer))
(s/def ::z (s/or :a ::vsu/is-double :b ::vsu/is-integer))

(s/def ::data-countries (s/keys :req-un [::x ::y ::z ::name ::country]))
;(s/def ::datapoint (s/tuple ::datapoint-date ::datapoint-from ::datapoint-to))

(s/def ::data (s/coll-of ::data-countries :kind vector?))

(s/def ::series-data (s/keys :req-un [::name ::data]))

(s/def ::series (s/coll-of ::series-data :kind vector?))

(s/def ::bubble-service (s/keys :req-un [::title ::data-format ::series]))
