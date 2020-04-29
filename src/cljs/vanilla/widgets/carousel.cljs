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


(def sat-dat1
  [{:name "trace-1", :keys ["x" "y"], :data [[100 9.937227944137275] [100.5 5.934299807156706] [101 7.223178727530351]
                                             [101.5 6.932051668280973] [102 6.612013473240736] [102.5 5.993094710057859]
                                             [103 7.787337657524545] [103.5 9.53945665319439] [104 6.1288318807498605]
                                             [104.5 9.54375159302475] [105 7.52855308421945] [105.5 6.6559592914609045]
                                             [106 8.240627849459404] [106.5 7.79124827746541] [107 7.630760623924645]
                                             [107.5 9.693306965882805] [108 7.37376174850044] [108.5 6.936061300147282]
                                             [109 8.170203377709448] [109.5 6.374498191600212] [110 5.047232918024987]
                                             [110.5 8.840273733410665] [111 9.231499943481513] [111.5 8.49069130375632]
                                             [112 7.970236415298308] [112.5 9.454423404774573] [113 9.394016530892504]
                                             [113.5 6.521100642268431] [114 6.003997937746833] [114.5 5.544792598671834]
                                             [115 9.898023322298393] [115.5 5.8767767290158615] [116 7.77246265915962]
                                             [116.5 5.617290275064755] [117 7.726870793124609] [117.5 9.338733918469623]
                                             [118 9.21129795128234] [118.5 6.446768732266566] [119 7.174431421822582]
                                             [119.5 7.726712612534568] [120 6.755801677528047] [120.5 7.289565536257233]]}])
(def sat-dat2
  [{:name "trace-2", :keys ["x" "y"], :data [[130 9.73071903968296] [130.5 6.738561036175065] [131 7.253389603029975]
                                             [131.5 8.80823984114017] [132 7.5690131283995346] [132.5 7.375088526911348]
                                             [133 7.6333791662964146] [133.5 8.796480851426661] [134 9.139791201038413]
                                             [134.5 5.003384787454093] [135 7.543837693892864] [135.5 5.639142551725815]
                                             [136 9.832477440691555] [136.5 9.263255253222628] [137 9.747709598382759]
                                             [137.5 8.992705756230816] [138 5.770577920190107] [138.5 9.902380400184423]
                                             [139 6.320752232361494] [139.5 8.908114056912659] [140 7.86657410654833]
                                             [140.5 6.504737458394508] [141 5.8345024412819795] [141.5 6.874315838386867]
                                             [142 8.79773484212785] [142.5 7.466732112342667] [143 5.681917710993952]
                                             [143.5 7.089204685071294] [144 5.9999230462909345] [144.5 6.929685062160845]
                                             [145 8.094428130918068] [145.5 5.340923473082421] [146 6.281356350753911]
                                             [146.5 8.890021431269366] [147 9.732144067530859] [147.5 5.348318590371539]
                                             [148 9.069414384476389] [148.5 8.37744917216784] [149 7.468221153829446]
                                             [149.5 7.531398296392866] [150 5.620966852025527] [150.5 5.416613409952889]]}])
(def sat-dat3
  [{:name "trace-3", :keys ["x" "y"], :data [[160 5.795190016325998] [160.5 9.37710232661994] [161 9.271925729431814]
                                             [161.5 9.669354171973954] [162 5.311264715664726] [162.5 6.164116566870412]
                                             [163 5.893325823624794] [163.5 9.737592041950553] [164 6.311677591103658]
                                             [164.5 8.173580136012458] [165 9.548552812201208] [165.5 5.671082298650003]
                                             [166 7.899075330684341] [166.5 6.437818041509182] [167 7.767027229212333]
                                             [167.5 8.408117749365754] [168 7.645890868322383] [168.5 6.186576915776582]
                                             [169 6.969512143331702] [169.5 9.500725367465739] [170 8.209132618665773]
                                             [170.5 8.696995334441862] [171 8.578239436562795] [171.5 8.037767795650105]
                                             [172 8.332308494719662] [172.5 9.859697895952577] [173 8.593836935247552]
                                             [173.5 6.594423776668597] [174 9.148945397595] [174.5 7.494855185439576]
                                             [175 5.833302242657479] [175.5 8.869333754764694] [176 8.113294868590103]
                                             [176.5 5.120999821136202] [177 6.762524790360558] [177.5 6.792766694078313]
                                             [178 8.201200896219692] [178.5 5.704583609068028] [179 9.002365354019895]
                                             [179.5 7.667482689619941] [180 9.247736863516181] [180.5 9.89970405027962]]}])


(def options
  {:viz/title        "Channels (line)"
   :viz/banner-color {:r 255, :g 105, :b 180, :a 1}
   :viz/line-width   0.5
   :viz/animation    false
   :viz/style-name   "widget"
   :viz/tooltip      {:followPointer true}
   :viz/icon         "timeline"})



(defn line-chart1 [name dataset]
  {:title       {:text name}
   :chart       {:type            "line"
                 :zoomType        "x"
                 :plotBorderWidth 1}
   :xAxis       {:title {:text          "frequency"
                         :allowDecimals false}}
   :yAxis       {:title  {:text          "power"
                          :allowDecimals false
                          :align         "high"}
                 :labels {:overflow "justify"}}
   :plotOptions {:series {:animation false}
                 :line   {:dataLabels {:enabled true
                                       :format  ""}
                          :lineWidth  0.5}}
   :series      (get-in @(rf/subscribe [:app-db dataset]) [:data :series])})

(defn big-chart [name dataset]

  (let [data @(rf/subscribe [:app-db dataset])]

    (prn "big-chart" data)

    {:legendBackgroundColor "rgba(48, 48, 48, 0.8)",
     :labels                {:style {:color "#CCC"}},
     :dataLabelsColor       "#444",
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
         ^{:key idx} [:> Slide {:key idx :index idx} c])]]))



      ;[:div {:style {:display :flex :justify-content :center :align-items :center}}
      ; (for [[idx c] (map-indexed vector contents)]
      ;   ^{:key idx} [:> Dot {:class "button is-small" :slide idx}])]]]))



(defn make-widget [name data options]
  ;(prn "Making carousel widget: " data
  ;     "///data?data? destringed\\\\ " (edn/read-string (:data (:data data)))
  ;     "/////options: " options)

  (let [sources [(big-chart "Signal Power (1000)" :channel-power-1000-service)
                 (big-chart "Signal Power (2000)" :channel-power-2000-service)
                 (big-chart "Signal Power (3000)" :channel-power-3000-service)
                 (big-chart "Signal Power (4000)" :channel-power-4000-service)]]

    (carousel
      (for [[idx s] (map-indexed vector sources)]
        ^{:key idx} [:> ReactHighcharts {:config s}]))))








;[:div {:key "9" :data-grid {:x 8 :y 6 :w 4 :h 3}}
; [basic-widget
;  "carousel"
;  [carousel [[:> ReactHighcharts {:config data/heatmap-data}]
;             [:> ReactHighmaps {:config mapping/world-map-data}]
;             [:> ReactHighmaps {:config mapping/aus-map-data}]]]
;  {:viz/title             "Carousel"
;   :viz/banner-color      {:r 255 :g 0 :b 0 :a 1}
;   :viz/banner-text-color white}]]]]]))
