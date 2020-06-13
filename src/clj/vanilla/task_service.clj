(ns vanilla.task-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.materialized-view :as mv]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.edn :as edn]))




(defn fetch-data []
  (log/info "Task Service")

  {:title       "Task Data"
   :data-format :data-format/task-link
   :data        {:data (->> (mv/mission-query)
                         (sort-by :name)
                         (into []))}})




(comment

  (take 5 (mv/mission-query))

  (vanilla.subscription-manager/refresh-source :task-service)

  ())