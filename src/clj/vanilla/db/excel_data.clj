(ns vanilla.db.excel-data
  (:require [dk.ative.docjure.spreadsheet :refer :all]
            [datascript.core :as d]))



(def schema {})
(def conn (d/create-conn schema))

(def filename "resources/public/excel/Demo.xlsx")


(defn load-data [filename sheet column-map post-fn]
  (->> (load-workbook filename)
    (select-sheet sheet)
    (select-columns column-map)
    (drop 1)
    post-fn
    (d/transact! conn)))




(comment
  (def sheet "Missions")
  (def column-map {:A :name
                   :B :organization
                   :C :start-time
                   :D :end-time})
  (def post-fn (fn [x] x))

  (load-workbook filename)

  (->> (load-workbook filename)
    (select-sheet  "Missions"))

  (->> (load-workbook filename)
    (select-sheet  "Missions")
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

  ())