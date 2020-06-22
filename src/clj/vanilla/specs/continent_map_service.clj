(ns vanilla.specs.continent-map-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]
            [vanilla.specs.latlonlabel :as vsl]))

;
; example of data we're specing
;
;{:title "Continent Data"
; :data-format :data-format/lat-lon-cont-n
;
; :data [{:name "Canberra",
;         :lat -35.2809,
;         :lon 149.13}
;        {:name "Brisbane",
;         :lat -27.47012,
;         :lon 153.021072}
;        {:name "Geraldton",
;         :lat -28.782387,
;         :lon 114.607513}
;        {:name "Wagga Wagga",
;         :lat -35.117275,
;         :lon 147.356522}]}
;

(s/def ::data ::vsl/lat-lon-label-vector)

(s/def ::continent-map-service (s/keys :req-un [::vsc/title ::vsc/data-format ::data]))


