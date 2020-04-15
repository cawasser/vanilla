(ns vanilla.widgets.carousel
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]
            ["pure-react-carousel" :refer [CarouselProvider Slider Slide
                                           Dot DotGroup
                                           ButtonFirst ButtonBack ButtonNext ButtonLast]]))

; Carousel
;  https://github.com/express-labs/pure-react-carousel
;

(def contents
  [[:> ReactHighcharts {:config data/heatmap-data}]
   [:> ReactHighmaps {:config mapping/world-map-data}]
   [:> ReactHighmaps {:config mapping/aus-map-data}]])


(defn make-widget [name contents options]

  [:div {:style {:width "650px" :height "450px"}}
   [:> CarouselProvider {:naturalSlideWidth 650
                         :naturalSlideHeight 400
                         :totalSlides (count contents)
                         :dragEnabled false}
    [:> Slider {:class "slider"
                :style {:width "650px" :height "380px"}}
     (for [[idx c] (map-indexed vector contents)]
       [:> Slide {:key idx :index idx} c])]

    [:> ButtonFirst {:class "button is-small"} "First"]
    [:> ButtonBack {:class "button is-small"} "Back"]
    [:> ButtonNext {:class "button is-small"} "Next"]
    [:> ButtonLast {:class "button is-small"} "Last"]

    [:div
     (for [[idx c] (map-indexed vector contents)]
       [:> Dot {:class "button is-small" :slide idx}])]]])



;[:div {:key "9" :data-grid {:x 8 :y 6 :w 4 :h 3}}
; [basic-widget
;  "carousel"
;  [carousel [[:> ReactHighcharts {:config data/heatmap-data}]
;             [:> ReactHighmaps {:config mapping/world-map-data}]
;             [:> ReactHighmaps {:config mapping/aus-map-data}]]]
;  {:viz/title             "Carousel"
;   :viz/banner-color      {:r 255 :g 0 :b 0 :a 1}
;   :viz/banner-text-color white}]]]]]))


;(defn make-widget [name data options]
;
;  ;(.log js/console ":simple-text" (str data) (str options))
;
;  [:div {:style {:width "100%"
;                 :text-align :center
;                 :border-style  (basic/debug-style options)}}
;   [:p {:style {:fontSize    "50px"
;                :font-weight "bold"
;                :color       "blue"}}
;
;    (get-in data [:data :text])]])