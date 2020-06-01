(ns vanilla.widgets.carousel
  (:require [reagent.core :as r :refer [atom]]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.add-widget :as add-widg]
            [re-frame.core :as rf]
            [clojure.edn :as edn]
            [vanilla.modal :refer [modal]]
            ["highcharts" :as Highcharts]
            ["highcharts-more" :as HighchartsMore]
            ["react-highcharts/ReactHighmaps" :as ReactHighmaps]
            ["pure-react-carousel" :refer [CarouselProvider Slider Slide
                                           Dot DotGroup
                                           ButtonFirst ButtonBack ButtonNext ButtonLast]]
            [vanilla.data-source-subscribe :as ds]))

(HighchartsMore Highcharts)


;(def options
;  {:viz/title        "Channels (line)"
;   :viz/banner-color {:r 255, :g 105, :b 180, :a 1}
;   :viz/line-width   0.5
;   :viz/animation    false
;   :viz/style-name   "widget"
;   :viz/tooltip      {:followPointer true}
;   :viz/icon         "timeline"})
;
;
;
;(defn line-chart1 [name dataset]
;  {:title       {:text name}
;   :chart       {:type            "line"
;                 :zoomType        "x"
;                 :plotBorderWidth 1}
;   :xAxis       {:title {:text          "frequency"
;                         :allowDecimals false}}
;   :yAxis       {:title  {:text          "power"
;                          :allowDecimals false
;                          :align         "high"}
;                 :labels {:overflow "justify"}}
;   :plotOptions {:series {:animation false}
;                 :line   {:dataLabels {:enabled true
;                                       :format  ""}
;                          :lineWidth  0.5}}
;   :series      (get-in @(rf/subscribe [:app-db dataset]) [:data :series])})

(defn big-chart [name dataset]

  (let [data @(rf/subscribe [:app-db dataset])]

    ;(prn "big-chart" data)

    {:legendBackgroundColor "rgba(48, 48, 48, 0.8)",
     :labels                {:style {:color "#CCC"}},
     :dataLabelsColor       "#444",
     :boost {:useGPUTranslations true}

     ;:chart/supported-formats [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y],
     :background2           "rgb(70, 70, 70)",
     :navigation            {:buttonOptions {:symbolStroke      "#DDDDDD",
                                             :hoverSymbolStroke "#FFFFFF",
                                             :theme             {:fill   {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                                          :stops          [[0.4 "#606060"] [0.6 "#333333"]]},
                                                                 :stroke "#000000"}}},
     :chart/type            :line-chart,
     :legend                {:itemStyle       {:color "#CCC"},
                             :itemHoverStyle  {:color "#FFF"},
                             :itemHiddenStyle {:color "#333"}},
     :colors                ["#DDDF0D" "#7798BF" "#55BF3B" "#DF5353" "#aaeeee" "#ff0066" "#eeaaee" "#55BF3B" "#DF5353" "#7798BF" "#aaeeee"],
     :rangeSelector         {:buttonTheme {:fill   {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1}, :stops [[0.4 "#888"] [0.6 "#555"]]},
                                           :stroke "#000000",
                                           :style  {:color "#CCC", :fontWeight "bold"},
                                           :states {:hover  {:fill   {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1}, :stops [[0.4 "#BBB"] [0.6 "#888"]]},
                                                             :stroke "#000000",
                                                             :style  {:color "white"}},
                                                    :select {:fill   {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                                      :stops          [[0.1 "#000"] [0.3 "#333"]]},
                                                             :stroke "#000000", :style {:color "yellow"}}}},
                             :inputStyle  {:backgroundColor "#333", :color "silver"}, :labelStyle {:color "silver"}},
     :plotOptions           {:series  {:animation false, :nullColor "#444444"},
                             :line    {:dataLabels {:enabled false, :format "", :color "#CCC"},
                                       :lineWidth  1,
                                       :marker     {:lineColor "#333"}},
                             :spline  {:marker {:lineColor "#333"}},
                             :scatter {:marker {:lineColor "#333"}}, :candlestick {:lineColor "white"}},
     :title                 {:text  name,
                             :style {:labels {:fontFamily "monospace", :color "#FFFFFF"},
                                     :color  "#FFF", :font "16px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
     :chart                 {:type            "line",
                             :zoomType        "x",
                             :backgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                               :stops          [[0 "rgb(96, 96, 96)"] [1 "rgb(16, 16, 16)"]]},
                             :borderWidth     0, :borderRadius 0, :plotBackgroundColor nil, :plotShadow false, :plotBorderWidth 0},
     :yAxis                 {:title              {:text          "power",
                                                  :allowDecimals false,
                                                  :align         "high",
                                                  :style         {:color "#AAA", :font "bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
                             :labels             {:overflow "justify", :style {:color "#999", :fontWeight "bold"}},
                             :alternateGridColor nil,
                             :minorTickInterval  nil,
                             :gridLineColor      "rgba(255, 255, 255, .1)",
                             :minorGridLineColor "rgba(255,255,255,0.07)",
                             :lineWidth          0, :tickWidth 0},
     :textColor             "#E0E0E0",
     :vanilla-mode          "dark",
     :credits               {:enabled false},
     :subtitle              {:text "", :style {:color "#DDD", :font "12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
     :navigator             {:handles      {:backgroundColor "#666", :borderColor "#AAA"},
                             :outlineColor "#CCC",
                             :maskFill     "rgba(16, 16, 16, 0.5)",
                             :series       {:color "#7798BF", :lineColor "#A6C7ED"}},
     :maskColor             "rgba(255,255,255,0.3)",
     :toolbar               {:itemStyle {:color "#CCC"}},
     :xAxis                 {:title         {:text          "frequency",
                                             :allowDecimals false,
                                             :style         {:color "#AAA", :font "bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
                             :gridLineWidth 0,
                             :lineColor     "#999",
                             :tickColor     "#999",
                             :labels        {:style {:color "#999", :fontWeight "bold"}}},
     :scrollbar             {:barBackgroundColor    {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                     :stops          [[0.4 "#888"] [0.6 "#555"]]},
                             :barBorderColor        "#CCC",
                             :buttonArrowColor      "#CCC",
                             :buttonBackgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                     :stops          [[0.4 "#888"] [0.6 "#555"]]},
                             :buttonBorderColor     "#CCC",
                             :rifleColor            "#FFF",
                             :trackBackgroundColor  {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                     :stops          [[0 "#000"] [1 "#333"]]},
                             :trackBorderColor      "#666"},
     :tooltip               {:valueSuffix "", :backgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                                :stops          [[0 "rgba(96, 96, 96, .8)"] [1 "rgba(16, 16, 16, .8)"]]},
                             :borderWidth 0, :style {:color "#FFF"}}
     :series                (get-in data [:data :series])}))





; Carousel
;  https://github.com/express-labs/pure-react-carousel
;

(defn add-widget [new-widget selected-source]               ;this needs to add to a carousel datasource?
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
    ;[:div {:style {:width "100%" :height "100%"}}
     [:> CarouselProvider {:style {:width "100%" :height "100%"}
                           :naturalSlideWidth  640
                           :naturalSlideHeight 420
                           :totalSlides        (count contents)
                           :dragEnabled        false}
      [:div {:style {:display :flex :justify-content :center :align-items :center}}
       [:button.button.is-small {:on-click #(swap! is-widget-active not)} "+"]
       [:> ButtonFirst {:class "button is-small"} "First"]
       [:> ButtonBack {:class "button is-small"} "Back"]
       [:> ButtonNext {:class "button is-small"} "Next"]
       [:> ButtonLast {:class "button is-small"} "Last"]
       [add-by-widget-modal is-widget-active]]

      [:> Slider {:class "slider" :style {:width "100%" :height "100%"}}
       (for [[idx c] (map-indexed vector contents)]
         ^{:key idx} [:> Slide {:key idx :index idx} c ])]]))



      ;[:div {:style {:display :flex :justify-content :center :align-items :center}}
      ; (for [[idx c] (map-indexed vector contents)]
      ;   ^{:key idx} [:> Dot {:class "button is-small" :slide idx}])]]]))



(defn make-widget [name data options]
  ;(prn "Making carousel widget: " data
  ;     "///data?data? destringed\\\\ " (edn/read-string (:data (:data data)))
  ;     "/////options: " options)

  ; TODO: this is a hack for the following hack (does NOT unsubscribe to sources when widget closes)
  (ds/data-source-subscribe [:channel-power-1000-service :channel-power-2000-service
                             :channel-power-3000-service :channel-power-4000-service])

  (let [sources [(big-chart "Signal Power (1000)" :channel-power-1000-service)
                 (big-chart "Signal Power (2000)" :channel-power-2000-service)
                 (big-chart "Signal Power (3000)" :channel-power-3000-service)
                 (big-chart "Signal Power (4000)" :channel-power-4000-service)]]

    (carousel
      (for [[idx s] (map-indexed vector sources)]

        (r/reactify-component
          (r/create-class
            {:reagent-render
             (fn [args]
               [:div {:style {:width "100%" :height "100%"}}])

             :component-did-mount
             (fn [this]
               ^{:key idx} [:> (Highcharts/chart (r/dom-node this) (clj->js s))])}))))))


      ;(for [[idx s] (map-indexed vector sources)]
      ;  ^{:key idx} [:> ReactHighcharts {:config s}]))))








;[:div {:key "9" :data-grid {:x 8 :y 6 :w 4 :h 3}}
; [basic-widget
;  "carousel"
;  [carousel [[:> ReactHighcharts {:config data/heatmap-data}]
;             [:> ReactHighmaps {:config mapping/world-map-data}]
;             [:> ReactHighmaps {:config mapping/aus-map-data}]]]
;  {:viz/title             "Carousel"
;   :viz/banner-color      {:r 255 :g 0 :b 0 :a 1}
;   :viz/banner-text-color white}]]]]]))
