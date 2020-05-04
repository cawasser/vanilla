(ns vanilla.task-service
  (:require [clojure.tools.logging :as log]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]
            [clojure.edn :as edn]))


(def sheet "Missions")
(def column-map {:A :name
                 :B :organization
                 :C :start-time
                 :D :end-time})
(def post-fn (fn [x] x))


(defn- get-data []
  (excel/load-data excel/filename sheet column-map post-fn)

  (->> (d/q '[:find ?name ?organization ?start-time ?end-time
              :where [?e :name ?name]
              [?e :organization ?organization]
              [?e :start-time ?start-time]
              [?e :end-time ?end-time]]
         @excel/conn)
    (map (fn [[name organization start-time end-time]]
           {:id    name
            :name  name
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

  ())