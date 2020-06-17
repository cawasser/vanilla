(ns vanilla.widgets.worldwind
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            ["worldwindjs" :as WorldWind]
            ["../js/worldwind-react-globe.js" :as Globe]
            ["worldwind-react-globe-bs4" :as bs4]
            ["reactstrap" :as rs]
            [vanilla.mapping.layer-management :as lm]))



(defn make-widget [name data options]

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
                                                              :icon "list"
                                                              :collapse @layersRef}])]}]] ; #(reset! is-active false) (fn []...

              [:> rs/Container {:fluid "lg"}
                [:div#globe {:style {:width        "100%"
                                     :text-align   :center
                                     :border-style (basic/debug-style options)}
                             :className "globe"}

                  [:> Globe {:ref  #(reset! globeRef %)
                             :layers (lm/make-layers)
                             :latitude  28.538336
                             :longitude -81.379234}]]

                [:div.overlayCards.noninteractive
                 [:> rs/CardColumns
                  [:> bs4/LayersCard {:ref #(reset! layersRef %)
                                      :categories ["overlay" "base"]
                                      :globe @globeRef}]]]]
              ])}))))



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