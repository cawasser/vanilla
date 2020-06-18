(ns vanilla.specs.australia-map-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]
            [vanilla.specs.latlonlabel :as vsl]))

;
; example of data we're specing
;
;{:title "Australia Data"
; :data-format :data-format/lat-lon-label
;
; :data [{:name "Canberra",
;         :lat -35.2809,
;         :lon 149.13
;         :dataLabels {:align "left" :x 5 :verticalAlign "middle"}}
;        {:name "Brisbane",
;         :lat -27.47012,
;         :lon 153.021072}
;        {:name "Geraldton",
;         :lat -28.782387,
;         :lon 114.607513}
;        {:name "Wagga Wagga",
;         :lat -35.117275,
;         :lon 147.356522,
;         :dataLabels {:align "right" :y 5 :verticalAlign "middle"}}]}
;

(s/def ::title ::vsu/is-string)

(s/def ::data-format ::vsd/data-format-in-set)

(s/def ::data ::vsl/lat-lon-label-vector)

(s/def ::australia-map-service (s/keys :req-un [::title ::data-format ::data]))