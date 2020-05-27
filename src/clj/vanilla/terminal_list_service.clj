(ns vanilla.terminal-list-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))

(def header [{:key "terminal-id" :name "Terminal ID" :editable true}
             {:key "lat" :name "Lat" :editable true}
             {:key "lon" :name "Lon" :editable true}
             {:key "satellite-id" :name "Satellite ID" :editable true}
             {:key "tx-beam" :name "Tx Beam" :editable true}
             {:key "tx-channel" :name "Tx Channel" :editable true}
             {:key "rx-beam" :name "Rx Beam" :editable true}
             {:key "rx-channel" :name "Rx Channel" :editable true}])



(defn- get-data []
  (into []
    (->> (d/q '[:find [(pull ?e [*]) ...]
                :where [?e :terminal-id]]
           @excel/conn)
      (sort-by first))))


(defn fetch-data []
  (log/info "Terminal List")

  {:title       "Terminal List"
   :data-format :data-format/entities
   :meta-data   header
   :series      (get-data)})


(comment
  (d/q '[:find [(pull ?e [*]) ...]
         :where [?e :terminal-id]]
    @excel/conn)


  (->> (d/q '[:find [(pull ?e [*]) ...]
              :where [?e :terminal-id]]
        @excel/conn)
    (sort-by first))

  ())
