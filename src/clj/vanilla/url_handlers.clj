(ns vanilla.url-handlers
  (:require [vanilla.db.core :as db]))


;;;;;;;;;;;;;;;;
;
; HTML endpoint handlers


(defn get-services []
  (db/get-services db/vanilla-db))


(def from-sqlite {:id :key :data_source :data-source :data_grid :data-grid})
(def to-sqlite {:key :id :data-source :data_source :data-grid :data_grid})

(defn get-layout []
  ; fixup the key changes caused by SQLite with rename-keys
  ; :id -> :key
  ; :data_source -> :data-source
  ; :data_grid -> :data-grid
  ; apply read-string over all the values for each widget map

  (prn "GET-LAYOUT-URL : " (->> db/vanilla-db
                                db/get-layout
                                (map #(clojure.set/rename-keys % from-sqlite))
                                (mapv #(into {} (for [[k v] %] [k (clojure.edn/read-string v)])))))

  (->> db/vanilla-db
       db/get-layout
       (map #(clojure.set/rename-keys % from-sqlite))
       (mapv #(into {} (for [[k v] %] [k (clojure.edn/read-string v)])))))



(comment

  (def layout-data {:layout [{:ret_types "[:data-format/x-y]",
                               :name ":area-widget",
                               :username "\"APaine\"",
                               :basis ":chart",
                               :data_grid "{:x 0, :y 0, :w 4, :h 14}",
                               :type ":area-chart",
                               :icon "\"/images/area-widget.png\"",
                               :label "\"Area\"",
                               :id "123",
                               :data_source ":spectrum-traces",
                               :options "#:viz{:style-name \"widget\", :animation false, :x-title \"frequency\", :banner-text-color {:r 255, :g 255, :b 255, :a 1}, :title \"Channels (area)\", :allowDecimals false, :banner-color {:r 0, :g 0, :b 255, :a 1}, :y-title \"power\", :tooltip {:followPointer true}}"}
                              {:ret_types "[:data-format/x-y-n]",
                               :name ":bubble-widget",
                               :username "\"APaine\"",
                               :basis ":chart",
                               :data_grid "{:x 4, :y 0, :w 5, :h 15}",
                               :type ":bubble-chart",
                               :icon "\"/images/bubble-widget.png\"",
                               :label "\"Bubble\"",
                               :id "213",
                               :data_source ":bubble-service",
                               :options "#:viz{:animation false, :labelFormat \"{point.name}\", :banner-text-color {:r 255, :g 255, :b 255, :a 1}, :title \"Bubble\", :dataLabels true, :lineWidth 0, :data-labels true, :banner-color {:r 0, :g 0, :b 255, :a 1}, :tooltip {:followPointer true}}"}]})

  (:layout layout-data)

  (assoc one :options (clojure.edn/read-string (:options one)))


  (defn foo [m f]
    (map #(into {} (for [[k v] %] [k (f v)])) m))

  (def dat [{:a 1 :b 2}
          {:a 33 :b 23}])

  (foo dat inc)

  (into {} (for [[k v] dat] [k (inc v)]))

  (foo (:layout layout-data) clojure.edn/read-string)



  ())

