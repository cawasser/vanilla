(ns vanilla.specs.carousel-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

;
; example of data struct to be spec'd
;
;{:title       "carousel data"
; :data-format :data-format/carousel
; :data        "heatmap-data"}
;

(s/def ::data ::vsu/is-string)

(s/def ::carousel-service (s/keys :req-un [::title ::data-format ::data]))

