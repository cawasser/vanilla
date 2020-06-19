(ns vanilla.specs.beam-location-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]
            [vanilla.specs.latlonlabel :as vsl]))

(s/def ::title ::vsu/is-string)

(s/def ::data-format ::vsd/data-format-in-set)

(s/def ::data ::vsl/lat-lon-label-vector)

(s/def ::beam-location-service (s/keys :req-un [::title ::data-format ::data]))

