(ns vanilla.widgets.worldwind
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]
            ["worldwindjs" :as WorldWind]
            ["../js/worldwind-react-globe.js" :as Globe]
            [vanilla.mapping.layer-management :as lm]))



(def cities [{:name "Canberra" :lat -35.2809 :lon 149.13 :alt 100}
             {:name "Brisbane" :lat -27.47012 :lon 153.021072 :alt 100}
             {:name "Geraldton" :lat -28.782387 :lon 114.607513 :alt 100}
             {:name "Wagga Wagga" :lat -35.117275 :lon 147.356522 :alt 100}
             {:name "Orlando" :lat 28.538336 :lon -81.379234 :alt 100}
             {:name "San Diego" :lat 32.715736 :lon -117.161087 :alt 100}
             {:name "Dulles" :lat 38.951666 :lon -77.448055 :alt 100}])


(def terminals [{:name "At-SEA" :lat 42.0989 :lon -25.09877 :alt 0}
                {:name "Boat Location 20", :lat 41.0, :lon 126.0, :atl 100}
                {:name "Dominican Republic", :lat 20.01, :lon -70.01, :atl 100}
                {:name "RX3", :lat 50.0, :lon -2.5, :atl 100}
                {:name "WP01", :lat -5.04, :lon -5.04, :atl 100}
                {:name "Boat Location 42", :lat -0.05, :lon 116.0, :atl 100}
                {:name "Guam", :lat 35.49, :lon 123.23, :atl 100}
                {:name "START", :lat -16.0, :lon 40.0, :atl 100}
                {:name "Boat Location 39", :lat 32.0, :lon 123.0, :atl 100}
                {:name "Fixed", :lat 0.0, :lon -118.0, :atl 100}
                {:name "Boat Location 44", :lat 30.0, :lon 130.0, :atl 100}
                {:name "Fixed", :lat 20.01, :lon -70.01, :atl 100}
                {:name "FIXED", :lat 25.0, :lon -10.0, :atl 100}
                {:name "Fixed", :lat 25.03, :lon -60.03, :atl 100}
                {:name "Guam", :lat 37.46, :lon 132.06, :atl 100}
                {:name "FIXED (NCA2/China)", :lat 31.0, :lon 113.0, :atl 100}
                {:name "Fixed", :lat 54.0, :lon 25.0, :atl 100}
                {:name "Guam", :lat 27.71, :lon 123.75, :atl 100}
                {:name "Boat Location 31", :lat 10.0, :lon 107.0, :atl 100}
                {:name "Boat Location 9", :lat 37.0, :lon 119.0, :atl 100}
                {:name "Boat Location 3", :lat 28.0, :lon 123.0, :atl 100}])

(def beam-coverage [{:name "BEAM-1" :lat 28.538336 :lon -81.379234 :diameter 1000000 :color (WorldWind/Color. 0 1 0 0.5)}
                    {:name "BEAM-2" :lat 42.0989 :lon -25.09877 :diameter 500000 :color (WorldWind/Color. 1 0 1 0.5)}
                    {:name "BEAM-4" :lat -35.117275 :lon 147.356522 :diameter 750000 :color (WorldWind/Color. 0 0.3 1 0.5)}
                    {:name "BEAM-5" :lat -5.04 :lon -5.04 :diameter 250000 :color (WorldWind/Color. 0.2 0.2 0.6 0.5)}
                    {:name "BEAM-6" :lat 27.71 :lon 123.75 :diameter 750000 :color (WorldWind/Color. 1 1 1 0.5)}])



(defn location-layer [title data color]
  (let [layer          (WorldWind/RenderableLayer. title)
        textAttributes (WorldWind/TextAttributes.)]

    (set! (.-color textAttributes) color)

    (doall (map (fn [d]
                  (let [point (WorldWind/Position. (:lat d) (:lon d) (:alt d))
                        loc   (WorldWind/Location.)
                        name  (get d :name "Missing")
                        text  (WorldWind/GeographicText. point name)]

                    (set! (.-attributes text) textAttributes)
                    (.addRenderable layer text)))
             data))
    layer))


(defn beam-layer [title data]
  (let [layer (WorldWind/RenderableLayer. title)]
    (doall (map (fn [d]
                  (let [attributes (WorldWind/ShapeAttributes.)
                        point      (WorldWind/Location. (:lat d) (:lon d))
                        circle     (WorldWind/SurfaceCircle. point (:diameter d) attributes)]

                    (set! (.-interiorColor attributes) (get d :color (WorldWind/Color. 1 1 1 0.5)))
                    (set! (.-outlineColor attributes) (get d :color (WorldWind/Color. 1 1 1 0.5)))

                    (.addRenderable layer circle)))
             data))
    layer))



(defn make-widget [name data options]

  (prn "ww widget " data)

  (let [cities    (location-layer "Cities" cities (.-YELLOW WorldWind/Color))
        terminals (location-layer "Terminals" (get-in data [:data :data]) (.-WHITE WorldWind/Color))
        beams     (beam-layer "GDAs" beam-coverage)]

  ;(.log js/console ":simple-text" (str data) (str options))

    [:div {:style {:width        "100%"
                   :text-align   :center
                   :border-style (basic/debug-style options)}}
     [:> Globe {:layers    (lm/make-layers)
                           ;["blue-marble"
                           ; cities
                           ; terminals
                           ; beams]
                :latitude  28.538336
                :longitude -81.379234}]]))
