(ns vanilla.mapping.layer-management
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            ["worldwindjs" :as WorldWind]))





(rf/reg-event-db
  :add-layer
  (fn-traced
    [db [_ name layer]]
    (assoc-in db [:layers name] (conj (get-in db [:layer name]) layer))))


(rf/reg-sub
  :layers
  (fn [db [_ name]]
    (get-in db [:layers name])))



(def cities [{:name "Canberra" :lat -35.2809 :lon 149.13 :alt 100}
             {:name "Brisbane" :lat -27.47012 :lon 153.021072 :alt 100}
             {:name "Geraldton" :lat -28.782387 :lon 114.607513 :alt 100}
             {:name "Wagga Wagga" :lat -35.117275 :lon 147.356522 :alt 100}
             {:name "Orlando" :lat 28.538336 :lon -81.379234 :alt 100}
             {:name "San Diego" :lat 32.715736 :lon -117.161087 :alt 100}
             {:name "Dulles" :lat 38.951666 :lon -77.448055 :alt 100}
             {:name "Montreal" :lat 45.50884 :lon -73.58781 :alt 100}
             {:name "London" :lat 51.50853 :lon -0.12574 :alt 100}
             {:name "Mumbai" :lat 19.07283 :lon 72.88261 :alt 100}
             {:name "Tokyo" :lat 35.6895 :lon 139.69171 :alt 100}
             {:name "Sau Paulo" :lat -23.5475 :lon -46.63611 :alt 100}
             {:name "Cartagena" :lat 10.39972 :lon -75.51444 :alt 100}])


(def beam-colors {"Broadcast" [0 1 0 0.15]
                  "Spot"      [1 0 1 0.5]
                  "Adaptive"  [0 0.3 1 0.5]
                  "Protected" [0.2 0.2 0.6 0.5]
                  "UHF"       [1 0 1 0.25]
                  "Y"         [1 1 0 0.25]
                  "B"         [0 0 1 0.25]
                  "R"         [1 0 0 0.25]
                  "G"         [0 1 0 0.25]})



(defn- location-layer [title data color]
  (let [layer          (WorldWind/RenderableLayer. title)
        textAttributes (WorldWind/TextAttributes.)]

    (set! (.-color textAttributes) color)

    (doall (map (fn [d]
                  (let [point (WorldWind/Position. (:lat d) (:lon d) (get d :alt 100))
                        loc   (WorldWind/Location.)
                        name  (get d :name "Missing")
                        text  (WorldWind/GeographicText. point name)]

                    (set! (.-attributes text) textAttributes)
                    (.addRenderable layer text)))
             data))
    layer))


(defn- beam-color [attributes beam]
  (let [color (get beam-colors beam [1 1 1 0.25])
        [r g b a] color]
    ;(prn "beam color" beam "," color)

    (set! (.-interiorColor attributes) (WorldWind/Color. r g b a))
    (set! (.-outlineColor attributes) (WorldWind/Color. r g b 1.0))))


(defn- beam-layer [title data]
  (let [layer (WorldWind/RenderableLayer. title)]
    (doall (map (fn [d]
                  (let [attributes     (WorldWind/ShapeAttributes.)
                        point          (WorldWind/Location. (:lat d) (:lon d))
                        label-pt       (WorldWind/Position. (:lat d) (:lon d) (get d :alt 100))
                        circle         (WorldWind/SurfaceCircle. point (* 1.6 (get-in d [:e :diam])) attributes)
                        textAttributes (WorldWind/TextAttributes.)
                        text           (WorldWind/GeographicText. label-pt (get d :name "Missing"))]

                    (beam-color attributes (get-in d [:e :purpose]))

                    (set! (.-color textAttributes) (.-WHITE WorldWind/Color))
                    (set! (.-attributes text) textAttributes)
                    (.addRenderable layer text)

                    (.addRenderable layer circle)))

             data))
    layer))




(defn make-layers []
  (let [x-beams   (get-in @(rf/subscribe [:app-db :beam-location-service]) [:data :data])
        terminals (get-in @(rf/subscribe [:app-db :terminal-location-service]) [:data :data])]

    ;(prn "x-beams" @(rf/subscribe [:app-db :beam-location-service]) x-beams)
    ;(prn "terminals" @(rf/subscribe [:app-db :terminal-location-service]) terminals)

    ["blue-marble"
     (location-layer "Cities" cities (.-YELLOW WorldWind/Color))
     (location-layer "Terminals" terminals (.-WHITE WorldWind/Color))
     (beam-layer "X Beams" x-beams)]))




(defn add-layer [layers new-layer]
  (swap! layers conj new-layer))



(comment
  (def beam-2 [{:name "BEAM-7" :lat -28.538336 :lon 81.379234 :diameter 1000000 :color (WorldWind/Color. 1 1 0 0.5)}])

  (def b (beam-layer "GDAs" beam-2))

  ())