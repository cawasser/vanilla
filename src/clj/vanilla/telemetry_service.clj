(ns vanilla.telemetry-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.sv-telemetry :as svt]))

(def temp "*C")
(def pressure "psi")

(def labels {:TCS_PT1_GHE_TEMP-TREND_AVG temp
             :PSS_PT3_OX_TRANSDUCER_1-TREND_AVG pressure
             :PSS_PT4_OX_TRANSDUCER_2-TREND_AVG pressure
             :TCS_OX_TANK_TEMP_1-TREND_AVG temp
             :TCS_OX_TANK_TEMP_2-TREND_AVG temp
             :TCS_OX_TANK_TEMP_3-TREND_AVG temp
             :TCS_OX_TANK_TEMP_4-TREND_AVG temp
             :TCS_OX_TANK_TEMP_5-TREND_AVG temp
             :TCS_OX_TANK_TEMP_6-TREND_AVG temp
             :PSS_PT2_FUEL_TRANSDUCER_1-TREND_AVG pressure
             :PSS_PT5_FUEL_TRANSDUCER_2-TREND_AVG pressure})
             ;:TCS_FUEL_TANK_TEMP_1-TREND_AVG temp
             ;:TCS_FUEL_TANK_TEMP_2-TREND_AVG temp
             ;:TCS_FUEL_TANK_TEMP_3-TREND_AVG temp
             ;:TCS_FUEL_TANK_TEMP_4-TREND_AVG temp
             ;:TCS_FUEL_TANK_TEMP_5-TREND_AVG temp
             ;:TCS_FUEL_TANK_TEMP_6-TREND_AVG temp
             ;:PSS_PT1_GHE_TRANSDUCER-TREND_AVG pressure
             ;:TCS_XPT3_XE_MFLD_TEMP-TREND_AVG pressure
             ;:HPS_XPT1_XETANK_TRANSDUCER_1-TREND_AVG pressure
             ;:HPS_XPT2_XETANK_TRANSDUCER_2-TREND_AVG pressure
             ;:TCS_EP_NXETANK_TEMP_1-TREND_AVG pressure
             ;:TCS_EP_NXETANK_TEMP_2-TREND_AVG pressure
             ;:TCS_EP_NXETANK_TEMP_3-TREND_AVG pressure
             ;:TCS_EP_NXETANK_TEMP_4-TREND_AVG pressure
             ;:TCS_EP_NXETANK_TEMP_5-TREND_AVG pressure
             ;:TCS_EP_NXETANK_TEMP_6-TREND_AVG pressure
             ;:HPS_XPT3_XEMFLD_TRANSDUCER_1-TREND_AVG pressure
             ;:HPS_XPT4_XEMFLD_TRANSDUCER_2-TREND_AV pressure})


(defn reformat [raw field]
  {:name (name field)
   :labels ["Date" (get labels field)]
   :data (merge-with clojure.set/union
           (map (fn [[k v]]
                  {:name k
                   :keys ["x" "y"]
                   :data ;(take-nth 6
                           (map (juxt :Spacecraft-Time #(get % field)) v)})
             raw))})



(defn get-data []
  (let [raw @svt/sv-telemetry]
    (map #(reformat raw %) (keys labels))))


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