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

;
; This function returns a data structure that shows the percentage free/used of a resource pool.
; The structure is returned as a map.
; The first element shows how much of the pool is used.
; The second element shows how much of the pool is available.
;
(defn entity3-data-total []
  {:used 43.3 :available 56.7})

;
; This function returns a data structure that shows how much of various resources are consumed by various consumers.
; The structure is returned as a vector of maps.
; The first element names the consumer.
; The rest of the key-value pairs describe the resource and the amount consumed.
;
(defn entity3-data-by-consumer []
  [{:consumer "Consumer 1" :resource1 15 :resource2 0  :resource3 0},
   {:consumer "Consumer 2" :resource1 90 :resource2 30 :resource3 0},
   {:consumer "Consumer 3" :resource1 30 :resource2 20 :resource3 75}])

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
    :yearly-capacity [{:year 2014 :capacity 17} {:year 2015 :capacity 19.5} {:year 2016 :capacity 21.5}]},
   {:name "Resource1"
    :yearly-capacity [{:year 2014 :capacity 12.81} {:year 2015 :capacity 14.63} {:year 2016 :capacity 17.42}]}])

