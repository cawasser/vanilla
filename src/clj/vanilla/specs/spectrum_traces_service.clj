(ns vanilla.specs.spectrum-traces-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

(s/def ::name ::vsu/is-string)

(s/def ::keys (s/coll-of ::vsu/is-string :kind vector?))

(s/def ::datapoint (s/tuple ::vsu/is-decimal ::vsu/is-float))

(s/def ::data (s/coll-of ::datapoint :kind vector?))

(s/def ::series-data (s/keys :req-un [::name ::keys ::data]))

(s/def ::series (s/coll-of ::series-data :kind vector?))

(s/def ::spectrum-traces-service (s/keys :req-un [::vsu/title ::vsu/data-format ::series]))

