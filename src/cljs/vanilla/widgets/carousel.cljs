(ns vanilla.widgets.carousel
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.add-widget :as add-widg]
            [re-frame.core :as rf]
            [clojure.edn :as edn]
            [vanilla.modal :refer [modal]]
            ["react-highcharts" :as ReactHighcharts]
            ["react-highcharts/ReactHighmaps" :as ReactHighmaps]
            ["pure-react-carousel" :refer [CarouselProvider Slider Slide
                                           Dot DotGroup
                                           ButtonFirst ButtonBack ButtonNext ButtonLast]]))


(defn heatmap-data* []
  [{:name "Fruit Production per Continent"
    :keys ["x" "y" "value"]
    :data [[0, 0, 10], [0, 1, 19], [0, 2, 8],
           [0, 3, 24], [0, 4, 67], [1, 0, 92],
           [1, 1, 58], [1, 2, 78], [1, 3, 117],
           [1, 4, 48], [2, 0, 35], [2, 1, 15],
           [2, 2, 123], [2, 3, 64], [2, 4, 52],
           [3, 0, 72], [3, 1, 132], [3, 2, 114],
           [3, 3, 19], [3, 4, 16], [4, 0, 38],
           [4, 1, 5], [4, 2, 8], [4, 3, 117],
           [4, 4, 115], [5, 0, 88], [5, 1, 32],
           [5, 2, 12], [5, 3, 6], [5, 4, 120],
           [6, 0, 13], [6, 1, 44], [6, 2, 88],
           [6, 3, 98], [6, 4, 96], [7, 0, 31],
           [7, 1, 1], [7, 2, 82], [7, 3, 32],
           [7, 4, 30], [8, 0, 85], [8, 1, 97],
           [8, 2, 123], [8, 3, 64], [8, 4, 84],
           [9, 0, 47], [9, 1, 114], [9, 2, 31],
           [9, 3, 48], [9, 4, 91]]}])

(def heatmap-data
  {:title       {:text "Heatmap Chart"}
   :chart       {:type            "heatmap"
                 :zoomType        "xy"
                 :marginTop       40
                 :marginBottom    80
                 :plotBorderWidth 1}
   :xAxis       {:categories ["Apples" "Avocados" "Bananas" "Oranges" "Peaches" "Pears" "Plums" "Prunes" "Starfruit" "Tangerine"]}
   :yAxis       {:categories ["North America" "South America" "Africa" "Europe" "Asia" "Australia" "Antarctica"]
                 :title      ""
                 :reversed   true}
   :colorAxis   {:min      0
                 :minColor "#FFFFFF"
                 :maxColor "#006400"}                       ;Highcharts.getOptions().colors[0]}
   :legend      {:align         "right"
                 :layout        "vertical"
                 :margin        0
                 :verticalAlign "top"
                 :y             25
                 :symbolHeight  280}
   :plotOptions {:series {:dataLabels {:enabled true :color "#000000"}}}
   :series      (heatmap-data*)})


; Carousel
;  https://github.com/express-labs/pure-react-carousel
;

;(def contents
;  [[:> ReactHighcharts {:config heatmap-data}]
;   [:> ReactHighmaps {:config aus-map-data}]])

(defn add-widget [new-widget selected-source] ;this needs to add to a carousel datasource?
  (rf/dispatch [:add-widget new-widget (keyword (:keyword selected-source))]))

(defn add-by-widget-modal
  "modal to allow the user to pick a widget and then select a compatible data-source"

  [is-active]

  (let [services             (rf/subscribe [:services])
        selected             (rf/subscribe [:selected-service])
        chosen-widget        (rf/subscribe [:selected-widget])
        widget-cards         (rf/subscribe [:all-widget-types])
        compatible-selection (rf/subscribe [:compatible-selections])]
    (fn []

      (modal {:is-active             is-active
              :title                 "Add Widget"
              :modal-body-list       [[add-widg/all-widget-list @widget-cards @chosen-widget]
                                      [add-widg/compatible-service-list @services @chosen-widget @selected]]
              :footer-button-enabled @compatible-selection
              :footer-button-fn      #(add-widget (:name @chosen-widget) @selected)
              :footer-button-text    "Add"}))))


(defn carousel [contents]

  (let [is-widget-active (r/atom false)]
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
      [:button.button.is-small {:on-click #(swap! is-widget-active not)} "+"]
      [add-by-widget-modal is-widget-active]

      [:div
       (for [[idx c] (map-indexed vector contents)]
         [:> Dot {:class "button is-small" :slide idx}])]]]))



(defn make-widget [name data options]
  (prn "Making carousel widget: " data
       "///data?data? destringed\\\\ " (edn/read-string (:data (:data data)))
       "/////options: " options)

    (carousel [[:> ReactHighcharts {:config heatmap-data}]]))




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