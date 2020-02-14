(ns vanilla.arearange-service)

(defn fetch-data []
  (prn "Arearange Service Data")

  {:title       "Area Range Data"
   :data-format :data-format/x-yl-yh
   :src/x-title "Date"
   :src/y-title "Temperature ('C)"

   :series      [{:name "Temperatures"
                  :keys [ "yl" "yh"]
                  :data [[1.4  4.7] [-1.3  1.9] [-0.7  4.3] [-5.5  3.2] [-9.9  -6.6]
                         [-9.6  0.1] [-0.9  4] [-2.2  2.9] [1.3  2.3] [-0.3  2.9]
                         [1.1  3.8] [0.6  2.1] [-3.4  2.5] [-2.9  2] [-5.7  -2.6]
                         [-8.7  -3.3] [-3.5  -0.3] [-0.2  7] [2.3  8.5] [5.6  9.5]
                         [0.4  5.8] [0.1  3.1] [1.5  4.1] [-0.2  2.8] [2.3  10.3]]}]})