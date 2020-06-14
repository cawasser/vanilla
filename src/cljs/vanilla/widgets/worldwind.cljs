(ns vanilla.widgets.worldwind
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            ["worldwindjs" :as WorldWind]
            ["../js/worldwind-react-globe.js" :as Globe]
            [vanilla.mapping.layer-management :as lm]))


(def start-loc {:n-america {:latitude 47.040182 :longitude -99.403964}
                :s-america {:latitude -15.453680 :longitude -58.771031}
                :europe {:latitude 48.574790 :longitude 12.163178}
                :africa {:latitude 10.487812 :longitude 20.470202}
                :australia {:latitude -18.812718 :longitude 134.619212}
                :asia {:latitude 31.952162 :longitude 115.949663}})


(defn make-widget [name data options]

  ;(prn "ww widget " data)

  [:div {:style {:width        "100%"
                 :text-align   :center
                 :border-style (basic/debug-style options)}}
   [:> Globe (merge {:layers (lm/make-layers)}
               (:africa start-loc))]])
