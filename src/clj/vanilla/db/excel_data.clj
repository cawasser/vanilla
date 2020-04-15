(ns vanilla.db.excel-data
  (:require [dk.ative.docjure.spreadsheet :refer :all]
            [datascript.core :as d]))



(def schema {})
(def conn (d/create-conn schema))



(defn load-data [filename sheet column-map post-fn]
  (->> (load-workbook filename)
    (select-sheet sheet)
    (select-columns column-map)
    (drop 1)
    post-fn
    (d/transact! conn)))


