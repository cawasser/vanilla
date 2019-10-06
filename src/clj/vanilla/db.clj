(ns vanilla.db)


(defn spectrum-data []
  [{:name   "trace-1"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}
   {:name   "trace-2"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}
   {:name   "trace-3"
    :values (into []
                  (take 200
                        (repeatedly #(+ 5.0
                                        (rand 5)))))}])


(defn usage-data []
  [["Apples" (rand 100)]
   ["Pears" (rand 100)]
   ["Oranges" (rand 100)]
   ["Plums" (rand 100)]
   ["Bananas" (rand 100)]
   ["Peaches" (rand 100)]
   ["Prunes" (rand 100)]
   ["Avocados" (rand 100)]])

