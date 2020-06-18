(ns vanilla.specs.arearange-service
  (:require [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.common :as vsc]
            [vanilla.specs.dataformat :as vsd]))

;
; this spec is for data returned by the fetch-data function of the arearange service.
; the data is of the format:
;
; {
;  title
;  data-format
;  title for x-axis data
;  title for y-axis data
;  [   'series'
;   {
;    name of series
;    [ 'data'
;     [
;      date of data point
;      'from' value of data point
;      'to' value of data point
;     ]
;    ]
;   }
;  ]
; }
;
; example:
;
;{:title       "Area Range Data"
; :data-format :data-format/date-yl-yh
; :src/x-title "Date"
; :src/y-title "Temperature (°C)"
;
; :series      [{:name "Temperatures"
;                :data [[1483232400000 1.4 4.7] [1483318800000 -1.3 1.9] [1483405200000 -0.7 4.3]]}]}
;


(s/def ::title ::vsu/is-string)

(s/def ::data-format ::vsd/data-format-in-set)

(s/def ::src-x-title ::vsu/is-string)

(s/def ::src-y-title ::vsu/is-string)


(s/def ::name ::vsu/is-string)

(s/def ::datapoint-date ::vsu/is-integer)
(s/def ::datapoint-from ::vsu/is-float)
(s/def ::datapoint-to   ::vsu/is-float)
(s/def ::datapoint (s/tuple ::datapoint-date ::datapoint-from ::datapoint-to))

(s/def ::data (s/coll-of ::datapoint :kind vector?))

(s/def ::series-data (s/keys :req-un [::name ::data]))

(s/def ::series (s/coll-of ::series-data :kind vector?))

(s/def ::arearange-service (s/keys :req-un [::title ::data-format ::src-x-title ::src-y-title ::series]))

