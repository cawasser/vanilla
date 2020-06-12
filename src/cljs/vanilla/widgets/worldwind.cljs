(ns vanilla.widgets.worldwind
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            ["worldwindjs" :as WorldWind]
            ["../js/worldwind-react-globe.js" :as Globe]
            ["worldwind-react-globe-bs4" :as bs4]
            [vanilla.mapping.layer-management :as lm]))



(defn make-widget [name data options]

  ;(prn "ww widget " data)

  ;[:div#globe {:style {:width        "100%"
  ;               :text-align   :center
  ;               :border-style (basic/debug-style options)}}
  ; [:> Globe {:layers    (lm/make-layers)
  ;            :latitude  28.538336
  ;            :longitude -81.379234}]])

  (let [is-active (r/atom false)]

    (r/create-element
      (r/create-class
        {:display-name "Globe"

         :reagent-render
           (fn []
             [:div#nav
              [:> bs4/NavBar {:logo ""
                              :title "HammerGlobe"
                              :items [(r/as-element
                                        [:> bs4/NavBarItem {:key "lyr"
                                                            :title "Layers"
                                                            :icon "list"
                                                            :collapse is-active}])]}] ; #(reset! is-active false)

              [:div#globe {:style {:width        "100%"
                                   :text-align   :center
                                   :border-style (basic/debug-style options)}}

              [:> Globe {:layers    [{:layer "blue-marble"
                                      :options {:category "base" :enabled true}}]      ;(lm/make-layers)
                         :latitude  28.538336
                         :longitude -81.379234}]]])}))))



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