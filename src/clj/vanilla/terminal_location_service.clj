(ns vanilla.terminal-location-service
  (:require [clojure.tools.logging :as log]
            [datascript.core :as d]
            [vanilla.db.materialized-view :as mv]))



;   TODO: need to test with Rx or only Tx terminals to be sure we capture them
;

(defn- tx-rx-terminals-by-epoch
  "this is a helper function to combine the results of the Tx and Rx terminal
   into a single result set"

  [a b]
  (for [{:keys [name data]} a]
    (->> (get-in b [name data])
      (clojure.set/union data)
      ((fn [x] {:name name :data x})))))



(defn get-terminal-location-data
  "get all the terminals (Tx and Rx) by epoch

   returns a sequence maps:

    ({:name <epoch-id>
      :data #{{:epoch <epoch-id>
               :name <terminal-id>
               :lat <lat> :lon <lon>}}})"

  []
  (tx-rx-terminals-by-epoch
    ; this gets the Tx terminals
    (->> (d/q '[:find [(pull ?e [*]) ...]
                :where [?e :tx-term-id ?tx-term-id]]
           @mv/conn)
      (map (fn [{:keys [epoch tx-term-id tx-term-lat tx-term-lon]}]
             {:epoch epoch :name tx-term-id :lat tx-term-lat :lon tx-term-lon}))
      (group-by :epoch)
      sort
      (map mv/merge-data-sets))

    ; and the rx terminals
    (->> (d/q '[:find [(pull ?e [*]) ...]
                :where [?e :rx-term-id ?rx-term-id]]
           @mv/conn)
      (map (fn [{:keys [epoch rx-term-id rx-term-lat rx-term-lon]}]
             {:epoch epoch :name rx-term-id :lat rx-term-lat :lon rx-term-lon}))
      (group-by :epoch)
      sort
      (map mv/merge-data-sets))))



(defn fetch-data []
  (log/info "Terminal Locations")

  {:title "Terminal Locations"
   :data-format :data-format/cont-n
   :data (get-terminal-location-data)}) ;(mv/terminal-location-query)})



(comment

  (get-terminal-location-data)

  (take 5 (mv/terminal-location-query))

  (vanilla.subscription-manager/refresh-source :terminal-location-service)

  ())