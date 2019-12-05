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

  (hugsql/def-sqlvec-fns "sql/queries.sql")

  (create-services-table-sqlvec vanilla-db)
  (create-services-table vanilla-db)

  (create-service!
    vanilla-db
    {:id         "1000" :keyword ":spectrum-traces"
     :name       "Spectrum"
     :ret_types  "data-format/x-y"
     :doc_string "returns power over frequency"})


  (create-services!
    vanilla-db
    {:services
     [["2000" ":usage-data" "Service Usage"
       "data-format/x-y-n" "returns usage data over time"]
      ["3000" ":sankey-service" "Interdependency"
       "data-format/x-y-n"
       "returns interdependencies between countries"]]})


  (get-services vanilla-db)

  (delete-service! vanilla-db {:id "1000"})
  (delete-service! vanilla-db {:id "2000"})
  (delete-service! vanilla-db {:id "3000"})

  (drop-services-table vanilla-db)

  ())



(comment

  (def w
    [{:keywrd :line-widget, :ret_types [:data-format/x-y], :icon "/images/line-widget.png", :label "Line"}
     {:keywrd :area-widget, :ret_types [:data-format/x-y], :icon "/images/area-widget.png", :label "Area"}
     {:keywrd :bar-widget, :ret_types [:data-format/x-y], :icon "/images/bar-widget.png", :label "Bar"}
     {:keywrd :column-widget, :ret_types [:data-format/x-y], :icon "/images/column-widget.png", :label "Column"}
     {:keywrd :pie-widget, :ret_types [:data-format/x-y], :icon "/images/pie-widget.png", :label "Pie"}
     {:keywrd :vari-pie-widget,
      :ret_types [:data-format/x-y-n],
      :icon "/images/vari-pie-widget.png",
      :label "Variable Pie"}
     {:keywrd :rose-widget, :ret_types [:data-format/x-y-n], :icon "/images/rose-widget.png", :label "Wind Rose"}
     {:keywrd :stoplight-widget,
      :ret_types [:data-format/entity],
      :icon "/images/stoplight-widget.png",
      :label "Stoplight"}
     {:keywrd :map-widget, :ret_types [:data-format/lat-lon-n], :icon "/images/map-widget.png", :label "Map"}
     {:keywrd :sankey-widget, :ret_types [:data-format/x-y], :icon "/images/sankey-widget.png", :label "Sankey"}
     {:keywrd :deps-widget, :ret_types [:data-format/x-y], :icon "/images/deps-widget.png", :label "Dependencies"}
     {:keywrd :network-widget, :ret_types [:data-format/x-y], :icon "/images/network-widget.png", :label "Network"}
     {:keywrd :org-widget, :ret_types [:data-format/x-y], :icon "/images/org-widget.png", :label "Org Chart"}
     {:keywrd :heatmap-widget, :ret_types [:data-format/x-y-n], :icon "/images/heatmap-widget.png", :label "Heatmap"}])

  (def s (get-services vanilla-db))
  (def s-1 (first s))
  s

  (if (some #{:data-format/x-y-n}
        [:data-format/x-y-n]) true false)

  (if (some #{:data-format/x-y-n}
        [:data-format/x-y]) true false)

  (if (some #{:data-format/x-y-n}
        [:data-format/x-y-n :data-format/lat-lon-n]) true false)

  (filter #(if (some #{(keyword ":data-format/x-y-n")}
                 (:ret_types %)) true false) w)

  (def ret (filter #(if (some #{(keyword (:ret_types ":data-format/x-y-n"))}
                          (:ret_types %)) true false) w))
  ret


  (defn- filter-widgets [widgets selected]
    (let [ret-val (filter #(if (some #{(keyword (:ret_types selected))}
                                 (:ret_types %))
                             true
                             false) widgets)]
      ret-val))





  (def s-test {:id "1000",
               :keyword ":spectrum-traces",
               :name "Spectrum",
               :ret_types "data-format/x-y",
               :doc_string "returns power over frequency"})


  (if (some #{(keyword (:ret_types s-test))}
        (:ret_types (first w))) true false)


  (filter #(if (some #{(keyword (:ret_types s-test))}
                 (:ret_types %)) true false) w)







  (defn- selected-service [services selected]
    (let [ret-val (first (filter #(= selected (:name %)) services))]
      ret-val))


  (filter #(= "Spectrum" (:name %)) s)


  (selected-service s "Spectrum")

  (filter-widgets w s-1)


  ())


