(ns vanilla.widgets.timeline
  (:require ["react-gantt-timeline" :default TimeLine]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(def built-in-formatter (f/formatters :date-time-no-ms))

;(def local-data {:data  [{:id  1 :start (t/now)
;                          :end (t/plus (t/now) (t/months 1)) :name "Demo Task 1"}
;                         {:id  2 :start (t/plus (t/now) (t/months 1))
;                          :end (t/plus (t/now) (t/months 1) (t/weeks 2) (t/days 4) (t/hours 9)) :name "Demo Task 2"}]
;
;                 :links [{:id 1 :start 1 :end 2}
;                         {:id 2 :start 1 :end 3}]})
;

(defn parse-times [data]
  (let [ret (assoc
              data
              :data (into []
                      (doall
                       (map (fn [{:keys [start end] :as orig}]
                              (-> orig
                                (assoc :start (f/parse built-in-formatter start))
                                (assoc :end (f/parse built-in-formatter end))))
                         (:data data)))))]
    ret))



(defn make-widget
  [name source-data options]

  [:> TimeLine (parse-times (:data (:data source-data)))])