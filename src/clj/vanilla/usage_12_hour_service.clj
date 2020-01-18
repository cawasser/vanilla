(ns vanilla.usage-12-hour-service)


(defn- make-map [name xs max-qty]
  {:name name
   :data (into []
           (for [y (range (count xs))]
              [(get xs y) (rand-int max-qty)]))})


(def hours ["0000h" "0100h" "0200h" "0300h" "0400h" "0600h"
            "0700h" "0800h" "0900h" "1000h" "1100h"])

(defn fetch-data []
  (prn "12-hour Usage Service")

  {:title       "12-hour Usage Data"
   :data-format :data-format/rose-y-n
   :src/x-title "time"
   :src/y-title "tons"
   :series      [
                 (make-map "Apple" hours 200)
                 (make-map "Pears" hours 200)
                 (make-map "Grapes" hours 200)
                 (make-map "Oranges" hours 200)
                 (make-map "Bananas" hours 200)]})



(comment

  (make-map "Apple" ["0000h" "0100h"] 10)

  (fetch-data)

  (range (count ["0000h" "0100h"]))

  (get ["0000h" "0100h"] 0)


  (def default-options {:colorByPoint true
                        :zmin         0
                        :innerSize    "20%"
                        :minPointSize 10})

  (def chart-type {})
  (def options {})
  (def slice-at 20)
  (def data {:data {:series [{:data [1 2 3 4]}]}})

  (def series (get-in data [:data :series]))


  (defn- process-data [slice-at chart-type data options]

    (let [series (get-in data [:data :series])
          ret    (for [d series]
                   (merge d default-options))]

      ;(prn "vari-pie process-data (data) "
      ;  " //// (ret) " ret)

      ret))

  (for [d series] d)

  (process-data slice-at chart-type
    data options)

  ())