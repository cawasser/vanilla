(ns vanilla.widgets.worldwind
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            ["worldwindjs" :as WorldWind]
            ["../js/worldwind-react-globe.js" :as Globe]
            ["worldwind-react-globe-bs4" :as bs4]
            ["reactstrap" :as rs]
            [vanilla.mapping.layer-management :as lm]
            [vanilla.data-source-subscribe :as ds]))


(def start-loc {:n-america {:latitude 47.040182 :longitude -99.403964}
                :s-america {:latitude -15.453680 :longitude -58.771031}
                :europe {:latitude 48.574790 :longitude 12.163178}
                :africa {:latitude 10.487812 :longitude 20.470202}
                :australia {:latitude -18.812718 :longitude 134.619212}
                :asia {:latitude 31.952162 :longitude 115.949663}})


(defn make-widget [name data options]

  (ds/data-source-subscribe [:x-beam-location-service :terminal-location-service :ka-beam-location-service])

  ;(prn "ww widget " data)

  ;[:div#globe {:style {:width        "100%"
  ;               :text-align   :center
  ;               :border-style (basic/debug-style options)}}
  ; [:> Globe {:layers    (lm/make-layers)
  ;            :latitude  28.538336
  ;            :longitude -81.379234}]])

  (let [this (r/current-component)
        state (merge {:globe nil} (r/state this))
        globeRef (atom nil)
        layersRef (atom nil)]

    (r/create-element
      (r/create-class
        {:display-name "Globe"

         ;:component-did-mount
         ;  (fn [comp]
         ;    (r/set-state this {:globe @globeRef}))

         :reagent-render
           (fn []
             [:div#all {:style {:width "100%" :height "100%"}}
              [:div#nav
                [:> bs4/NavBar {:logo ""
                                :title "HammerGlobe"
                                :items [(r/as-element
                                          [:> bs4/NavBarItem {:key "lyr"
                                                              :title "Layers"
                                                              :icon "/images/list-icon.png"
                                                              :collapse @layersRef}])]}]] ; #(reset! is-active false) (fn []...

              [:> rs/Container {:fluid "xl"
                                :style {:width "100%"
                                        :height "100%"}}
                [:div#globe {:style {:width        "100%"
                                     :height       "100%"
                                     :text-align   :center
                                     :border-style (basic/debug-style options)}
                             :className "globe"}

                  [:> Globe (merge {:ref  #(reset! globeRef %)
                                    :layers (lm/make-layers)
                                    :latitude  28.538336
                                    :longitude -81.379234}
                              (:n-america start-loc))]]

                [:div.overlayCards.noninteractive
                 [:> rs/CardColumns
                  [:> bs4/LayersCard {:ref #(reset! layersRef %)
                                      :categories ["overlay" "base"]
                                      :globe @globeRef}]]]]])}))))




  ;(r/create-element
  ;  (r/create-class
  ;    {:display-name "Globe"
  ;
  ;     :reagent-render
  ;       (fn []
  ;         [:div#globe {:style {:width        "100%"
  ;                              :text-align   :center
  ;                              :border-style (basic/debug-style options)}}])
  ;
  ;     :component-did-mount
  ;       (fn [this]
  ;         [:> Globe {:ref       (r/dom-node this)
  ;                    :layers    (lm/make-layers)
  ;                    :latitude  28.538336
  ;                    :longitude -81.379234}])})))