(ns vanilla.stoplight-service
  (:require [clojure.tools.logging :as log]))


(def idents ["item-00" "item-01" "item-02" "item-03" "item-04"
             "item-05" "item-06" "item-07" "item-08" "item-09"
             "item-10" "item-11" "item-12" "item-13" "item-14"
             "item-15" "item-16" "item-17" "item-18" "item-19"
             "item-20" "item-21" "item-22" "item-23" "item-24"])


(defn- code [x]
  (cond
    (<= x 50) :up
    (and (< 50 x) (<= x 80)) :inactive
    (and (< 80 x) (<= x 93)) :warning
    :else :fault))


(defn fetch-data []
  (log/info "Stoplight Service")

  {:title "Health and Status Data"
   :data-format :data-format/entity
   :src/keys [:id :status]
   :series (let [v (map (fn [_]
                          (->> 100 rand code))
                        (range 25))]
             (sort
               (into []
                     (zipmap idents v))))})


(comment

  (vanilla.subscription-manager/refresh-source :health-and-status-data)

  ())
