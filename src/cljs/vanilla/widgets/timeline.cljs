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


(def colors ["blue" "green" "red" "goldenrod" "purple"])


(defn parse-types-to-color [data]
  (let [types-set (into [] (into #{} (map #(:type %) (:data data))))
        ret       (assoc
                    data
                    :data (into []
                            (doall
                              (map (fn [{:keys [type] :as orig}]
                                     (assoc orig :color (get colors
                                                          (.indexOf types-set type))))
                                (:data data)))))]
    ret))

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

;  (prn "timeline" source-data)

  [:> TimeLine (merge {:style {:overflow :auto
                               :backgroundColor "#2F4F4F"}
                       :mode "year"
                       :config {:taskList {:task {:style {:backgroundColor "#2F4F4F"
                                                          :color  "white"}}}
                                :dataViewPort {:rows {:style {:backgroundColor "#2F4F4F"
                                                              :borderBottom "solid 0.5px #cfcfcd"}}
                                               :task {:showLabel true
                                                      :style {:borderRadius 1
                                                              :boxShadow "2px 2px 8px #888888"
                                                              :backgroundColor "#2F4F4F"}}}}}

                 (->> (:data (:data source-data))
                   ;parse-times
                   parse-types-to-color))])




(comment

  (def colors ["blue" "green" "red" "yellow"])

  (defn parse-types-to-color [data]
    (let [color-count (:type (:data data))]))

  (def types-set (into [] (into #{} (map #(:type %) (:data d)))))
  (def type "fixed")
  (assoc
    d
    :data (into []
            (doall
              (map (fn [{:keys [type] :as orig}]
                     (assoc orig :color (get colors (.indexOf types-set type))))
                (:data d)))))
  ())