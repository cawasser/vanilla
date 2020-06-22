(ns vanilla.specs.sankey-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

(s/def ::keys (s/coll-of ::vsu/is-string :kind vector?))

(s/def ::datapoint (s/tuple ::vsu/is-string ::vsu/is-string ::vsu/is-integer))

(s/def ::data (s/coll-of ::datapoint :kind vector?))

(s/def ::series-data (s/keys :req-un [::keys ::data]))

(s/def ::series (s/coll-of ::series-data :kind vector?))

(s/def ::sankey-service (s/keys :req-un [::vsc/title ::vsc/data-format ::series]))

