(ns vanilla.power-data-service)


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


(defn power-data []
  [{:name   "dBM-1"
    :keys ["x" "y"]
    :data (make-trace 7900 500 0.1 -200 150 1)}])



(defn fetch-data []
  (prn "Power Data")

  {:title       "Power Data"
   :data-format :data-format/x-y
   :series      (power-data)})





(comment
  (bigdec 3.0)

  (with-precision 1 (range (bigdec 7900) (bigdec 8400) (bigdec 0.1)))

  (with-precision 1 (range 7900 (+ 7900 (* 5 0.1)) 0.1))

  (make-trace 7900 250 0.1 -200 150 1)


  (into []
    (for [[x y] (make-trace 7900 25 0.1 -200 150 1)]
      [x y]))

  ())



