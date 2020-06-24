(ns vanilla.specs.terminal-list-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

;
; common to header and table data
;
(s/def ::name ::vsu/is-string)

;
; header
;
(s/def ::key ::vsu/is-string)
(s/def ::editable ::vsu/is-boolean)

(s/def ::header-datapoint (s/keys :req-un [::key ::name ::editable]))

;
; the final spec
;
(s/def ::metadata (s/coll-of ::header-datapoint :kind vector?))
(s/def ::series ::vsu/is-vector)

(s/def ::terminal-list-service (s/keys :req-un [::vsc/title ::vsc/data-format ::metadata ::series]))
