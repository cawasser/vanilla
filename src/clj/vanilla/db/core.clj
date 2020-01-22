(ns vanilla.db.core
  (:require
    [hugsql.core :as hugsql]
    [next.jdbc :as jdbc]
    [next.jdbc.sql :as sql]
    [com.stuartsierra.component :as component]))



;
; https://www.hugsql.org
;
; https://github.com/seancorfield/usermanager-example
;


(hugsql/def-db-fns "sql/queries.sql")



(def vanilla-db
  "SQLite database connection spec."
  {:dbtype "sqlite" :dbname "vanilla_db"})



(defrecord Database [db-spec                                ; configuration
                     datasource]                            ; state

  component/Lifecycle
  (start [this]
    (if datasource
      this                                                  ; already initialized
      (assoc this :datasource (jdbc/get-datasource db-spec))))

  (stop [this]
    (assoc this :datasource nil)))



(defn setup-database [] (map->Database {:db-spec vanilla-db}))



(comment
  (do
    (hugsql/def-db-fns "sql/queries.sql")
    (hugsql/def-sqlvec-fns "sql/queries.sql"))



  (create-services-table-sqlvec vanilla-db)
  (create-services-table vanilla-db)


  (create-service!-sqlvec
    vanilla-db
    {:id         "1000"
     :keyword    "spectrum-traces"
     :name       "Spectrum"
     :ret_type  "data-format/x-y"
     :read_fn    "vanilla.spectrum-traces-service/spectrum-traces"
     :doc_string "returns power over frequency"})


  (do
    (drop-services-table vanilla-db)
    (create-services-table vanilla-db))

  (create-service!
    vanilla-db
    {:id         "1000"
     :keyword    "spectrum-traces"
     :name       "Spectrum"
     :ret_type  "data-format/x-y"
     :read_fn    "vanilla.spectrum-traces-service/spectrum-traces"
     :doc_string "returns power over frequency"})

  ;;;;;;;;;;;;;;;;;;;;;;;
  ;
  ; THIS is the function to setup the initial database!!!!!
  ;
  ;
  (create-services!
    vanilla-db
    {:services
     [["1000" "spectrum-traces" "Spectrum Traces"
       "data-format/x-y" "vanilla.spectrum-traces-service/spectrum-traces"
       "returns power over frequency"]

      ["2000" "usage-data" "Usage Data"
       "data-format/label-y" "vanilla.usage-data-service/usage-data"
       "returns usage data over time"]

      ["3000" "sankey-service" "Relationship Data"
       "data-format/from-to-n" "vanilla.sankey-service/fetch-data"
       "returns interdependencies between countries"]

      ["4000" "bubble-service" "Bubble Data"
       "data-format/x-y-n" "vanilla.bubble-service/fetch-data"
       "returns x-y-n data for Fruits, Countries and MLB teams"]

      ["5000" "network-service" "Network Data"
       "data-format/from-to" "vanilla.network-service/fetch-data"
       "returns interconnectivity data"]

      ["6000" "power-data" "Power Data"
       "data-format/x-y" "vanilla.power-data-service/power-data"
       "returns quantity of fruit sold"]

      ["7000" "heatmap-data" "Heatmap Data"
       "data-format/grid-n" "vanilla.heatmap-service/heatmap-data"
       "returns quantity of fruit sold per country"]

      ["8000" "health-and-status-data" "Health and Status"
       "data-format/entity" "vanilla.stoplight-service/fetch-data"
       "returns green/yellow/red status for a collection of items"]

      ["9000" "usage-12-hour-service" "12-hour Usage Data"
       "data-format/rose-y-n" "vanilla.usage-12-hour-service/fetch-data"
       "returns quantity of fruit sold per hour"]

      ["10000" "scatter-service-data" "Scatter Data"
       "data-format/x-y" "vanilla.scatter-service/fetch-data"
       "returns height and weight for a sample of females and males"]]})


  (create-services!
    vanilla-db
    {:services
     [["9999" "bubble-service" "Bubble Data"
       "data-format/x-y-n" "vanilla.bubble-service/fetch-data"
       "returns x-y-n data for Fruits, Countries and MLB teams"]]})
  (create-services!-sqlvec
    vanilla-db
    {:services
     [["9999" "bubble-service" "Bubble Data"
       "data-format/x-y-n" "vanilla.bubble-service/fetch-data"
       "returns x-y-n data for Fruits, Countries and MLB teams"]]})

  (delete-service! vanilla-db {:id "9999"})

  (get-services vanilla-db)







  (delete-all-services! vanilla-db)

  (delete-service! vanilla-db {:id "1000"})
  (delete-service! vanilla-db {:id "4000"})
  (delete-service! vanilla-db {:id "7000"})





  (drop-services-table vanilla-db)

  ())



(comment


  (defn y-conversion [chart-type d options]
    (let [s (get-in d [:data :series])
          ret (for [{:keys [name data]} s]
                (assoc {}
                  :name name
                  :data (into []
                              (for [x-val (range 0 (count data))]
                                [x-val (get data x-val)]))))]

      (prn "y-conversion " ret)
      (into [] ret)))


  (def data {:data {:series [{:name "one"
                              :data [5 6 7 8 9]}
                             {:name "two"
                              :data [100 50 25 12 6]}]}})

  (y-conversion :dummy data {})



  (def slice-at 45)

  (def data-2 `({:keys ["name" "y" "z"],
                 :data [["Apples" 0.7879438295646968 10]
                        ["Pears" 98.70783117307522 10]
                        ["Oranges" 74.74830656485089 10]
                        ["Plums" 7.7001283846018005 10]
                        ["Bananas" 0.8177902166278117 10]
                        ["Peaches" 19.772631719757726 10]
                        ["Prunes" 48.40954519661507 10]
                        ["Avocados" 26.941125770951402 10]]}))

  (conj (:keys (first data-2)) "selected" "sliced")
  (map #(conj % false (< (second %) slice-at)) (:data (first data-2)))

  ())


