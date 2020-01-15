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
    {:id         "1000"
     :keyword    ":spectrum-traces"
     :name       "Spectrum"
     :ret_types  "data-format/x-y"
     :read_fn    ":vanilla.fetcher/spectrum-traces"
     :doc_string "returns power over frequency"})


  (create-services!
    vanilla-db
    {:services
     [["1000" "spectrum-traces" "Spectrum Traces"
       "data-format/x-y" "vanilla.fetcher/spectrum-traces"
       "returns power over frequency"]

      ["2000" "usage-data" "Usage Data"
       "data-format/x-y-n" "vanilla.fetcher/usage-data"
       "returns usage data over time"]

      ["3000" "sankey-service" "Relationship Data"
       "[data-format/x-y-n data-format/from-to]" "vanilla.sankey-service/fetch-data"
       "returns interdependencies between countries"]

      ["4000" "bubble-service" "Bubble Data"
       "data-format/x-y-n" "vanilla.bubble-service/fetch-data"
       "returns x-y-n data for Fruits, Countries and MLB teams"]

      ["5000" "network-service" "Network Data"
       "[data-format/x-y-n data-format/from-to]" "vanilla.network-service/fetch-data"
       "returns interconnectivity data"]

      ["6000" "power-data" "Power Data"
       "data-format/name-y" "vanilla.fetcher/power-data"
       "returns quantity of fruit sold"]

      ["7000" "heatmap-data" "Heatmap Data"
       "data-format/x-y-n" "vanilla.fetcher/heatmap-data"
       "returns quantity of fruit sold per country"]

      ["8000" "health-and-status-data" "Health and Status"
       "data-format/entity" "vanilla.stoplight-service/fetch-data"
       "returns green/yellow/red status for a collection of items"]

      ["9000" "usage-24-hour-service" "12-hour Usage Data"
       "data-format/x-y-n" "vanilla.usage-24-hour-service/fetch-data"
       "returns quantity of fruit sold per hour"]

      ["10000" "scatter-service-data" "Scatter Data"
       "data-format/x-y" "vanilla.scatter-service/fetch-data"
       "returns height and weight for a sample of females and males"]]})


  (create-services!
    vanilla-db
    {:services
     [["4000" "bubble-service" "Bubble Data"
       "data-format/x-y-n" "vanilla.bubble-service/fetch-data"
       "returns x-y-n data for Fruits, Countries and MLB teams"]]})

  (get-services vanilla-db)

  (delete-all-services! vanilla-db)

  (delete-service! vanilla-db {:id "1000"})
  (delete-service! vanilla-db {:id "4000"})
  (delete-service! vanilla-db {:id "7000"})





  (drop-services-table vanilla-db)

  ())


; ***Layout table rich comment***
(comment

  (create-layout-table vanilla-db)

  ;ret_types needs square brackets around it
  ;data-grid needs curly braces around it
  ;viz_tooltip(redundant) = {:followPointer true}
  ;viz_animation(redundant) = false, defaults to false though
  (create-layout!
    vanilla-db
    {:id          "123456"
     :username    "APaine"
     :key         "1"
     :name        :area-widget
     :ret_types   [:data-format/x-y-n]
     :basis       :chart
     :data_source :spectrum-traces
     :type        :area-chart
     :icon        "/images/area-widget.png"
     :label       "Area"
     :data_grid   {:x 0 :y 0 :w 4 :h 14}
     :viz_style_name        "widget"
     :viz_y_title           "power"
     :viz_x_title           "frequency"
     :viz_line_width        "NA"
     :viz_data_labels       "NA"
     :viz_label_format      "NA"
     :viz_allow_decimals    false
     :viz_banner_color      {:r 0x00 :g 0x00 :b 0xff :a 1}
     :viz_title             "Channels (area)"
     :viz_icon              "NA"
     :viz_slice_format      "NA"
     :viz_banner_text_color {:r 255, :g 255, :b 255, :a 1}})


  (get-layout vanilla-db)

  (delete-all-layouts! vanilla-db)

  (delete-layout! vanilla-db {:id "1"})

  (drop-layout-table vanilla-db)

  ())



(comment

  (def w
    [{:keywrd :line-widget, :ret_types [:data-format/x-y], :icon "/images/line-widget.png", :label "Line"}
     {:keywrd :area-widget, :ret_types [:data-format/x-y], :icon "/images/area-widget.png", :label "Area"}
     {:keywrd :bar-widget, :ret_types [:data-format/x-y], :icon "/images/bar-widget.png", :label "Bar"}
     {:keywrd :column-widget, :ret_types [:data-format/x-y], :icon "/images/column-widget.png", :label "Column"}
     {:keywrd :pie-widget, :ret_types [:data-format/x-y], :icon "/images/pie-widget.png", :label "Pie"}
     {:keywrd :bubble-widget, :ret_types [:data-format/x-y-n], :icon "/images/bubble-widget.png", :label "Bubble"}
     {:keywrd    :vari-pie-widget,
      :ret_types [:data-format/x-y-n],
      :icon      "/images/vari-pie-widget.png",
      :label     "Variable Pie"}
     {:keywrd :rose-widget, :ret_types [:data-format/x-y-n], :icon "/images/rose-widget.png", :label "Wind Rose"}
     {:keywrd    :stoplight-widget,
      :ret_types [:data-format/entity],
      :icon      "/images/stoplight-widget.png",
      :label     "Stoplight"}
     {:keywrd :map-widget, :ret_types [:data-format/lat-lon-n], :icon "/images/map-widget.png", :label "Map"}
     {:keywrd :sankey-widget, :ret_types [:data-format/x-y :data-format/from-to], :icon "/images/sankey-widget.png", :label "Sankey"}
     {:keywrd :deps-widget, :ret_types [:data-format/x-y :data-format/from-to], :icon "/images/deps-widget.png", :label "Dependencies"}
     {:keywrd :network-widget, :ret_types [:data-format/x-y :data-format/from-to], :icon "/images/network-widget.png", :label "Network"}
     {:keywrd :org-widget, :ret_types [:data-format/x-y :data-format/from-to], :icon "/images/org-widget.png", :label "Org Chart"}
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





  (def s-test {:id         "1000",
               :keyword    ":spectrum-traces",
               :name       "Spectrum",
               :ret_types  "data-format/x-y",
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


