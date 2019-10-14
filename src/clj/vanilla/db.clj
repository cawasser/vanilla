(ns vanilla.db)


(defn spectrum-data []
  [{:name   "trace-1"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}
   {:name   "trace-2"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}
   {:name   "trace-3"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}])

(defn usage-data []
  [["Apples" (rand 100)]
   ["Pears" (rand 100)]
   ["Oranges" (rand 100)]
   ["Plums" (rand 100)]
   ["Bananas" (rand 100)]
   ["Peaches" (rand 100)]
   ["Prunes" (rand 100)]
   ["Avocados" (rand 100)]])

; This function returns a data structure that can be used to create a frequency vs. power line chart
; The structure is returned as a map of keys in "values" (frequency & power) with 5000 values
; The first key "frequency" is a representation of the frequency value
; The second key "power" is a representation of the power value in dBm

(defn power-data []
  [{:name   "dBM-1"
    :values {:frequency (with-precision 1 (range 7900 8400 0.1))
             :power (take 5000 (repeatedly #(+ -200 (rand 150))))}}])

; This function retunrs a data structure that can be used to create a heatmap
; The structure is returned as a map of keys in "values" (long, lat & value) with 2000 values
; The first key "long" is the longitude of the locations
; The second key "lat" is the latitude of the locations
; The third key "value" is the percentage of used resources at the location

(defn heatmap-data []
  [{:name   "heatmap-1"
    :values {:long (take 2000 (repeatedly #(+ -180 (rand 360))))
             :lat  (take 2000 (repeatedly #(+ -90 (rand 180))))
             :value (take 2000 (repeatedly #(+ 10.99999 (rand 50.99999))))}}])