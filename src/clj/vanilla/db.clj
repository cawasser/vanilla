(ns vanilla.db)


;(defn spectrum-data []
;  [{:name   "trace-1"
;    :data (into []
;                (take 200
;                      (repeatedly #(+ 5.0
;                                      (rand 5)))))}
;   {:name   "trace-2"
;    :data (into []
;                (take 200
;                      (repeatedly #(+ 5.0
;                                      (rand 5)))))}
;   {:name   "trace-3"
;    :data (into [] (take 200 (repeatedly #(+ 5.0 (rand 5)))))}])

;(defn usage-data []
;  [{:keys ["name" "y" "selected" "sliced"]
;    :data [["Apples" (rand 100)]
;           ["Pears" (rand 100)]
;           ["Oranges" (rand 100)]
;           ["Plums" (rand 100)]
;           ["Bananas" (rand 100)]
;           ["Peaches" (rand 100)]
;           ["Prunes" (rand 100)]
;           ["Avocados" (rand 100)]]}])

; This function returns a data structure that can be used to create a frequency vs. power line vanilla.widgets.line-chart
; The structure is returned as a map of keys in "values" (frequency & power) with 5000 values
; The first key "frequency" is a representation of the frequency value
; The second key "power" is a representation of the power value in dBm

(defn power-data []
  [{:name   "dBM-1"
    :data {:frequency (with-precision 1 (range 7900 8400 0.1))
           :power (take 5000 (repeatedly #(+ -200 (rand 150))))}}])

; This function retunrs a data structure that can be used to create a heatmap
; The structure is returned as a map of keys in "values" (long, lat & value) with 2000 values
; The first key "long" is the longitude of the locations
; The second key "lat" is the latitude of the locations
; The third key "value" is the percentage of used resources at the location

(defn heatmap-data []
  [{:name   "heatmap-1"
    :data {:long (take 2000 (repeatedly #(+ -180 (rand 360))))
           :lat  (take 2000 (repeatedly #(+ -90 (rand 180))))
           :value (take 2000 (repeatedly #(+ 10.99999 (rand 50.99999))))}}])
;
; This function returns a data structure that shows the percentage free/used of a resource pool.
; The structure is returned as a map.
; The first element shows how much of the pool is used.
; The second element shows how much of the pool is available.
;
(defn entity3-data-total []
  [{:data {:used 43.3 :available 56.7}}])

;
; This function returns a data structure that shows how much of various resources are consumed by various consumers.
; The structure is returned as a vector of maps.
; The first element names the consumer.
; The rest of the key-value pairs describe the resource and the amount consumed.
;
(defn entity3-data-by-consumer []
  [{:data [{:consumer "Consumer 1" :resource1 15 :resource2 0  :resource3 0},
           {:consumer "Consumer 2" :resource1 90 :resource2 30 :resource3 0},
           {:consumer "Consumer 3" :resource1 30 :resource2 20 :resource3 75}]}])

;
; This function returns a data structure that the yearly capacity of certain resources.
; The structure is a vector of maps
; The map consists of two elements:
;   Firstly the name of a resource
;   Secondly, a vector of maps containing yearly capacity figures
;     Each of those maps contains an element for the year of interest
;     and a second element with the capacity for that year.
;
(defn entity4-data []
  [{:name "Requirements"
    :data [{:year 2014 :capacity 17} {:year 2015 :capacity 19.5} {:year 2016 :capacity 21.5}]},
   {:name "Resource1"
    :data [{:year 2014 :capacity 12.81} {:year 2015 :capacity 14.63} {:year 2016 :capacity 17.42}]}])

