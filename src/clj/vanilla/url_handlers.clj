(ns vanilla.url-handlers
  (:require [vanilla.db.core :as db]))


;;;;;;;;;;;;;;;;
;
; HTML endpoint handlers


(defn get-services []
  (db/get-services db/vanilla-db))


(def from-sqlite {:id :key :data_source :data-source :data_grid :data-grid})
(def to-sqlite {:key :id :data-source :data_source :data-grid :data_grid})

; fixup the key changes caused by SQLite with rename-keys
; :id -> :key
; :data_source -> :data-source
; :data_grid -> :data-grid
(defn get-layout []
  (map #(clojure.set/rename-keys % from-sqlite) (db/get-layout db/vanilla-db)))

(defn save-layout [new-layout]
  (let [renamed (map #(clojure.set/rename-keys % to-sqlite) new-layout)]
    (prn "SAVE-LAYOUT route: " new-layout
      " //// renames " renamed)
    (db/save-layout! db/vanilla-db renamed)))

