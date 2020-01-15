(ns vanilla.usage-data-service)

(defn usage-data []
  [{:keys ["name" "y" "selected" "sliced"]
    :data [["Apples" (rand 100)]
           ["Pears" (rand 100)]
           ["Oranges" (rand 100)]
           ["Plums" (rand 100)]
           ["Bananas" (rand 100)]
           ["Peaches" (rand 100)]
           ["Prunes" (rand 100)]
           ["Avocados" (rand 100)]]}])

(defn fetch-data []
  (prn "Usage Data")

  {:title       "Usage Data"
   :data-format :data-format/name-y
   :src/x-title "Fruit"
   :src/y-title "Qty."
   :series      (usage-data)})

