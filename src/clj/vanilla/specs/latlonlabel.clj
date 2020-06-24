(ns vanilla.specs.latlonlabel
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]))

;
; specs for the lat-lon-label data format
;
(s/def ::name ::vsu/is-string)
(s/def ::lat ::vsc/lat)
(s/def ::lon ::vsc/lon)
(s/def ::lat-lon-label (s/keys :req [::name ::lat ::lon]))
(s/def ::lat-lon-label-un (s/keys :req-un [::name ::lat ::lon]))
(s/def ::lat-lon-label-vector (s/coll-of ::lat-lon-label-un :kind vector?))
