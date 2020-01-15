(ns vanilla.spectrum-traces-service)

(defn spectrum-data []
  [{:name   "trace-1"
    :data (into []
                (take 200
                      (repeatedly #(+ 5.0
                                      (rand 5)))))}
   {:name   "trace-2"
    :data (into []
                (take 200
                      (repeatedly #(+ 5.0
                                      (rand 5)))))}
   {:name   "trace-3"
    :data (into [] (take 200 (repeatedly #(+ 5.0 (rand 5)))))}])

(defn fetch-data []
  (prn "Spectrum Traces")

  {:title           "Spectrum Traces"
   :data-format     :data-format/y
   :series          (spectrum-data)
   :src/chart-title "dB"
   :src/x-title     "frequency"
   :src/y-title     "power"})

