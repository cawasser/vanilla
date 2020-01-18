(ns vanilla.usage-24-hour-service)


(defn fetch-data []
  (prn "24-hour Usage Service")

  {:title       "24-hour Usage Data"
   :data-format :data-format/x-y-n
   :src/x-title "time"
   :src/y-title "tons"
   :series      [{:name "Apples"
                  :keys ["0000" "0100" "0200" "0300" "0400" "0600"
                         "0700" "0800" "0900" "1000" "1100"]
                  :data (take 12 (repeatedly #(rand-int 200)))}
                 {:name "Oranges"
                  :keys ["0000" "0100" "0200" "0300" "0400" "0600"
                         "0700" "0800" "0900" "1000" "1100"]
                  :data (take 12 (repeatedly #(rand-int 200)))}
                 {:name "Pears"
                  :keys ["0000" "0100" "0200" "0300" "0400" "0600"
                         "0700" "0800" "0900" "1000" "1100"]
                  :data (take 12 (repeatedly #(rand-int 200)))}
                 {:name "Grapes"
                  :keys ["0000" "0100" "0200" "0300" "0400" "0600"
                         "0700" "0800" "0900" "1000" "1100"]
                  :data (take 12 (repeatedly #(rand-int 200)))}
                 {:name "Bananas"
                  :keys ["0000" "0100" "0200" "0300" "0400" "0600"
                         "0700" "0800" "0900" "1000" "1100"]
                  :data (take 12 (repeatedly #(rand-int 200)))}]})
