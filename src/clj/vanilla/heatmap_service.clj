(ns vanilla.heatmap-service)

;(defn heatmap-data []
;  [{:name   "heatmap-1"
;    :data {:long (take 2000 (repeatedly #(+ -180 (rand 360))))
;           :lat  (take 2000 (repeatedly #(+ -90 (rand 180))))
;           :value (take 2000 (repeatedly #(+ 10.99999 (rand 50.99999))))}}])

(defn heatmap-data []
  [{:name        "Fruit Production per Continent"
    :borderWidth 1
    :keys ["x" "y" "z"]
    :data        [[0 0 10] [0 1 19] [0 2 8] [0 3 24] [0 4 67] [0 5 12] [0 6 0]
                  [1 0 92] [1 1 58] [1 2 78] [1 3 117] [1 4 48] [1 5 35] [1 6 0]
                  [2 0 35] [2 1 15] [2 2 123] [2 3 64] [2 4 52] [2 5 42] [2 6 0]
                  [3 0 72] [3 1 132] [3 2 114] [3 3 19] [3 4 16] [3 5 62] [3 6 0]
                  [4 0 38] [4 1 5] [4 2 8] [4 3 117] [4 4 115] [4 5 2] [4 6 0]
                  [5 0 88] [5 1 32] [5 2 12] [5 3 6] [5 4 120] [5 5 12] [5 6 0]
                  [6 0 13] [6 1 44] [6 2 88] [6 3 98] [6 4 96] [6 5 120] [6 6 0]
                  [7 0 31] [7 1 1] [7 2 82] [7 3 32] [7 4 30] [7 5 44] [7 6 0]
                  [8 0 85] [8 1 97] [8 2 123] [8 3 64] [8 4 84] [8 5 55] [8 6 0]
                  [9 0 47] [9 1 114] [9 2 31] [9 3 48] [9 4 91] [9 5 100] [9 6 0]]}])

(defn fetch-data []
  (prn "Heatmap Service")

  {:title "Heatmap Data"
   :data-format :data-format/x-y-n

   :series  (heatmap-data)})
