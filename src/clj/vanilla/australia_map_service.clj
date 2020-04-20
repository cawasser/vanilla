(ns vanilla.australia-map-service
  (:require [clojure.tools.logging :as log]))

(defn fetch-data []
  (log/info "Australia Map Service")

  {:title "Australia Data"
   :data-format :data-format/lat-lon-label

   :data [{:name "Canberra",
           :lat -35.2809,
           :lon 149.13
           :dataLabels {:align "left" :x 5 :verticalAlign "middle"}}
          {:name "Brisbane",
           :lat -27.47012,
           :lon 153.021072}
          {:name "Geraldton",
           :lat -28.782387,
           :lon 114.607513}
          {:name "Wagga Wagga",
           :lat -35.117275,
           :lon 147.356522,
           :dataLabels {:align "right" :y 5 :verticalAlign "middle"}}]})

