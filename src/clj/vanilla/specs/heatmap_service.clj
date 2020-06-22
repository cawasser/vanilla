(ns vanilla.specs.heatmap-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

(s/def ::name ::vsu/is-string)

(s/def ::keys (s/coll-of ::vsu/is-string :kind vector?))

(s/def ::x (s/coll-of ::vsu/is-string :kind vector?))

(s/def ::y (s/coll-of ::vsu/is-string :kind vector?))

(s/def ::categories (s/keys :req-un [::x ::y]))

(s/def ::datapoint (s/coll-of ::vsu/is-integer :kind vector?))

(s/def ::data (s/coll-of ::datapoint :kind vector?))

(s/def ::series-data (s/keys :req-un [::name ::keys ::categories ::data]))

(s/def ::series (s/coll-of ::series-data :kind vector?))

(s/def ::heatmap-service (s/keys :req-un [::vsc/title ::vsc/data-format ::series]))

