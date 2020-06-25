(ns vanilla.task-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.materialized-view :as mv]
            [datascript.core :as d]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.edn :as edn]))


(def datetime-formatter (f/formatter "ddHHmmz MMM yyyy"))

(defn ->GMT [x] (clojure.string/replace x #"Z" "GMT"))
(defn ->Z [x] (clojure.string/replace x #"UTC" "Z"))
(defn strip-tz [x] (clojure.string/replace x #"UTC|GMT|Z" ""))



(defn- mission-data
  "build the data needed to present a Mission in a timeline widget"

  [{:keys [mission-id plan-id start-epoch end-epoch]}]

  {:id    mission-id :name mission-id :type plan-id
   :start (f/parse datetime-formatter (->GMT start-epoch))
   :end   (f/parse datetime-formatter (->GMT end-epoch))})



(defn- merge-mission
  "combine all the mission entries in the argument into a single 'mission'

  returns map of one mission entry:

  :id, :name, :type from the first item

  :start   earliest of all the :start values (converted to strings for transmission)
  :end     latest of all the :end values (converted to strings for transmission)

  ASSUMPTIONS

  - all the :id :name: and :type values are the same"

  [args]
  (if (seq args)
    (let [a (first args)]
      {:id    (:id a)
       :name  (:name a)
       :type  (:type a)
       :start (->> (map #(:start-set %) args)
                (apply clojure.set/union)
                sort
                first
                .toDate)
       :end   (->> (map #(:end-set %) args)
                (apply clojure.set/union)
                sort
                last
                .toDate)})
    {}))



(defn get-mission-data
  ""

  []
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :where [?e :mission-id]]
         @mv/conn)
    (map mission-data)
    (map #(assoc %
            :start-set #{(:start %)}
            :end-set #{(:end %)}))
    (group-by :id)
    (map (fn [[k v]] (merge-mission v)))))


(defn fetch-data []
  (log/info "Task Service")

  {:title       "Task Data"
   :data-format :data-format/task-link
   :data        {:data (->> (get-mission-data)
                         (sort-by :name)
                         (into []))}})




(comment

  (get-mission-data)

  (vanilla.subscription-manager/refresh-source :task-service)

  ())