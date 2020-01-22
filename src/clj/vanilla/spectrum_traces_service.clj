(ns vanilla.spectrum-traces-service)


(defn zipvec [keys vals]
  (loop [v []
         ks (seq keys)
         vs (seq vals)]
    (if (and ks vs)
      (recur (conj v [(first ks) (first vs)])
        (next ks)
        (next vs))
      v)))



(defn- make-trace [start-x count step min-y y-delta x-precision]
  (let [x (with-precision
            x-precision
            (range (bigdec start-x)
              (bigdec (+ start-x  (* count step)))
              (bigdec step)))
        y (take count (repeatedly #(+ min-y (rand y-delta))))]

    (zipvec x y)))



(defn spectrum-data []
  [{:name "trace-1"
    :keys ["x" "y"]
    :data (make-trace 100 200 0.5 5 5 1)}

   {:name "trace-2"
    :keys ["x" "y"]
    :data (make-trace 100 200 0.5 5 5 1)}

   {:name "trace-3"
    :keys ["x" "y"]
    :data (make-trace 100 200 0.5 5 5 1)}])



(defn fetch-data []
  (prn "Spectrum Traces")

  {:title           "Spectrum Traces"
   :data-format     :data-format/x-y
   :series          (spectrum-data)
   :src/chart-title "dB"
   :src/x-title     "frequency"
   :src/y-title     "power"})






(comment

  (range 100 (+ 100 (* 20 0.5)) 0.5)

  (take 20 (repeatedly #(+ 5.0 (rand 5))))


  (def c-x (range 100 (+ 100 (* 20 0.5)) 0.5))
  (def c-y (take 20 (repeatedly #(+ 5.0 (rand 5)))))

  (seq c-x)
  (seq c-y)

  (zipvec c-x c-y)

  (let [x (range 100 (+ 100 (* 20 0.5)) 0.5)
        y (take 20 (repeatedly #(+ 5.0 (rand 5))))]

    (->> (zipmap x y)
      (into (sorted-map-by <))))

  (make-trace 100 10 0.5 5 5 1)


  ())