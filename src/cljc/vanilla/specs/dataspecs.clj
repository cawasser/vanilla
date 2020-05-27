(ns vanilla.specs.dataspecs
  (:require [clojure.spec-alpha2 :as s]
            [clojure.tools.logging :as log]))

;
; some common specs for 'type checking'
;
(s/def ::is-keyword keyword?)
(s/def ::is-vector vector?)
(s/def ::is-string string?)
(s/def ::is-float float?)

;
; specs for :data-format
; two 'simple' ones, and one composed of those two
; make that one simple one, I'm now using the common keyword check above
;
;(s/def ::data-format-keyword keyword?)
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
(s/def ::data-format-valid (s/and ::data-format-in-set ::is-keyword))

;
; some constants for lat/lon specs
;
(def min-latitude -90)
(def max-latitude 90)
(def min-longitude -180)
(def max-longitude 180)

;
; a special function to range bound a value
; we'll use this to range bound lat/lon
;
(s/defop bounded-float
         "Specs a float with bounded range (<= min float-value max)"
         [min max]
         (s/and (s/or (float? double?) #(<= min % max))))

;
; specs for the lat-lon-label data format
;
(s/def ::lat (bounded-float min-latitude max-latitude))
(s/def ::lon (bounded-float min-longitude max-longitude))
;(s/def ::lat ::is-float)
;(s/def ::lon ::is-float)
(s/def ::name ::is-string)
(s/def ::lat-lon-label (s/keys :req [::name ::lat ::lon]))
(s/def ::lat-lon-label-un (s/keys :req-un [::name ::lat ::lon]))
(s/def ::lat-lon-label-vector (s/coll-of ::lat-lon-label-un :kind vector?))


