(ns vanilla.specs.common
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.dataformat :as vsd]))


;
; some constants for lat/lon specs
;
(def min-latitude -90)
(def max-latitude 90)
(def min-longitude -180)
(def max-longitude 180)

;
; specs for the lat-lon-label data format
;
(s/def ::lat (vsu/bounded-float min-latitude max-latitude))
(s/def ::lon (vsu/bounded-float min-longitude max-longitude))

(s/def ::latitude (s/double-in :min min-latitude :max max-latitude :NaN? false :infinite? false))
(s/def ::longitude (s/double-in :min min-longitude :max max-longitude :NaN? false :infinite? false))

;
; other specs commonly used
;
(s/def ::title ::vsu/is-string)
(s/def ::data-format ::vsd/data-format-in-set)
