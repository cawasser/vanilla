(ns vanilla.specs.util
  (:require [clojure.spec-alpha2 :as s]))

;
; some common specs for 'type checking'
;
(s/def ::is-keyword keyword?)
(s/def ::is-vector vector?)
(s/def ::is-string string?)
(s/def ::is-float float?)
(s/def ::is-double double?)
(s/def ::is-integer integer?)
(s/def ::is-list list?)

;
; a special function to range bound a float
; spec already has a built-in for double called double-in
;
(s/defop bounded-float
         [min max]
         (s/and float? #(<= min % max)))
