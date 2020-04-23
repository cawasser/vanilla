(ns vanilla.widgets.worldwind
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            ["worldwindjs" :as WorldWind]
            ["../js/worldwind-react-globe.js" :as Globe]
            [vanilla.mapping.layer-management :as lm]))



(defn make-widget [name data options]

  (prn "ww widget " data)

  [:div {:style {:width        "100%"
                 :text-align   :center
                 :border-style (basic/debug-style options)}}
   [:> Globe {:layers    (lm/make-layers)
              :latitude  28.538336
              :longitude -81.379234}]])
