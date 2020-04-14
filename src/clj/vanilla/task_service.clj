(ns vanilla.task-service
  (:require [clojure.tools.logging :as log]
            [clj-time.core :as t]
            [clj-time.format :as f]))



(def built-in-formatter (f/formatters :date-time-no-ms))

(defn make-task
  [id start end]

  (let [s (let [[months days hours] start]
            (t/plus (t/now) (t/months months) (t/days days) (t/hours hours)))
        e (let [[months days hours] end]
            (t/plus s (t/months months) (t/days days) (t/hours hours)))]

    {:id    id
     :name  (str "Task " id)
     :start (f/unparse built-in-formatter s)
     :end   (f/unparse built-in-formatter e)}))

(def tasks [[[0 0 0] [1 0 0]]
            [[0 1 0] [1 0 0]]
            [[0 2 0] [1 0 0]]
            [[0 3 0] [1 0 0]]
            [[0 4 0] [1 0 0]]
            [[0 5 0] [1 0 0]]
            [[0 6 0] [1 0 0]]])

(defn make-tasks
  [tasks]

  (into []
    (for [[idx [start end]] (map-indexed vector tasks)]
      (make-task idx start end))))



(defn make-links
  [tasks num-links])


(defn fetch-data []
  (log/info "Task Service")

  {:title       "Task Data"
   :data-format :data-format/task-link

   :data        {:data  (make-tasks tasks)

                 :links [{:id 1 :start 1 :end 2}
                         {:id 2 :start 1 :end 3}]}})



(comment
  (def start [2 4 13])
  (def end [1 3 2])

  {:start (let [[months days hours] start]
            (t/plus (t/now) (t/months months) (t/days days) (t/hours hours)))}

  (def s (let [[months days hours] start]
           (t/plus (t/now) (t/months months) (t/days days) (t/hours hours))))
  (def e (let [[months days hours] end]
           (t/plus s (t/months months) (t/days days) (t/hours hours))))


  (f/unparse built-in-formatter s)
  (make-tasks tasks)

  (fetch-data)

  (f/show-formatters)


  (def d {:data  (make-tasks tasks)
          :links [{:id 1 :start 1 :end 2}
                  {:id 2 :start 1 :end 3}]})
  d

  (map (fn [{:keys [start end] :as orig}]
         (-> orig
           (assoc :start (f/parse start))
           (assoc :end (f/parse start))))
    (:data d))

  (f (:data d))

  ())