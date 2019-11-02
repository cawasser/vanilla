(ns vanilla.usage-24-hour-service)


(defn fetch-data []
  (prn "24-hour Usage Service")

  {:title       "24-hour Usage Data"
   :data-format :data-format/x-y-n
   :src/x-title "time"
   :src/y-title "tons"
   :src/keys    ["0000" "0100" "0200" "0300" "0400" "0600"
                 "0700" "0800" "0900" "1000" "1100"]
   :series      [{:name "Apples"
                  :data (take 12 (repeatedly #(rand-int 200)))}
                 {:name "Oranges"
                  :data (take 12 (repeatedly #(rand-int 200)))}
                 {:name "Pears"
                  :data (take 12 (repeatedly #(rand-int 200)))}
                 {:name "Grapes"
                  :data (take 12 (repeatedly #(rand-int 200)))}
                 {:name "Bananas"
                  :data (take 12 (repeatedly #(rand-int 200)))}]})
