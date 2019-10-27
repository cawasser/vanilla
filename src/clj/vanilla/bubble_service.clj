(ns vanilla.bubble-service)


(defn fetch-data []
  (prn "Bubble Service")

  {:title "Bubble Data"
   :data-format :x-y-z

   ; notice how we can mix formats - some data sets can be maps,
   ; while others can be vectors with the keys in a separate key,
   ; called :keys
   ;
   :series  [{:name "Countries"
              :data [{:x 95 :y 95 :z 13.8 :name "BE" :country "Belgium"}
                     {:x 86.5 :y 102.9 :z 14.7 :name "DE" :country "Germany"}
                     {:x 80.8 :y 91.5 :z 15.8 :name "FI" :country "Finland"}
                     {:x 80.4 :y 102.5 :z 12 :name "NL" :country "Netherlands"}
                     {:x 80.3 :y 86.1 :z 11.8 :name "SE" :country "Sweden"}
                     {:x 78.4 :y 70.1 :z 16.6 :name "ES" :country "Spain"}
                     {:x 74.2 :y 68.5 :z 14.5 :name "FR" :country "France"}
                     {:x 73.5 :y 83.1 :z 10 :name "NO" :country "Norway"}
                     {:x 71 :y 93.2 :z 24.7 :name "UK" :country "United Kingdom"}
                     {:x 69.2 :y 57.6 :z 10.4 :name "IT" :country "Italy"}
                     {:x 68.6 :y 20 :z 16 :name "RU" :country "Russia"}
                     {:x 65.5 :y 126.4 :z 35.3 :name "US" :country "United States"}
                     {:x 65.4 :y 50.8 :z 28.5 :name "HU" :country "Hungary"}
                     {:x 63.4 :y 51.8 :z 15.4 :name "PT" :country "Portugal"}
                     {:x 64 :y 82.9 :z 31.3 :name "NZ" :country "New Zealand"}]}
             {:name "Fruit"
              :data [{:x 85.9 :y 64 :z 34 :name "Apples" :fruit "Apples"}
                     {:x 25.29 :y 15 :z 14 :name "Oranges" :fruit "Oranges"}
                     {:x 65.9 :y 114 :z 64.5 :name "Pears" :fruit "Pears"}
                     {:x 42.1 :y 64 :z 30.4 :name "Grapefruit" :fruit "Grapefruit"}]}
             {:name "MLB"
              :keys ["x" "y" "z" "name" "franchise"]
              :data [[17.63 18.01 6 "ARZ" "Arizona Diamondbacks"]
                     [106.97 106.59 25 "ATL" "Atlanta Braves"]
                     [87.68 97.28 14 "BAL" "Baltimore Orioles"]
                     [96.02 89.08 24 "BOS" "Boston Red Sox"]
                     [109.82 104.04 20 "CHI" "Chicago Cubs"]
                     [92.83 92.15 9 "CWS" "Chicago White Sox"]
                     [105.99 103.93 15 "CIN" "Cincinnati Reds"]
                     [94.77 90.37 14 "CLE" "Cleveland Indians"]
                     [20.33 22.8 5 "COL" "Colorado Rockies"]
                     [93.46 91.91 16 "DET" "Detroit Tigers"]
                     [46.01 46.66 13 "HOU" "Houston Astros"]
                     [39.01 42.22 9 "KC" "Kansas City Royals"]
                     [47.09 47.19 10 "LAA" "Los Angeles Angels"]
                     [109.74 98.18 33 "LAD" "Los Angeles Dodgers"]
                     [19.9 23.14 2 "MIA" "Miami Marlins"]
                     [39.13 42.17 6 "MIL" "Milwaukee Brewers"]
                     [89.03 96.03 16 "MIN" "Minnesota Twins"]
                     [44.48 48.08 9 "NYM" "New York Mets"]
                     [103.78 78.4 55 "NYY" "New York Yankees"]
                     [90.28 94.52 28 "OAK" "Oakland Athletics"]
                     [98.25 110 14 "PHI" "Philadelphia Phillies"]
                     [105.45 104.05 17 "PIT" "Pittsburgh Pirates"]
                     [37.47 43.89 5 "SD" "San Diego Padres"]
                     [111.65 96.87 26 "SF" "San Francisco Giants"]
                     [32.19 36.22 4 "SEA" "Seattle Mariners"]
                     [109.18 100.63 29 "STL" "St. Louis Cardinals"]
                     [16.86 18.76 5 "TB" "Tampa Bay Rays"]
                     [45 49.12 8 "TEX" "Texas Rangers"]
                     [33.83 34.58 7 "TOR" "Toronto Blue Jays"]
                     [39.77 41.49 6 "WSH" "Washington Nationals"]]}]})
