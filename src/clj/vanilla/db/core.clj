(ns vanilla.db.core
  (:require
    [hugsql.core :as hugsql]
    [next.jdbc :as jdbc]
    [next.jdbc.sql :as sql]
    [com.stuartsierra.component :as component]
    [clojure.java.io :as jio]
    [clojure.tools.logging :as log]))



;
; https://www.hugsql.org
;
; https://github.com/seancorfield/usermanager-example
;


(hugsql/def-db-fns "sql/queries.sql")

(def db-type "sqlite")


;; This database is stored locally. It is considered our "working" or dev db
;; This should not be pushed to the repo or AWS, it will be added to git ignore
(def vanilla-db
  "SQLite database connection spec."
  {:dbtype db-type :dbname "vanilla_db"})






(defn populate-services
  [database]
  (create-services!
    database
    {:services
     [; 1
      ["1000" "spectrum-traces" "Spectrum Traces"
       "data-format/x-y" "vanilla.spectrum-traces-service/spectrum-traces"
       "returns power over frequency"]

      ; 2
      ["2000" "usage-data" "Usage Data"
       "data-format/label-y" "vanilla.usage-data-service/usage-data"
       "returns usage data over time"]

      ; 3
      ["3000" "sankey-service" "Relationship Data"
       "data-format/from-to-n" "vanilla.sankey-service/fetch-data"
       "returns interdependencies between countries"]

      ; 4
      ["4000" "bubble-service" "Bubble Data"
       "data-format/x-y-n" "vanilla.bubble-service/fetch-data"
       "returns x-y-n data for Fruits, Countries and MLB teams"]

      ; 5
      ["5000" "network-service" "Network Data"
       "data-format/from-to" "vanilla.network-service/fetch-data"
       "returns interconnectivity data"]

      ; 6
      ["6000" "power-data" "Power Data"
       "data-format/x-y" "vanilla.power-data-service/power-data"
       "returns quantity of fruit sold"]

      ; 7
      ["7000" "heatmap-data" "Heatmap Data"
       "data-format/grid-n" "vanilla.heatmap-service/heatmap-data"
       "returns quantity of fruit sold per country"]

      ; 8
      ["8000" "health-and-status-data" "Health and Status"
       "data-format/entity" "vanilla.stoplight-service/fetch-data"
       "returns green/yellow/red status for a collection of items"]

      ; 9
      ["9000" "usage-12-hour-service" "12-hour Usage Data"
       "data-format/rose-y-n" "vanilla.usage-12-hour-service/fetch-data"
       "returns quantity of fruit sold per hour"]

      ; 10
      ["10000" "scatter-service-data" "Scatter Data"
       "data-format/x-y" "vanilla.scatter-service/fetch-data"
       "returns height and weight for a sample of females and males"]

      ; 11
      ["11000" "current-time" "Current Time"
       "data-format/string" "vanilla.current-time-service/fetch-data"
       "returns a simple text"]

      ; 12
      ["12000" "table-service" "Data Table"
       "data-format/entities" "vanilla.table-service/fetch-data"
       "returns users' info"]

      ; 13
      ["13000" "continent-map-service" "Continent Data"
       "data-format/cont-n" "vanilla.continent-map-service/fetch-data"
       "returns tons of fruit produced per continent"]

      ; 14
      ["14000" "australia-map-service" "Australia Data"
       "data-format/lat-lon-label" "vanilla.australia-map-service/fetch-data"
       "returns location of various cities in Australia"]

      ; 15
      ["15000" "arearange-service" "AreaRange Data"
       "data-format/date-yl-yh" "vanilla.arearange-service/fetch-data"
       "returns low and high temperature of a day over time"]

      ; 16
      ["20000" "energy-use-service" "Energy Use by Sector"
       "data-format/from-to-n" "vanilla.energy-use-service/fetch-data"
       "returns energy source usage by sector"]

      ; 17
      ["21000" "task-service" "Tasks"
       "data-format/task-link" "vanilla.task-service/fetch-data"
       "returns task on a schedule"]

      ; 18
      ["22000" "signal-path-service" "Signal Paths"
       "data-format/from-to-n" "vanilla.signal-path-service/fetch-data"
       "returns signal path for missions"]

      ; 19
      ["23000" "terminal-location-service" "Terminal Locations"
       "data-format/cont-n" "vanilla.terminal-location-service/fetch-data"
       "returns terminal locations around the world"]

      ;20
      ["24000" "carousel-service" "Multi-Data Carousel"
       "data-format/carousel" "vanilla.carousel-service/fetch-data"
       "returns carousel widget that supports multiple widgets"]

      ;21
      ["25000" "beam-service" "'General' Beam Data"
       "data-format/lat-lon-e" "vanilla.beam-location-service/fetch-data"
       "returns 'general' beam location data for 2D/3D display"]

      ;22
      ["26000" "terminal-list-service" "List of Terminals"
       "data-format/entities" "vanilla.terminal-list-service/fetch-data"
       "returns a collection of Terminals and their properties"]]}))





;;;;;;;;;;;;;;;;;;;;;;;;;;
;; INITIAL DB SETUP FUNCTION
;;
;; run in the repl to create fresh db tables populated accordingly
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn initialize-database
  [database]
  (do
    ;; Remove any current tables to start fresh
    (drop-services-table database)
    (drop-layout-table database)
    (drop-users-table database)

    ;; Create new empty versions of the tables
    (create-services-table database)
    (create-layout-table database)
    (create-user-table database)

    ;; Bootstrap any data needed in the DB at start
    (populate-services database)))


(defrecord Database [db-spec                                ; configuration
                     datasource]                            ; state

  component/Lifecycle
  (start [this]
    (if datasource
      this                                                  ; already initialized
      (assoc this :datasource (jdbc/get-datasource db-spec))))

  (stop [this]
    (assoc this :datasource nil)))


(defn database-exist?
  "Runs a simple check to see if the vanilla_db file exists."
  [database]
  ;(prn "Database-exist? /////" (database :dbname))
  ;(prn (str (.exists (jio/file "vanilla_db"))))
  (.exists (jio/file (database :dbname))))



(defn setup-database
  "This is called by dashboard-clj.system to start the applications
   connection to the database. Only creates a database when in dev mode
   and when the vanilla_db has not been created."
  [dev-mode?]
  (when (and (not (database-exist? vanilla-db)) dev-mode?)
    ;; If it does not exist, create it
    (initialize-database vanilla-db)
    (log/info "creating startup database: VANILLA_DB"))

  ;; Create a Database component -- see component library
  (map->Database {:db-spec vanilla-db}))



;;; REPL ME WHEN DATABASE STRUCTURE CHANGES ;;;
(comment

  ;; The following is the deployed database that is pushed to the repo and to AWS
  ;; It is not called by any of our production code, so it is in this comment block
  (def vanilla-default
    "SQLite database connection spec."
    {:dbtype db-type :dbname "vanilla_default"})

  ;; Initialize this on database structure changes, push changes to repo
  (initialize-database vanilla-default)

  ;; Run this to create a local database for your app
  (initialize-database vanilla-db)

  ())


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; COMMENTS ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Services table rich comment
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
     :ret_type   "data-format/x-y"
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
     :ret_type   "data-format/x-y"
     :read_fn    "vanilla.spectrum-traces-service/spectrum-traces"
     :doc_string "returns power over frequency"})



  (create-services!
    vanilla-db
    {:services
     [["14000" "australia-map-service" "Australia Data"
       "data-format/lat-lon-label" "vanilla.australia-map-service/fetch-data"
       "returns location of various cities in Australia"]]})
  (create-services!-sqlvec
    vanilla-db
    {:services
     [["9999" "bubble-service" "Bubble Data"
       "data-format/x-y-n" "vanilla.bubble-service/fetch-data"
       "returns x-y-n data for Fruits, Countries and MLB teams"]]})

  (delete-service! vanilla-db {:id "13000"})

  (get-services vanilla-db)

  (delete-all-services! vanilla-db)

  (delete-service! vanilla-db {:id "1000"})
  (delete-service! vanilla-db {:id "4000"})
  (delete-service! vanilla-db {:id "7000"})





  (drop-services-table vanilla-db)

  ())

;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Layout table rich comment
(comment

  (hugsql/def-db-fns "sql/queries.sql")
  (hugsql/def-sqlvec-fns "sql/queries.sql")

  (create-layout-table-sqlvec vanilla-db)

  ;;;;;;;;;;;;;;;
  ; THIS FUNCTION MUST BE RUN TO CREATE INITIAL DB TABLE
  ;;;;;;;;;;;;;;;
  (create-layout-table vanilla-db)

  ;ret_types needs square brackets around it
  ;data-grid needs curly braces around it
  ;viz_tooltip(redundant) = {:followPointer true}
  ;viz_animation(redundant) = false, defaults to false though

  (create-layout!
    vanilla-db
    {:id          "\"123\""
     :username    "\"APaine\""
     :name        :area-widget
     :ret_types   [:data-format/x-y]
     :basis       :chart
     :data_source :spectrum-traces
     :type        :area-chart
     :icon        "\"/images/area-widget.png\""
     :label       "\"Area\""
     :data_grid   {:x 0 :y 0 :w 4 :h 14}
     :options     {:viz/style-name        "widget"
                   :viz/y-title           "power"
                   :viz/x-title           "frequency"
                   :viz/allowDecimals     false
                   :viz/banner-color      {:r 0x00 :g 0x00 :b 0xff :a 1}
                   :viz/tooltip           {:followPointer true}
                   :viz/title             "Channels (area)"
                   :viz/banner-text-color {:r 255, :g 255, :b 255, :a 1}
                   :viz/animation         false}})

  (create-layout!
    vanilla-db
    {:id          "\"213\""
     :username    "\"APaine\""
     :name        :bubble-widget
     :ret_types   [:data-format/x-y-n]
     :basis       :chart
     :data_source :bubble-service
     :type        :bubble-chart
     :icon        "\"/images/bubble-widget.png\""
     :label       "\"Bubble\""
     :data_grid   {:x 4 :y 0 :w 5 :h 15}
     :options     {:viz/banner-color      {:r 0x00 :g 0x00 :b 0xff :a 1}
                   :viz/tooltip           {:followPointer true}
                   :viz/dataLabels        true
                   :viz/data-labels       true
                   :viz/labelFormat       "{point.name}"
                   :viz/lineWidth         0
                   :viz/title             "Bubble"
                   :viz/banner-text-color {:r 255, :g 255, :b 255, :a 1}
                   :viz/animation         false}})


  (get-layout vanilla-db)

  (get-user-layout vanilla-db {:username "austin"})

  (save-layout! vanilla-db {:layout test1})

  (save-layout! vanilla-db
                {:layout
                 [["123" "APaine" ":area-widget" "[:data-format/x-y]"
                   ":chart" ":spectrum-traces" ":area-chart"
                   "\"/images/area-widget.png\"" "\"Area\"" "{:x 0, :y 0, :w 4, :h 14}"
                   "#:viz{:style-name \"widget\", :animation false, :x-title \"frequency\", :banner-text-color {:r 255, :g 255, :b 255, :a 1}, :title \"Channels (area)\", :allowDecimals false, :banner-color {:r 0, :g 0, :b 255, :a 1}, :y-title \"power\", :tooltip {:followPointer true}}"]
                  ["213" "APaine" ":bubble-widget" "[:data-format/x-y-n]"
                   ":chart" ":bubble-service" ":bubble-chart"
                   "\"/images/bubble-widget.png\"" "\"Bubble\"" "{:x 4, :y 0, :w 5, :h 15}"
                   "#:viz{:animation false, :labelFormat \"{point.name}\", :banner-text-color {:r 255, :g 255, :b 255, :a 1}, :title \"Bubble\", :dataLabels true, :lineWidth 0, :data-labels true, :banner-color {:r 0, :g 0, :b 255, :a 1}, :tooltip {:followPointer true}}"]]})

  (delete-all-layouts! vanilla-db)

  (delete-layout! vanilla-db {:id "7ff54050-42bd-4837-b056-8916899cc0d0"})
  (delete-layout! vanilla-db {:id "213"})

  (drop-layout-table vanilla-db)

  ())

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Users db table rich comment
(comment


  (hugsql/def-db-fns "sql/queries.sql")
  (hugsql/def-sqlvec-fns "sql/queries.sql")

  (create-user-table vanilla-db)


  (create-new-user!
    vanilla-db "Jeff" "321")                                ;; This does not work, don't try to do this

  (create-new-user!
    vanilla-db
    {:username "chad"
     :pass     "123"})

  (get-user vanilla-db {:username "chad"})
  (get-users vanilla-db)

  (verify-credentials vanilla-db
    {:username "chad" :pass "123"})

  (verify-credentials vanilla-db
    {:username "chad" :pass "321"})


  (drop-users-table vanilla-db)

  ())

;;;;;;;;;;;;;;;;;;;;;;;;;
;; Data setup playground
(comment


  (defn y-conversion [chart-type d options]
    (let [s   (get-in d [:data :series])
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