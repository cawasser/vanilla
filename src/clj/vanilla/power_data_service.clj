(ns vanilla.power-data-service
  (:require [clojure.tools.logging :as log]))


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
  (log/info "Power Data")

  {:title       "Power Data"
   :data-format :data-format/x-y
   :series      (power-data)})





(comment

  (def temp
    {:legendBackgroundColor "rgba(48, 48, 48, 0.8)",
     :labels                {:style {:color "#CCC"}},
     :dataLabelsColor       "#444",
     :boost                 {:useGPUTranslations true},
     :chart                 {:type            "line",
                             :zoomType        "x",
                             :backgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                               :stops          [[0 "rgb(96, 96, 96)"] [1 "rgb(16, 16, 16)"]]},
                             :borderWidth     0,
                             :borderRadius 0,
                             :plotBackgroundColor nil,
                             :plotShadow false,
                             :plotBorderWidth 0}})

  (update-in temp [:chart] assoc :height 500 :width 660)

  (bigdec 3.0)

  (with-precision 1 (range (bigdec 7900) (bigdec 8400) (bigdec 0.1)))

  (with-precision 1 (range 7900 (+ 7900 (* 5 0.1)) 0.1))

  (make-trace 7900 250 0.1 -20 15 1)


  (into []
    (for [[x y] (make-trace 7900 25 0.1 -200 150 1)]
      [x y]))


  ())



