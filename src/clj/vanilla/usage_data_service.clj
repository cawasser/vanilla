(ns vanilla.usage-data-service
  (:require [clojure.tools.logging :as log]))

(defn usage-data []
  [{:keys ["name" "y"]
    :data [["Apples" (rand 100)]
           ["Pears" (rand 100)]
           ["Oranges" (rand 100)]
           ["Plums" (rand 100)]
           ["Bananas" (rand 100)]
           ["Peaches" (rand 100)]
           ["Prunes" (rand 100)]
           ["Avocados" (rand 100)]]}])

(defn fetch-data []
  (log/info "Usage Data")

  {:title       "Usage Data"
   :data-format :data-format/label-y
   :src/x-title "Fruit"
   :src/y-title "Qty."
   :series      (usage-data)})

