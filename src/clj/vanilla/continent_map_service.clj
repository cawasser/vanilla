(ns vanilla.continent-map-service)

(defn fetch-data []
  (prn "Continent Map Service")

  {:title "Continent Data"
   :data-format :data-format/cont-n

   :series  [{:data [["eu" (rand-int 101)]

                     ["oc" (rand-int 101)]

                     ["af" (rand-int 101)]

                     ["as" (rand-int 101)]

                     ["na" (rand-int 101)]

                     ["sa" (rand-int 101)]]

              :name "Tons produced"

              :states {:hover {:color "#BADA55"}}

              :dataLabels {:enabled true

                           :format "{point.name}"}}]})
