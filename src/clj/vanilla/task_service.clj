(ns vanilla.task-service
  (:require [clojure.tools.logging :as log]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]
            [clojure.edn :as edn]))




(defn- get-data []
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :where [?e :task-name]]
         @excel/conn)
    (map (fn [{:keys [task-name organization start-time end-time]}]
           {:id    task-name
            :name  task-name
            :type  organization
            :start start-time
            :end   end-time}))
    (into [])
    (sort-by :name)))


(defn fetch-data []
  (log/info "Task Service")

  {:title       "Task Data"
   :data-format :data-format/task-link

   :data        {:data (get-data)}})




(comment
  (excel/load-data excel/filename sheet column-map post-fn)

  (->> (d/q '[:find ?name ?organization ?start-time ?end-time
              :where [?e :name ?name]
              [?e :organization ?organization]
              [?e :start-time ?start-time]
              [?e :end-time ?end-time]]
         @excel/conn)
    (map (fn [[name organization start-time end-time]]
           {:id    name
            :type  organization
            :start start-time
            :end   end-time}))
    (into []))

  (get-data)

  (d/q '[:find [(pull ?e [*]) ...]
         :where [?e :task-name]]
    @excel/conn)

  ())