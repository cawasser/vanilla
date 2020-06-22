(ns vanilla.specs.current-time-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

(s/def ::text ::vsu/is-string)

(s/def ::current-time-service (s/keys :req-un [::vsc/title ::vsc/data-format ::text]))

