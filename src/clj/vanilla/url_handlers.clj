(ns vanilla.url-handlers
  (:require [vanilla.db.core :as db]))


;;;;;;;;;;;;;;;;
;
; HTML endpoint handlers


(defn get-services []
  (db/get-services db/vanilla-db))


(def from-sqlite {:id :key :data_source :data-source :data_grid :data-grid})
;dont need this call because we insert as a keyless tuple but may be useful in the future
;(def to-sqlite {:key :id :data-source :data_source :data-grid :data_grid})

; fixup the key changes caused by SQLite with rename-keys
; :id -> :key
; :data_source -> :data-source
; :data_grid -> :data-grid
(defn get-layout []
  (map #(clojure.set/rename-keys % from-sqlite) (db/get-layout db/vanilla-db)))

(defn save-layout [new-layout]
  ;(prn "SAVE-LAYOUT HANDLER incoming: " new-layout)
  ; TODO replace with actual user, probably sooner than here, on the cljs side
  ; first add the current user to all the widget maps after reading the string
  ; then order the maps by keys, then remove all keys
  (let [usernamed (map #(assoc % :username "testHuman") (clojure.core/read-string new-layout))
        ordered (map #(vals %) (map #(into (sorted-map) %) usernamed))]
    ;(prn "SAVE-LAYOUT Handler: " ordered)
    (db/save-layout! db/vanilla-db {:layout ordered})))

