(ns vanilla.widgets.gif-widget
  (:require [reagent.core :as r]
            [vanilla.widgets.basic-widget :as basic]
            ["react-gif-player" :as GifPlayer]))


(defn make-widget [name data options]

  ;(prn ":gif-widget" (str data) (str options))

  [:div {:style {:width "100%"
                 :text-align :center
                 :border-style  (basic/debug-style options)
                 :background-color "#404040"}}

   [:> GifPlayer {:gif "/images/flag.gif"
                  :still "/images/gif-still.png"
                  :autoplay true}]])