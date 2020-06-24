(ns vanilla.specs.usage-12-hour-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

(s/def ::name ::vsu/is-string)

(s/def ::datapoint (s/tuple ::vsu/is-string ::vsu/is-integer))
(s/def ::data (s/coll-of ::datapoint :kind vector?))

(s/def ::series-data (s/keys :req-un [::name ::data]))
(s/def ::series (s/coll-of ::series-data :kind vector?))

(s/def ::usage-12-hour-service (s/keys :req-un [::vsc/title ::vsc/data-format ::series]))


