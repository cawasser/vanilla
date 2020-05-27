(ns vanilla.beam-location-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))





(defn- get-data [band]
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :where [?e :band ?band]
              [?e :beam-id ?name]
              :in $ ?band]
         @excel/conn band)
    (map (fn [{:keys [band beam-id lat lon radius type]}]
           {:name (str band beam-id) :lat lat :lon lon
            :e {:diam (* radius 2) :purpose type}}))))


(defn fetch-data [band]
  (log/info "Beam Locations" band "band")

  {:title "Beam Locations"
   :data-format :data-format/lat-lon-e
   :data (get-data band)})



(comment
  (sort-by :name (get-data "X"))

  (sort-by :name (get-data "Ka"))

  (d/q '[:find ?name
         :where [?e :beam-id ?name]]
    @excel/conn)


  (def db @excel/conn)
  (def band "X")

  (d/q '[:find [(d/pull ?e ?name) ...]
         :where [?e :beam-id ?name]]
    db)

  (d/q '[:find ?e
         :where [?e :beam-id ?name]]
        db)

  (def beams (d/q '[:find ?e
                    :where [?e :beam-id ?name]]
               db))

  (d/pull db '[*] (ffirst beams))

  (d/q '[:find [(pull ?e [*]) ...]
         :where [?e :beam-id _]]
    db)




  ())


