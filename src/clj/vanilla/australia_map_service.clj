(ns vanilla.australia-map-service
  (:require [clojure.tools.logging :as log]))

(defn fetch-data []
  (log/info "Australia Map Service")

  {:title "Australia Data"
   :data-format :data-format/lat-lon-label

   :series  [{:name "Basemap",
              :borderColor "#A0A0A0",
              :nullColor "rgba(200, 200, 200, 0.3)",
              :showInLegend false
             },
             {
              :name "Separators",
              :type "mapline",
              :nullColor "#707070",
              :showInLegend false,
              :enableMouseTracking false
                  }
             {:type "mappoint",
              :name "Cities",
              :color "#000000"
              :data [{
                      :name "Canberra",
                      :lat -35.2809,
                      :lon 149.13
                      },
                     {
                      :name "Melbourne",
                      :lat -37.840935,
                      :lon 144.946457
                     },
                     {
                      :name "Adelaide",
                      :lat -34.921230,
                      :lon 138.599503
                     },
                     {
                      :name "Perth",
                      :lat -31.953512,
                      :lon 115.857048
                     }]}]})

