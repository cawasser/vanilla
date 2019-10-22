(ns vanilla.stoplight-service)


(def idents ["item-00" "item-01" "item-02" "item-03" "item-04"
             "item-05" "item-06" "item-07" "item-08" "item-09"
             "item-10" "item-11" "item-12" "item-13" "item-14"
             "item-15" "item-16" "item-17" "item-18" "item-19"
             "item-20" "item-21" "item-22" "item-23" "item-24"])


(defn- code [x]
  (cond
    (< x 3) :up
    (and (< 3 x)
         (< x 7)) :warning
    :else :fault))


(defn fetch-data []
  (prn "Stoplight Service")

  {:title "Health and Status Data"
   :keys [:id :status]
   :data (let [v (map (fn [_]
                        (->> 10 rand code))
                      (range 25))]
           (sort
             (into []
                   (zipmap idents v))))})


