(ns vanilla.sankey-service
  (:require [clojure.tools.logging :as log]))


(defn val [] (inc (rand-int 10)))

(defn fetch-data []
  (log/info "Sankey Service")

  {:title "Sankey Data"
   :data-format :data-format/from-to-n
   :series [{:keys ["from" "to" "weight"]
             :data [["Brazil" "Portugal" (val)]
                    ["Brazil" "France" (val)]
                    ["Brazil" "Spain" (val)]
                    ["Brazil" "England" (val)]
                    ["Canada" "Portugal" (val)]
                    ["Canada" "France" (val)]
                    ["Canada" "England" (val)]
                    ["Mexico" "Portugal" (val)]
                    ["Mexico" "France" (val)]
                    ["Mexico" "Spain" (val)]
                    ["Mexico" "England" (val)]
                    ["USA" "Portugal" (val)]
                    ["USA" "France" (val)]
                    ["USA" "Spain" (val)]
                    ["USA" "England" (val)]
                    ["Portugal" "Angola" (val)]
                    ["Portugal" "Senegal" (val)]
                    ["Portugal" "Morocco" (val)]
                    ["Portugal" "South Africa" (val)]
                    ["France" "Angola" (val)]
                    ["France" "Senegal" (val)]
                    ["France" "Mali" (val)]
                    ["France" "Morocco" (val)]
                    ["France" "South Africa" (val)]
                    ["Spain" "Senegal" (val)]
                    ["Spain" "Morocco" (val)]
                    ["Spain" "South Africa" (val)]
                    ["England" "Angola" (val)]
                    ["England" "Senegal" (val)]
                    ["England" "Morocco" (val)]
                    ["England" "South Africa" (val)]
                    ["South Africa" "China" (val)]
                    ["South Africa" "India" (val)]
                    ["South Africa" "Japan" (val)]
                    ["Angola" "China" (val)]
                    ["Angola" "India" (val)]
                    ["Angola" "Japan" (val)]
                    ["Senegal" "China" (val)]
                    ["Senegal" "India" (val)]
                    ["Senegal" "Japan" (val)]
                    ["Mali" "China" (val)]
                    ["Mali" "India" (val)]
                    ["Mali" "Japan" (val)]
                    ["Morocco" "China" (val)]
                    ["Morocco" "India" (val)]
                    ["Morocco" "Japan" (val)]]}]})

