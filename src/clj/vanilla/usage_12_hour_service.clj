(ns vanilla.usage-12-hour-service)


(defn- make-map [name xs max-qty]
  (for [y (range (count xs))]
    {:name name
     :y (get xs y)
     :z (rand max-qty)}))



(defn fetch-data []
  (prn "12-hour Usage Service")

  {:title       "12-hour Usage Data"
   :data-format :data-format/label-y-n
   :src/x-title "time"
   :src/y-title "tons"
   :src/keys ["0000h" "0100h" "0200h" "0300h" "0400h" "0600h"
              "0700h" "0800h" "0900h" "1000h" "1100h"]
   :series      [{:name "Apples"
                  :keys ["0000h" "0100h" "0200h" "0300h" "0400h" "0600h"
                         "0700h" "0800h" "0900h" "1000h" "1100h"]
                  :data (into [] (take 12 (repeatedly #(rand-int 200))))}
                 {:name "Oranges"
                  :keys ["0000h" "0100h" "0200h" "0300h" "0400h" "0600h"
                         "0700h" "0800h" "0900h" "1000h" "1100h"]
                  :data (into [] (take 12 (repeatedly #(rand-int 200))))}
                 {:name "Pears"
                  :keys ["0000h" "0100h" "0200h" "0300h" "0400h" "0600h"
                         "0700h" "0800h" "0900h" "1000h" "1100h"]
                  :data (into [] (take 12 (repeatedly #(rand-int 200))))}
                 {:name "Grapes"
                  :keys ["0000h" "0100h" "0200h" "0300h" "0400h" "0600h"
                         "0700h" "0800h" "0900h" "1000h" "1100h"]
                  :data (into [] (take 12 (repeatedly #(rand-int 200))))}
                 {:name "Bananas"
                  :keys ["0000h" "0100h" "0200h" "0300h" "0400h" "0600h"
                         "0700h" "0800h" "0900h" "1000h" "1100h"]
                  :data (into [] (take 12 (repeatedly #(rand-int 200))))}]})



(comment

  (make-map "Apple" ["0000h" "0100h"] 10)

  (range (count ["0000h" "0100h"]))

  (get ["0000h" "0100h"] 0)

  ())