(ns vanilla.continent-map-service)

(defn fetch-data []
  (prn "Continent Map Service")

  {:title "Continent Data"
   :data-format :data-format/x-y

   :series  [{:data [["eu" 0]

                     ["oc" 10]

                     ["af" 200]

                     ["as" 33]

                     ["na" 44]

                     ["sa" 55]]

              :name "Tons produced"

              :states {:hover {:color "#BADA55"}}

              :dataLabels {:enabled true

                           :format "{point.name}"}}]})
