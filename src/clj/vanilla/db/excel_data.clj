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
                   :E :organization
                   :C :start-time
                   :D :end-time})
  (def post-fn (fn [x] x))

  (load-workbook filename)

  (->> (load-workbook filename)
    (select-sheet  "Sat-Power-1000"))

  (->> (load-workbook filename)
    (select-sheet  "Sat-Power-1000")
    row-seq
    (drop 1)
    (take 1))

  (->> (load-workbook filename)
    (select-sheet "Sat-Power-1000")
    (select-columns {:A :satellite :B :freq :C :cp1 :D :cp2 :E :cp3})
    (drop 1)
    (take 5)
    (d/transact! conn))

  (->> (load-workbook filename)
    (select-sheet "Sat-Power-1000")
    (select-columns column-map))


  (->> (load-workbook filename)
    (select-sheet "SCN_NETWORK_CARRIER_VW")
    (select-columns {:A :satellite :B :rx-beam :C :rx-channel :D :tx-beam
                     :E :tx-channel :F :plan :G :mission-name :H :service
                     :R :data-rate})
    (drop 1))



  (->> (load-workbook filename)
    (select-sheet  "Sat-Power-1000")
    row-seq
    (remove nil?)
    (map cell-seq)
    (map #(map read-cell-value %)))



  ())