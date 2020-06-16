(ns vanilla.mapping.layer-management
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            ["worldwindjs" :as WorldWind]
            [vanilla.data-source-subscribe :as ds]))





(rf/reg-event-db
  :add-layer
  (fn-traced
    [db [_ name layer]]
    (assoc-in db [:layers name] (conj (get-in db [:layer name]) layer))))


(rf/reg-sub
  :layers
  (fn [db [_ name]]
    (get-in db [:layers name])))



(def cities [{:name "Canberra" :lat -35.2809 :lon 149.13}
             {:name "Brisbane" :lat -27.47012 :lon 153.021072}
             {:name "Perth" :lat -31.924193 :lon 115.822557}
             {:name "Sydney" :lat -33.853310 :lon 151.214847}
             {:name "Orlando" :lat 28.538336 :lon -81.379234}
             {:name "San Diego" :lat 32.715736 :lon -117.161087}
             {:name "Dulles" :lat 38.951666 :lon -77.448055}
             {:name "Montreal" :lat 45.50884 :lon -73.58781}
             {:name "London" :lat 51.50853 :lon -0.12574}
             {:name "Mumbai" :lat 19.07283 :lon 72.88261}
             {:name "Tokyo" :lat 35.6895 :lon 139.69171}
             {:name "Sau Paulo" :lat -23.5475 :lon -46.63611}
             {:name "Cartagena" :lat 10.39972 :lon -75.51444}
             {:name "Lima" :lat -12.125264 :lon -77.054384}
             {:name "Sanitago" :lat -33.477272 :lon -70.668214}
             {:name "Buenos Aires" :lat -34.597042 :lon -58.460822}
             {:name "Manaus" :lat -3.096636 :lon -59.969071}
             {:name "Monrovia" :lat 6.304379 :lon -10.790660}
             {:name "Cairo" :lat 30.061963 :lon 31.247398}
             {:name "Cape Town" :lat -34.089061 :lon 18.357140}
             {:name "Paris" :lat 48.868328 :lon 2.343648}
             {:name "Berlin" :lat 52.509535 :lon 13.409717}
             {:name "Rome" :lat 41.910453 :lon 12.486864}
             {:name "Moscow" :lat 55.742574 :lon 37.632079}
             {:name "Beijing" :lat 39.876019 :lon 116.394211}
             {:name "Shanghai" :lat 31.240985 :lon 121.387871}
             {:name "Jakarta" :lat -6.227934 :lon 106.778561}])


(def beam-colors {"Y" [1 1 0 0.25]
                  "B" [0 0 1 0.25]
                  "R" [1 0 0 0.25]
                  "G" [0 1 0 0.25]
                  "O" [1 0.27 0 0.25]})



(defn- location-layer [title data color]
  (let [layer          (WorldWind/RenderableLayer. title)
        textAttributes (WorldWind/TextAttributes.)]

    (set! (.-color textAttributes) color)

    (doall (map (fn [d]
                  ;(prn "location-layer d" d)
                  (let [point (WorldWind/Position. (:lat d) (:lon d) (get d :alt 200))
                        name  (get d :name "Missing")
                        text  (WorldWind/GeographicText. point name)]

                    (set! (.-attributes text) textAttributes)
                    (.addRenderable layer text)))
             data))
    layer))


(defn- beam-properties [attributes beam hollow]
  (let [color (get beam-colors beam [1 1 1 0.25])
        [r g b a] color]
    ;(prn "beam color" beam "," color)

    (if (not hollow)
      (set! (.-interiorColor attributes) (WorldWind/Color. r g b a))
      (do
        (set! (.-interiorColor attributes) (WorldWind/Color. r g b 0.0))
        (set! (.-outlineWidth attributes) 2)))
    (set! (.-outlineColor attributes) (WorldWind/Color. r g b 1.0))))


(defn- beam-layer [title data hollow]
  (let [layer (WorldWind/RenderableLayer. title)]
    (doall
      (map (fn [d]
             ;(prn "beam-layer d" d)
             (let [attributes     (WorldWind/ShapeAttributes.)
                   point          (WorldWind/Location. (:lat d) (:lon d))
                   label-pt       (WorldWind/Position. (:lat d) (:lon d) (get d :alt 100))
                   circle         (WorldWind/SurfaceCircle. point (* 1.6 (get-in d [:e :diam])) attributes)
                   textAttributes (WorldWind/TextAttributes.)
                   text           (WorldWind/GeographicText. label-pt (get d :name "Missing"))]

               (beam-properties attributes (get-in d [:e :purpose]) (= "1" (last (:satellite-id d))))

               (set! (.-color textAttributes) (.-WHITE WorldWind/Color))
               (set! (.-attributes text) textAttributes)
               ;(.addRenderable layer text)

               (.addRenderable layer circle)))

        data))
    layer))



(defn- find-epoch [epoch events]
  (filter #(= epoch (:name %)) events))

(defn make-layers []
  ; TODO: this is a hack for the following hack (does NOT unsubscribe to sources when widget closes)
  (ds/data-source-subscribe [:terminal-location-service :ka-beam-location-service])

  (let [x-beams   (get-in @(rf/subscribe [:app-db :x-beam-location-service]) [:data :data])
        ka-beams  (get-in @(rf/subscribe [:app-db :ka-beam-location-service]) [:data :data])
        terminals (get-in @(rf/subscribe [:app-db :terminal-location-service]) [:data :data])
        epoch     "170400Z JUL 2020"
        t-id      "MOB1"]

    ;(prn "ka-beams" ka-beams)
    ;(prn "ka-beams[2]" (get-in ka-beams [2 :data]))
    ;(prn "terminals" @(rf/subscribe [:app-db :terminal-location-service]) terminals)

    ["blue-marble"
     ;(location-layer "Cities" cities (.-YELLOW WorldWind/Color))
     (beam-layer "Ka Beams" (->> (find-epoch epoch ka-beams) first :data) false)
     (location-layer "Terminals"
       (->> (find-epoch epoch terminals) first :data)
       (.-WHITE WorldWind/Color))]))





(defn add-layer [layers new-layer]
  (swap! layers conj new-layer))



(comment
  (def beam-2 [{:name "BEAM-7" :lat -28.538336 :lon 81.379234 :diameter 1000000 :color (WorldWind/Color. 1 1 0 0.5)}])

  (def b (beam-layer "GDAs" beam-2))


  ; working out how to get just one epoch from the epochal-data
  (def ka-beams (get-in @(rf/subscribe [:app-db :ka-beam-location-service]) [:data :data]))
  (get-in ka-beams [2 :data])
  (map :name ka-beams)

  (def terminals (get-in @(rf/subscribe [:app-db :terminal-location-service]) [:data :data]))
  (map :name terminals)
  (clojure.set/intersection
    (into #{} (map :name ka-beams))
    (into #{} (map :name terminals)))

  #{"161200Z JUL 2020"
    "170400Z JUL 2020"
    "172000Z JUL 2020"
    "190000Z JUL 2020"
    "211600Z JUL 2020"
    "230000Z JUL 2020"}

  (def epoch "170400Z JUL 2020")
  (def epoch "161607Z JUL 2020")
  (def epoch "190000Z JUL 2020")
  (->> (find-epoch epoch ka-beams) first :data)
  (->> (find-epoch epoch terminals) first :data)



  ; since we don't yet have a means of scanning through the epochs,
  ; for data development, collapse all the terminal epochs into 1,
  ; suffixing the terminal-id with the epoch so we get a sense of terminals
  ; "moving" across the globe

  (map (fn [e]
         (fn [t]
           (:data t))
         (:data e))
    terminals)

  (def t-id "MOB2")
  (into #{}
    (filter #(= t-id (subs (:name %) 0
                       (clojure.string/index-of (:name %) "-")))
      (apply concat
        (map (fn [e]
               (map (fn [t]
                      (assoc t :name (str (:name t) "-" (subs (:name e) 0 4))))
                 (:data e)))
          terminals))))

  (subs "222000Z JUL 2020" 0 4)
  (clojure.string/index-of "DUL>1904" ">")

  (subs "MOB1-1808" 0 (clojure.string/index-of "MOB1-1808" "-"))
  (= t-id (subs "MOB1-1808" 0
            (clojure.string/index-of "MOB1-1808" "-")))

  (last "SAT3")
  ())