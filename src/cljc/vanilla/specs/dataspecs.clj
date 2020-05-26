(ns vanilla.specs.dataspecs
  (:require [clojure.spec-alpha2 :as s]
            [clojure.spec-alpha2.gen :as gen]
            [clojure.tools.logging :as log]))

;
; specs for :data-format
; two 'simple' ones, and one composed of those two
;
(s/def ::data-format-keyword keyword?)
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
(s/def ::data-format-valid (s/and ::data-format-in-set ::data-format-keyword))


(log/info "keyword this should be true = " (s/valid? ::data-format-keyword :data-format/lat-lon-label))
(log/info "keyword this should be false = " (s/valid? ::data-format-keyword 42))
(log/info "in-set this should be true = " (s/valid? ::data-format-in-set :data-format/lat-lon-label))
(log/info "in-set this should be false = " (s/valid? ::data-format-in-set :data-format/lat-lon))
(log/info "::data-format-valid keyword this should be true = " (s/valid? ::data-format-valid :data-format/lat-lon-label))
(log/info "::data-format-valid keyword this should be false = " (s/valid? ::data-format-valid 42))
(log/info "::data-format-valid in-set this should be true = " (s/valid? ::data-format-valid :data-format/lat-lon-label))
(log/info "::data-format-valid in-set this should be false = " (s/valid? ::data-format-valid :data-format/lat-lon))

(log/info "explain failure: " (s/explain-str ::data-format-keyword 42))

(log/info "generate a value: " (gen/generate (s/gen ::data-format-in-set)))
(log/info "generate a value: " (gen/generate (s/gen ::data-format-keyword)))

;
;this didn't work if ::data-format-valid had the keyword check first in the s/and!!!
(log/info "generate a value: " (gen/generate (s/gen ::data-format-valid)))
;

(log/info "generate a series of values: " (gen/sample (s/gen ::data-format-in-set)))
