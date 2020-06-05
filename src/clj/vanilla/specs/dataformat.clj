(ns vanilla.specs.dataformat
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]))

;
; specs for :data-format
; two 'simple' ones, and one composed of those two
; make that one simple one, I'm now using the common keyword check above
;
(s/def ::data-format-in-set #{:data-format/carousel
                              :data-format/cont-n
                              :data-format/date-yl-yh
                              :data-format/entities
                              :data-format/entity
                              :data-format/from-to
                              :data-format/from-to-n
                              :data-format/grid-n
                              :data-format/label-y
                              :data-format/lat-lon-e
                              :data-format/lat-lon-label
                              :data-format/rose-y-n
                              :data-format/string
                              :data-format/task-link
                              :data-format/x-y
                              :data-format/x-y-n})
(s/def ::data-format-valid (s/and ::data-format-in-set ::vsu/is-keyword))

