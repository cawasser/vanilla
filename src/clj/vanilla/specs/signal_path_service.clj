(ns vanilla.specs.signal-path-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))


(s/def ::keys (s/coll-of ::vsu/is-string :kind vector?))

(s/def ::data ::vsu/is-list)

(s/def ::series-data (s/keys :req-un [::keys ::data]))

(s/def ::series (s/coll-of ::series-data :kind vector?))

(s/def ::signal-path-service (s/keys :req-un [::vsc/title ::vsc/data-format ::series]))
