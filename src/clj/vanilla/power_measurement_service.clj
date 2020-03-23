(ns vanilla.power-measurement-service
  (:require [clojure.tools.logging :as log]))
  ;(:import 'java.util.Date))



(def last-n-points (atom [{:name "device-1"
                           :keys ["x" "y"]
                           :data []}
                          {:name "device-2"
                           :keys ["x" "y"]
                           :data []}]))
(def nominal -24.5)


(defn datetime-stamp []
  (.getTime (java.util.Date.)))



(defn new-measurement [series size]
  (let [date-stamp (datetime-stamp)]
    (-> series
        :data
        (conj [date-stamp (+ nominal (rand 1))])
        (#(if (< size (count %))
            (into [] (drop 1 (into [] %)))
            %))
        (#(assoc series :data %)))))



(defn power-data [size]
  (let [dev-1 (new-measurement (get @last-n-points 0) size)
        dev-2 (new-measurement (get @last-n-points 1) size)]
    (reset! last-n-points [dev-1 dev-2]))

  @last-n-points)




(defn fetch-data []
  (log/info "Device Power")

  {:title           "Device Power"
   :data-format     :data-format/date-y
   :series          (power-data 200)
   :src/chart-title "dBm"
   :src/x-title     "Date/Time"
   :src/y-title     "Power"})



(comment

  (def device-1 {:name "trace-1"
                 :keys ["x" "y"]
                 :data []})
  (def device-nn {:name "trace-1"
                  :keys ["x" "y"]
                  :data [[11111 -24.44326349977039]
                         [11111 -24.12593808084187]]})
  (def series device-1)
  (def size 2)


  (-> device-1
      :data
      (conj [11111 (+ nominal (rand 1))])
      (#(assoc device-1 :data %)))

  (def u (-> device-1
             :data
             (conj [11111 (+ nominal (rand 1))])
             (#(assoc device-1 :data %))))


  (-> u
      :data
      (conj [11111 (+ nominal (rand 1))])
      (#(assoc u :data %)))


  (-> device-nn
      :data
      (conj [11111 (+ nominal (rand 1))])
      (#(if (< size (count %))
          (drop 1 %)
          "keep")))



      ;(#(if (< size (count %)) (drop % 1))))
      ;(#(assoc u :data %)))



  (new-measurement device-1 2)
  (def u (new-measurement device-1 2))
  (def uu (new-measurement u 2))

  (new-measurement uu 2)
  (new-measurement (new-measurement uu 2) 2)

  (reset! last-n-points [])

  (power-data 200)

  (def last-n-points (atom [{:name "device-1"
                             :keys ["x" "y"]
                             :data []}
                            {:name "device-2"
                             :keys ["x" "y"]
                             :data []}]))

  (drop 1 @last-n-points)
  (power-data 5)

  ())

