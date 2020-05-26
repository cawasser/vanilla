(ns vanilla.specs.dataspecs
  (:require [clojure.spec-alpha2 :as s]
            [clojure.spec-alpha2.gen :as gen]
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
; some quick tests of the data-format specs
;
(log/info "keyword this should be true = " (s/valid? ::is-keyword :data-format/lat-lon-label))
(log/info "keyword this should be false = " (s/valid? ::is-keyword 42))
(log/info "in-set this should be true = " (s/valid? ::data-format-in-set :data-format/lat-lon-label))
(log/info "in-set this should be false = " (s/valid? ::data-format-in-set :data-format/lat-lon))
(log/info "::data-format-valid keyword this should be true = " (s/valid? ::data-format-valid :data-format/lat-lon-label))
(log/info "::data-format-valid keyword this should be false = " (s/valid? ::data-format-valid 42))
(log/info "::data-format-valid in-set this should be true = " (s/valid? ::data-format-valid :data-format/lat-lon-label))
(log/info "::data-format-valid in-set this should be false = " (s/valid? ::data-format-valid :data-format/lat-lon))

(log/info "explain failure: " (s/explain-str ::is-keyword 42))

(log/info "generate a value: " (gen/generate (s/gen ::data-format-in-set)))
(log/info "generate a value: " (gen/generate (s/gen ::is-keyword)))

;
;this didn't work if ::data-format-valid had the keyword check first in the s/and!!!
(log/info "generate a value: " (gen/generate (s/gen ::data-format-valid)))
;

(log/info "generate a series of values: " (gen/sample (s/gen ::data-format-in-set)))

;
; some test data for specing lat-lon-label
;
(def lat-lon-label-data [{:name "Canberra",
                          :lat -35.2809,
                          :lon 149.13
                          :dataLabels {:align "left" :x 5 :verticalAlign "middle"}}
                         {:name "Brisbane",
                          :lat -27.47012,
                          :lon 153.021072}
                         {:name "Geraldton",
                          :lat -28.782387,
                          :lon 114.607513}
                         {:name "Wagga Wagga",
                          :lat -35.117275,
                          :lon 147.356522,
                          :dataLabels {:align "right" :y 5 :verticalAlign "middle"}}])

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
         (s/and float? #(<= min % max)))

;
; specs for the lat-lon-label data format
;
(s/def ::is-valid-latitude (bounded-float min-latitude max-latitude))
(s/def ::is-valid-longitude (bounded-float min-longitude max-longitude))

;
; tests for lat-lon-label spec
;
(log/info "vector this should be false = " (s/valid? ::is-vector 42))
(log/info "vector this should be true = " (s/valid? ::is-vector []))


