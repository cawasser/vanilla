(ns vanilla.heatmap-service
  (:require [clojure.tools.logging :as log]
            [datascript.core :as d]
            [vanilla.db.materialized-view :as mv]))


(def lat (into [] (map str (keep #(if (not= 0 %) %) (range -80 80 10)))))
(def lon (into [] (map str (keep #(if (not= 0 %) %) (range -180 180 10)))))


(defn merge-data
  [[name data]]
  {:name         name
   :data         (->> data
                   (map (fn [{d :data}] d))
                   (remove nil?)
                   (sort-by (juxt (fn [x] (get x 0))
                              (fn [x] (get x 1))))
                   (into []))})

(defn- beam-data
  [[epoch beam-name lat lon data-rate]]

  {:epoch epoch
   :data  [lon lat data-rate]})


(defn get-beam-data
  [band]
  (mv/query-thread
    {:q-fn     (d/q '[:find ?epoch ?name ?lat ?lon ?data-rate
                      :where [?e :epoch ?epoch]
                      [?e :beam-id ?name]
                      [?e :lat ?lat]
                      [?e :lon ?lon]

                      [?b :epoch ?epoch]
                      [?b :rx-beam ?name]
                      [?b :data-rate ?data-rate]
                      :in $] @mv/conn)
     :map-fn   (map beam-data)
     :merge-fn merge-data}))


(defn- fill-data [d]
  (into []
    (concat [[-180 -80 0] [-180 80 0] [180 -80 0] [180 -80 0]] d)))

(defn fill-out-sparse-matrix [m]
  (assoc m
    :showInLegend true
    :type         :heatmap
    ;:categories   {:x lon
    ;               :y lat}
    :keys         ["x" "y" "value"]
    :data (fill-data (:data m))))



(defn fetch-data []
  (log/info "Heatmap Service")

  {:title       "Heatmap Data"
   :data-format :data-format/grouped-grid-n
   :series      (map fill-out-sparse-matrix (get-beam-data "Ka"))})


(comment

  (vanilla.subscription-manager/refresh-source :heatmap-data)

  ())


(comment
  (->>
    (d/q '[:find ?epoch ?name ?lat ?lon ?data-rate
           :where [?e :epoch ?epoch]
           [?e :beam-id ?name]
           [?e :lat ?lat]
           [?e :lon ?lon]

           [?b :epoch ?epoch]
           [?b :rx-beam ?name]
           [?b :data-rate ?data-rate]
           :in $] @mv/conn)
    (map (fn [[epoch beam-name lat lon data-rate]]
           {:epoch     epoch
            :lat       lat
            :lon       lon
            :data-rate data-rate})))

  (def beam-data (get-beam-data "Ka"))


  (mv/query-thread
    {:q-fn   (d/q '[:find ?epoch ?name ?lat ?lon ?data-rate
                    :where [?e :epoch ?epoch]
                    [?e :beam-id ?name]
                    [?e :lat ?lat]
                    [?e :lon ?lon]

                    [?b :epoch ?epoch]
                    [?b :rx-beam ?name]
                    [?b :data-rate ?data-rate]
                    :in $] @mv/conn)
     :map-fn (map (fn [[epoch beam-name lat lon data-rate]]
                    {:epoch epoch
                     :data  [lat lon data-rate]}))})



  (def d [{:name "220800Z JUL 2020",
           :data #{{:epoch "220800Z JUL 2020", :data [36.58 -76.2 2048.0]}
                   {:epoch "220800Z JUL 2020", :data [39.74 -85.04 2048.0]}
                   {:epoch "220800Z JUL 2020", :data [36.7 -126.3 2048.0]}}}
          {:name "221200Z JUL 2020",
           :data #{{:epoch "221200Z JUL 2020", :data [36.7 -126.3 2048.0]}
                   {:epoch "221200Z JUL 2020", :data [36.58 -76.2 2048.0]}
                   {:epoch "221200Z JUL 2020", :data [39.74 -85.04 2048.0]}}}
          {:name "221600Z JUL 2020",
           :data #{{:epoch "221600Z JUL 2020", :data [36.7 -126.3 2048.0]}
                   {:epoch "221600Z JUL 2020", :data [39.74 -85.04 2048.0]}
                   {:epoch "221600Z JUL 2020", :data [36.58 -76.2 2048.0]}}}
          {:name "222000Z JUL 2020",
           :data #{{:epoch "222000Z JUL 2020", :data [36.7 -126.3 2048.0]}
                   {:epoch "222000Z JUL 2020", :data [39.74 -85.04 2048.0]}}}])

  d

  (for [{:keys [name data]} d]
    {:name name
     :keys ["x" "y" "value"]
     :data (->> data
             (map (fn [{d :data}] d))
             (remove nil?)
             (sort-by (juxt (fn [x] (get x 0))
                        (fn [x] (get x 1))))
             (into []))})

  (->>
    (mv/query-thread
      {:q-fn   (d/q '[:find ?epoch ?name ?lat ?lon ?data-rate
                      :where [?e :epoch ?epoch]
                      [?e :beam-id ?name]
                      [?e :lat ?lat]
                      [?e :lon ?lon]

                      [?b :epoch ?epoch]
                      [?b :rx-beam ?name]
                      [?b :data-rate ?data-rate]
                      :in $] @mv/conn)
       :map-fn (map (fn [[epoch name lat lon data-rate]]
                      {:epoch epoch
                       :data  [lat lon data-rate]}))})

    (map (fn [{:keys [name data]}]
           {:name name
            :keys ["x" "y" "value"]
            :data (->> data
                    (map (fn [{d :data}] d))
                    (remove nil?)
                    (sort-by (juxt (fn [x] (get x 0))
                               (fn [x] (get x 1))))
                    (into []))})))

  (def q-fn (d/q '[:find ?epoch ?name ?lat ?lon ?data-rate
                   :where [?e :epoch ?epoch]
                   [?e :beam-id ?name]
                   [?e :lat ?lat]
                   [?e :lon ?lon]

                   [?b :epoch ?epoch]
                   [?b :rx-beam ?name]
                   [?b :data-rate ?data-rate]
                   :in $] @mv/conn))
  (def map-fn (map (fn [[epoch name lat lon data-rate]]
                     {:epoch epoch
                      :data  [lat lon data-rate]})))

  (def merge-fn (fn [[k v]] {:name k :data v}))

  (->> q-fn
    (sequence map-fn)
    (group-by :epoch)
    sort
    (map merge-fn))

  (def d [[10 10 2048] [20 20 4096]])
  (concat [[-80 -180 0] [-80 180 0] [80 -180 0] [-80 -180 0]] d)

  ())
