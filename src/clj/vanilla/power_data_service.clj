(ns vanilla.power-data-service)

(defn power-data []
  [{:name   "dBM-1"
    :data {:frequency (with-precision 1 (range 7900 8400 0.1))
           :power (take 5000 (repeatedly #(+ -200 (rand 150))))}}])

(defn fetch-data []
  (prn "Power Data")

  {:title       "Power Data"
   :data-format :data-format/x-y
   :series      (power-data)})
