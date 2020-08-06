(ns vanilla.telemetry-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.sv-telemetry :as svt]))


(defn reformat [raw field]
  {:name (name field)
   :data (merge-with clojure.set/union
           (map (fn [[k v]]
                  {:name k
                   :keys ["x" "y"]
                   :data (map (juxt #(get % field) :Spacecraft-Time ) v)})
             raw))})



(defn get-data []
  (let [raw @svt/sv-telemetry]
    (map #(reformat raw %) [:TCS_EP_NXETANK_TEMP_1-TREND_AVG
                            :TCS_EP_NXETANK_TEMP_2-TREND_AVG])))


(defn fetch-data []
  (log/info "Telemetry")

  {:title       "SV Telemetry"
   :src/x-title "Data"
   :src/y-title "Measurement"
   :data-format :data-format/grouped-x-y
   :series      (get-data)})


(comment

  (vanilla.subscription-manager/refresh-source :telemetry-service)


  (identity "SC1")

  ())


{:name "131807Z JUL 2020", :showInLegend true, :type :sankey, :keys ["from" "to" "weight"], :data ()}

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; data re-formatting
;
(comment
  (def raw @svt/sv-telemetry)

  (map key raw)
  (map val raw)

  (map (fn [[k v]]
         (map count v))
    raw)

  (map (fn [[k v]]
         {k (map (juxt :Spacecraft-Time
                   :TCS_EP_NXETANK_TEMP_1-TREND_AVG) v)})
    raw)

  (def field :TCS_EP_NXETANK_TEMP_1-TREND_AV)
  (map (fn [[k v]]
         {(name k) (map (juxt :Spacecraft-Time #(get % field)) v)})
    raw)


  {"TCS_EP_NXETANK_TEMP_1-TREND_AVG"
   (merge-with clojure.set/union
     (map (fn [[k v]]
           {k (map (juxt :Spacecraft-Time
                       :TCS_EP_NXETANK_TEMP_1-TREND_AVG) v)})
      raw))}



  (defn reformat [raw field]
    {(name field)
     (merge-with clojure.set/union
       (map (fn [[k v]]
              {k (map (juxt :Spacecraft-Time #(get % field)) v)})
         raw))})

  (reformat raw :TCS_EP_NXETANK_TEMP_1-TREND_AVG)

  (map #(reformat raw %) [:TCS_EP_NXETANK_TEMP_1-TREND_AVG
                          :TCS_EP_NXETANK_TEMP_2-TREND_AVG])


  ())