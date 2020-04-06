(ns vanilla.continent-map-service
  (:require [clojure.tools.logging :as log]))

(defn fetch-data []
  (log/info "Continent Map Service")

  {:title "Continent Data"
   :data-format :data-format/cont-n

   :series  [{:name "Basemap"
              :borderColor "#A0A0A0"
              :nullColor "rgba(200 200 200 0.3)"
              :showInLegend false}

             {:name "Separators"
              :type "mapline"
              :nullColor "#707070"
              :showInLegend false
              :enableMouseTracking false}

             {:type "mappoint"
              :name "Cities"
              :color "#eeeeee"
              :data [{:name "Canberra"
                      :lat -35.2809
                      :lon 149.13}

                     {:name "Brisbane"
                      :lat -27.47012
                      :lon 153.021072}
                     
                     {:name "Geraldton"
                      :lat -28.782387
                      :lon 114.607513}
                     
                     {:name "Wagga Wagga"
                      :lat -35.117275
                      :lon 147.356522}
                     {:name "Orlando"
                      :lat 28.538336
                      :lon -81.379234}
                     
                     {:name "San Diego"
                      :lat 32.715736
                      :lon -117.161087}
                     
                     {:name "Dulles"
                      :lat 38.951666
                      :lon -77.448055}]}]})

