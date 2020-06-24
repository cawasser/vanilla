(ns vanilla.specs.stoplight-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

;(s/def ::name ::vsu/is-string)

;(s/def ::keys (s/coll-of ::vsu/is-string :kind vector?))

(s/def ::series-data (s/tuple ::vsu/is-string ::vsu/is-keyword))

;(s/def ::series-data (s/coll-of ::datapoint :kind vector?))

;(s/def ::series-data (s/keys :req-un [::name ::keys ::data]))

(s/def ::series (s/coll-of ::series-data :kind list?))

(s/def ::stoplight-service (s/keys :req-un [::vsu/title ::vsu/data-format ::series]))

