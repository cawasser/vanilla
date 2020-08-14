(ns vanilla.db.sv-telemetry
  (:require [dk.ative.docjure.spreadsheet :refer :all]
            [vanilla.db.excel-data :as excel]
            [clojure.tools.logging :as log]))


(def sv-telemetry (atom {}))

(def filename "public/excel/NOAA-Telemetry.xlsx")
(def column-map [{:sheet      "SC1"
                  :column-map {:A  :Spacecraft-Time
                               :B  :TCS_PT1_GHE_TEMP-TREND_AVG
                               :C  :PSS_PT3_OX_TRANSDUCER_1-TREND_AVG
                               :D  :PSS_PT4_OX_TRANSDUCER_2-TREND_AVG
                               :E  :TCS_OX_TANK_TEMP_1-TREND_AVG
                               :F  :TCS_OX_TANK_TEMP_2-TREND_AVG
                               :G  :TCS_OX_TANK_TEMP_3-TREND_AVG
                               :H  :TCS_OX_TANK_TEMP_4-TREND_AVG
                               :I  :TCS_OX_TANK_TEMP_5-TREND_AVG
                               :J  :TCS_OX_TANK_TEMP_6-TREND_AVG
                               :K  :PSS_PT2_FUEL_TRANSDUCER_1-TREND_AVG
                               :L  :PSS_PT5_FUEL_TRANSDUCER_2-TREND_AVG
                               :M  :TCS_FUEL_TANK_TEMP_1-TREND_AVG
                               :N  :TCS_FUEL_TANK_TEMP_2-TREND_AVG
                               :O  :TCS_FUEL_TANK_TEMP_3-TREND_AVG
                               :P  :TCS_FUEL_TANK_TEMP_4-TREND_AVG
                               :Q  :TCS_FUEL_TANK_TEMP_5-TREND_AVG
                               :R  :TCS_FUEL_TANK_TEMP_6-TREND_AVG
                               :S  :PSS_PT1_GHE_TRANSDUCER-TREND_AVG
                               :T  :TCS_XPT3_XE_MFLD_TEMP-TREND_AVG
                               :U  :HPS_XPT1_XETANK_TRANSDUCER_1-TREND_AVG
                               :V  :HPS_XPT2_XETANK_TRANSDUCER_2-TREND_AVG
                               :W  :TCS_EP_NXETANK_TEMP_1-TREND_AVG
                               :Y  :TCS_EP_NXETANK_TEMP_2-TREND_AVG
                               :Z  :TCS_EP_NXETANK_TEMP_3-TREND_AVG
                               :AA :TCS_EP_NXETANK_TEMP_4-TREND_AVG
                               :AB :TCS_EP_NXETANK_TEMP_5-TREND_AVG
                               :AC :TCS_EP_NXETANK_TEMP_6-TREND_AVG
                               :AD :HPS_XPT3_XEMFLD_TRANSDUCER_1-TREND_AVG
                               :AE :HPS_XPT4_XEMFLD_TRANSDUCER_2-TREND_AVG}
                  :post-fn    (fn [x] x)}
                 {:sheet      "SC2"
                  :column-map {:A  :Spacecraft-Time
                               :B  :TCS_PT1_GHE_TEMP-TREND_AVG
                               :C  :PSS_PT3_OX_TRANSDUCER_1-TREND_AVG
                               :D  :PSS_PT4_OX_TRANSDUCER_2-TREND_AVG
                               :E  :TCS_OX_TANK_TEMP_1-TREND_AVG
                               :F  :TCS_OX_TANK_TEMP_2-TREND_AVG
                               :G  :TCS_OX_TANK_TEMP_3-TREND_AVG
                               :H  :TCS_OX_TANK_TEMP_4-TREND_AVG
                               :I  :TCS_OX_TANK_TEMP_5-TREND_AVG
                               :J  :TCS_OX_TANK_TEMP_6-TREND_AVG
                               :K  :PSS_PT2_FUEL_TRANSDUCER_1-TREND_AVG
                               :L  :PSS_PT5_FUEL_TRANSDUCER_2-TREND_AVG
                               :M  :TCS_FUEL_TANK_TEMP_1-TREND_AVG
                               :N  :TCS_FUEL_TANK_TEMP_2-TREND_AVG
                               :O  :TCS_FUEL_TANK_TEMP_3-TREND_AVG
                               :P  :TCS_FUEL_TANK_TEMP_4-TREND_AVG
                               :Q  :TCS_FUEL_TANK_TEMP_5-TREND_AVG
                               :R  :TCS_FUEL_TANK_TEMP_6-TREND_AVG
                               :S  :PSS_PT1_GHE_TRANSDUCER-TREND_AVG
                               :T  :TCS_XPT3_XE_MFLD_TEMP-TREND_AVG
                               :U  :HPS_XPT1_XETANK_TRANSDUCER_1-TREND_AVG
                               :V  :HPS_XPT2_XETANK_TRANSDUCER_2-TREND_AVG
                               :W  :TCS_EP_NXETANK_TEMP_1-TREND_AVG
                               :Y  :TCS_EP_NXETANK_TEMP_2-TREND_AVG
                               :Z  :TCS_EP_NXETANK_TEMP_3-TREND_AVG
                               :AA :TCS_EP_NXETANK_TEMP_4-TREND_AVG
                               :AB :TCS_EP_NXETANK_TEMP_5-TREND_AVG
                               :AC :TCS_EP_NXETANK_TEMP_6-TREND_AVG
                               :AD :HPS_XPT3_XEMFLD_TRANSDUCER_1-TREND_AVG
                               :AE :HPS_XPT4_XEMFLD_TRANSDUCER_2-TREND_AVG}
                  :post-fn    (fn [x] x)}])



(defn init-telemetry [filename defs]
  (log/info "Init Telemetry" filename)

  (try
    (with-open [workbook (load-workbook-from-resource filename)]
      (doall
        (map (fn [{:keys [sheet column-map post-fn]}]
               (->>
                 (excel/load-data workbook sheet column-map post-fn)
                 ;(take 3)
                 (swap! sv-telemetry assoc sheet)))
          defs)))
    (catch Exception e (log/error "Exception: " (.getMessage e)))
    (finally (log/info "Telemetry file" filename "loaded!")))
  ())



;;;;;;;;;;;;;;;;;;;;;;;;
;
; load telemetry
;
(comment
  (init-telemetry "public/excel/NOAA-Telemetry.xlsx"
    column-map)

  (swap! sv-telemetry assoc "SC1"
    (take 2
      (with-open [workbook (load-workbook-from-resource filename)]
        (excel/load-data workbook "SC1"
          (:column-map (first column-map))
          (:post-fn (first column-map))))))




  (with-open [workbook (load-workbook-from-resource filename)]
    (doall
      (map (fn [{:keys [sheet column-map post-fn]}]
             (->>
               (excel/load-data workbook sheet column-map post-fn)
               (swap! sv-telemetry assoc sheet)))
        (take 1 column-map))))


  @sv-telemetry



  ())