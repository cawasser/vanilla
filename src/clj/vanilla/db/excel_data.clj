(ns vanilla.db.excel-data
  (:require [dk.ative.docjure.spreadsheet :refer :all]
            [datascript.core :as d]
            [clojure.tools.logging :as log]))



(def schema {})
(def conn (d/create-conn schema))

(def filename "public/excel/Demo.xlsx")

; drf: "data reduction factor"
(def drf 10)

(def excel-defs [{:sheet      "X-Beams"
                  :column-map {:A :band
                               :B :beam-id
                               :C :lat
                               :D :lon
                               :E :radius
                               :G :type}
                  :post-fn    (fn [x] x)}
                 {:sheet      "SCN_NETWORK_CARRIER_VW"
                  :column-map {:A :satellite-id
                               :B :tx-beam
                               :C :tx-channel
                               :D :rx-beam
                               :E :rx-channel
                               :F :plan
                               :G :mission-name
                               :H :service
                               :R :data-rate}
                  :post-fn    (fn [x] x)}
                 {:sheet      "Missions"
                  :column-map {:A :task-name
                               :B :organization
                               :C :start-time
                               :D :end-time}
                  :post-fn    (fn [x] x)}
                 {:sheet      "Terminals"
                  :column-map {:C :terminal-id
                               :E :lat
                               :F :lon
                               :M :satellite-id
                               :N :tx-beam
                               :O :tx-channel
                               :P :rx-beam
                               :Q :rx-channel}
                  :post-fn    (fn [x] x)}
                 {:sheet      "Sat-Power-1000"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}
                 {:sheet      "Sat-Power-2000"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}
                 {:sheet      "Sat-Power-3000"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}
                 {:sheet      "Sat-Power-4000"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}])



(defn- load-data [filename sheet column-map post-fn]
  (log/info "Loading " filename "/" sheet)
  (->> (load-workbook-from-resource filename)
    (select-sheet sheet)
    (select-columns column-map)
    (drop 1)
    post-fn
    (d/transact! conn)))


(defn init-from-excel []
  (log/info "Init From Excel")
  (doall
    (map (fn [{:keys [sheet column-map post-fn]}]
           (load-data filename sheet column-map post-fn))
      excel-defs)))





(comment
  (def sheet "Missions")
  (def column-map {:A :name
                   :B :organization
                   :C :start-time
                   :D :end-time})
  (def post-fn (fn [x] x))

  (load-workbook filename)

  (->> (load-workbook filename)
    (select-sheet "Missions"))

  (->> (load-workbook filename)
    (select-sheet "Missions")
    row-seq)

  (->> (load-workbook "resources/public/excel/Demo - 9102.xlsx")
    (select-sheet "Missions")
    (select-columns {:A :name :B :organization :C :start-time :D :end-time})
    (drop 1))

  (->> (load-workbook filename)
    (select-sheet "SCN_NETWORK_CARRIER_VW")
    (select-columns {:A :satellite :B :rx-beam :C :rx-channel :D :tx-beam
                     :E :tx-channel :F :plan :G :mission-name :H :service
                     :R :data-rate})
    (drop 1))


  (map (fn [{:keys [sheet column-map post-fn]}]
         [filename sheet column-map post-fn])
    excel-defs)


  ())