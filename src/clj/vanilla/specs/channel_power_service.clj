(ns vanilla.specs.channel-power-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

(s/def ::name ::vsu/is-string)

(s/def ::keys (s/coll-of ::vsu/is-string :kind vector?))

(s/def ::data ::vsu/is-list)

(s/def ::series-data (s/keys :req-un [::name ::keys ::data]))

(s/def ::series (s/coll-of ::series-data :kind vector?))

(s/def ::channel-power-service (s/keys :req-un [::vsc/title ::vsc/data-format ::series]))

