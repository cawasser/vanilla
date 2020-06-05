(ns vanilla.specs.util
  (:require [clojure.spec-alpha2 :as s]))

;
; some common specs for 'type checking'
;
(s/def ::is-keyword keyword?)
(s/def ::is-vector vector?)
(s/def ::is-string string?)
(s/def ::is-float float?)

;
; a special function to range bound a value
;
(s/defop bounded-float
         [min max]
         (s/and float? #(<= min % max)))
