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
    (->> (d/q '[:find ?terminal-id ?lat ?lon
                ?satellite-id ?tx-beam ?tx-channel ?rx-beam ?rx-channel
                :where [?e :terminal-id ?terminal-id]
                [?e :lat ?lat]
                [?e :lon ?lon]
                [?e :satellite-id ?satellite-id]
                [?e :tx-beam ?tx-beam]
                [?e :tx-channel ?tx-channel]
                [?e :rx-beam ?rx-beam]
                [?e :rx-channel ?rx-channel]]
           @excel/conn)
      (sort-by first)
      (map (fn [[t lat lon s txb txc rxb rxc]]
             {:terminal-id  t
              :lat          lat
              :lon          lon
              :satellite-id s
              :tx-beam      txb
              :tx-channel   txc
              :rx-beam      rxb
              :rx-channel   rxc})))))


(defn fetch-data []
  (log/info "Terminal List")

  {:title       "Terminal List"
   :data-format :data-format/entities
   :meta-data   header
   :series      (get-data)})


(comment

  (vanilla.subscription-manager/refresh-source :terminal-list-service)

  ())
