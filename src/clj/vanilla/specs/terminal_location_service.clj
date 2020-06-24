(ns vanilla.specs.terminal-location-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

(s/def ::data ::vsu/is-list)

(s/def ::terminal-location-service (s/keys :req-un [::vsc/title ::vsc/data-format ::data]))
