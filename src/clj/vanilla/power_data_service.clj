(ns vanilla.power-data-service
  (:require [clojure.tools.logging :as log]))


(defn zipvec [keys vals]
  (loop [v []
         ks (seq keys)
         vs (seq vals)]
    (if (and ks vs)
      (recur (conj v [(first ks) (first vs)])
        (next ks)
        (next vs))
      v)))

(defn- make-trace [start-x count step min-y y-delta x-precision]
  (let [x (with-precision
            x-precision
            (range (bigdec start-x)
              (bigdec (+ start-x  (* count step)))
              (bigdec step)))
        y (take count (repeatedly #(+ min-y (rand y-delta))))]

    (zipvec x y)))


(defn power-data []
  [{:name   "dBM-1"
    :keys ["x" "y"]
    :data (make-trace 7900 500 0.1 -200 150 1)}])



(defn fetch-data []
  (log/info "Power Data")

  {:title       "Power Data"
   :data-format :data-format/x-y
   :series      (power-data)})





(comment

  (def temp
    {:legendBackgroundColor "rgba(48, 48, 48, 0.8)",
     :labels                {:style {:color "#CCC"}},
     :dataLabelsColor       "#444",
     :boost                 {:useGPUTranslations true},
     :chart                 {:type            "line",
                             :zoomType        "x",
                             :backgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                               :stops          [[0 "rgb(96, 96, 96)"] [1 "rgb(16, 16, 16)"]]},
                             :borderWidth     0,
                             :borderRadius 0,
                             :plotBackgroundColor nil,
                             :plotShadow false,
                             :plotBorderWidth 0}})

  (update-in temp [:chart] assoc :height 500 :width 660)

  (bigdec 3.0)

  (with-precision 1 (range (bigdec 7900) (bigdec 8400) (bigdec 0.1)))

  (with-precision 1 (range 7900 (+ 7900 (* 5 0.1)) 0.1))

  (make-trace 7900 250 0.1 -20 15 1)


  (into []
    (for [[x y] (make-trace 7900 25 0.1 -200 150 1)]
      [x y]))

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  {:legendBackgroundColor "rgba(48, 48, 48, 0.8)",
   :labels {:style {:color "#CCC"}},
   :dataLabelsColor "#444",
   :chart/supported-formats [:data-format/label-y],
   :series [{:colorByPoint true,
             :keys ["name" "y" "selected" "sliced"],
             :data (["Apples" 69.18047982189528 false false]
                    ["Pears" 91.56474936850996 false false]
                    ["Oranges" 74.49409121640193 false false]
                    ["Plums" 35.98583328386419 false true]
                    ["Bananas" 37.32379297976045 false true]
                    ["Peaches" 4.406835424042477 false true]
                    ["Prunes" 0.29254319096659875 false true]
                    ["Avocados" 72.3509284953235 false false])}],
   :background2 "rgb(70, 70, 70)",
   :navigation {:buttonOptions {:symbolStroke "#DDDDDD",
                                :hoverSymbolStroke "#FFFFFF",
                                :theme {:fill {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                               :stops [[0.4 "#606060"] [0.6 "#333333"]]},
                                        :stroke "#000000"}}},
   :chart/type :pie-chart,
   :legend {:align "right",
            :verticalAlign "top",
            :layout "vertical",
            :itemStyle {:color "#CCC"},
            :itemHoverStyle {:color "#FFF"},
            :itemHiddenStyle {:color "#333"}},
   :colors ["#DDDF0D" "#7798BF" "#55BF3B" "#DF5353" "#aaeeee" "#ff0066" "#eeaaee" "#55BF3B" "#DF5353" "#7798BF" "#aaeeee"],
   :rangeSelector {:buttonTheme {:fill {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                        :stops [[0.4 "#888"] [0.6 "#555"]]},
                                 :stroke "#000000",
                                 :style {:color "#CCC", :fontWeight "bold"},
                                 :states {:hover {:fill {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                         :stops [[0.4 "#BBB"] [0.6 "#888"]]},
                                                  :stroke "#000000", :style {:color "white"}},
                                          :select {:fill {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                                                          :stops [[0.1 "#000"] [0.3 "#333"]]},
                                                   :stroke "#000000", :style {:color "yellow"}}}},
                   :inputStyle {:backgroundColor "#333", :color "silver"},
                   :labelStyle {:color "silver"}},
   :plotOptions {:series {:animation false, :nullColor "#444444"},
                 :pie {:allowPointSelect true,
                       :dataLabels {:enabled true,
                                    :format "{point.name}"},
                       :showInLegend true},
                 :line {:dataLabels {:color "#CCC"},
                        :marker {:lineColor "#333"}},
                 :spline {:marker {:lineColor "#333"}},
                 :scatter {:marker {:lineColor "#333"}},
                 :candlestick {:lineColor "white"}},
   :title {:text "", :style {:labels {:fontFamily "monospace", :color "#FFFFFF"},
                             :color "#FFF", :font "16px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
   :chart {:borderRadius 0,
           :width 800,
           :type "pie",
           :plotBorderWidth 0,
           :borderWidth 0,
           :style {:labels {:fontFamily "monospace",
                            :color "#FFFFFF"}},
           :plotBackgroundColor nil,
           :plotShadow false,
           :backgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                             :stops [[0 "rgb(96, 96, 96)"] [1 "rgb(16, 16, 16)"]]},
           :height 300},
   :yAxis {:title {:align "high",
                   :style {:color "#AAA", :font "bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
           :labels {:overflow "justify",
                    :style {:color "#999",
                            :fontWeight "bold"}},
           :alternateGridColor nil,
           :minorTickInterval nil,
           :gridLineColor "rgba(255, 255, 255, .1)",
           :minorGridLineColor "rgba(255,255,255,0.07)",
           :lineWidth 0, :tickWidth 0},
   :textColor "#E0E0E0",
   :vanilla-mode "dark",
   :credits {:enabled false},
   :subtitle {:text "", :style {:color "#DDD", :font "12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}},
   :navigator {:handles {:backgroundColor "#666", :borderColor "#AAA"},
               :outlineColor "#CCC",
               :maskFill "rgba(16, 16, 16, 0.5)",
               :series {:color "#7798BF", :lineColor "#A6C7ED"}},
   :maskColor "rgba(255,255,255,0.3)",
   :toolbar {:itemStyle {:color "#CCC"}},
   :xAxis {:gridLineWidth 0, :lineColor "#999", :tickColor "#999", :labels {:style {:color "#999", :fontWeight "bold"}},
           :title {:style {:color "#AAA", :font "bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif"}}},
   :scrollbar {:barBackgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1}, :stops [[0.4 "#888"] [0.6 "#555"]]},
               :barBorderColor "#CCC",
               :buttonArrowColor "#CCC",
               :buttonBackgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1}, :stops [[0.4 "#888"] [0.6 "#555"]]},
               :buttonBorderColor "#CCC",
               :rifleColor "#FFF",
               :trackBackgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1}, :stops [[0 "#000"] [1 "#333"]]},
               :trackBorderColor "#666"},
   :tooltip {:valueSuffix "",
             :backgroundColor {:linearGradient {:x1 0, :y1 0, :x2 0, :y2 1},
                               :stops [[0 "rgba(96, 96, 96, .8)"] [1 "rgba(16, 16, 16, .8)"]]},
             :borderWidth 0, :style {:color "#FFF"}}}





  ())



