(ns vanilla.widget-defs
  (:require [vanilla.widgets.table-widget]
            [vanilla.widgets.stoplight-widget]
            [vanilla.widgets.simple-text]
            [vanilla.widgets.worldwind]
            [vanilla.widgets.timeline]
            [vanilla.widgets.rough-widget]))



(def black {:r 0x00 :g 0x00 :b 0x00 :a 1})
(def white {:r 0xff :g 0xff :b 0xff :a 1})

(def widgets [
              {:name      :area-widget
               :basis     :chart
               :type      :area-chart
               :ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
               :icon      "/images/area-widget.png"
               :label     "Area"
               :options   {:viz/title             "Channels (area)"
                           :viz/allowDecimals     false
                           :viz/x-title           "frequency"
                           :viz/y-title           "power"
                           :viz/banner-color      {:r 0x00 :g 0x00 :b 0xff :a 1}
                           :viz/banner-text-color white
                           :viz/style-name        "widget"
                           :viz/animation         false
                           :viz/tooltip           {:followPointer true}}}

              {:name      :arearange-widget
               :basis     :chart
               :type      :arearange-chart
               :ret_types [:data-format/date-yl-yh]
               :icon      "/images/arearange-widget.png"
               :label     "AreaRange"
               :options   {:viz/title             "Area Range"
                           :viz/allowDecimals     false
                           :viz/x-title           "Date"
                           :viz/y-title           "Temperature"
                           :viz/banner-color      {:r 0x00 :g 0x00 :b 0xff :a 1}
                           :viz/banner-text-color white
                           :viz/style-name        "widget"
                           :viz/animation         false
                           :viz/tooltip           {:followPointer true
                                                   :crosshairs true
                                                   :shared true
                                                   :valueSuffix "°C"
                                                   :xDateFormat "%A %b %e"}}}

              {:name        :stoplight-widget
               :basis       :simple
               :type        :stoplight-widget
               :build-fn    vanilla.widgets.stoplight-widget/make-widget
               ;:data-source :health-and-status-data
               :ret_types   [:data-format/entity]
               :icon        "/images/stoplight-widget.png"
               :label       "Stoplight"
               :options     {:viz/title        "Status"
                             :viz/banner-color {:r 0x00 :g 0xff :b 0xff :a 1}}}

              {:name        :simple-text-widget
               :basis       :simple
               :type        :simple-text-widget
               :build-fn    vanilla.widgets.simple-text/make-widget
               ;:data-source :current-time
               :ret_types   [:data-format/string]
               :icon        "/images/simple-text-widget.png"
               :label       "Current Time"
               :options     {:viz/title        "Time"
                             :viz/banner-color {:r 0x00 :g 0xff :b 0xff :a 1}}}

              {:name        :rough-widget
               :basis       :simple
               :type        :rough-widget
               :build-fn    vanilla.widgets.rough-widget/make-widget
               :ret_types   [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y
                             :data-format/lat-lon-label :data-format/cont-n]
               :icon        "/images/rough-widget.png"
               :label       "Hand drawn"
               :options     {:viz/title        "Hand drawn"
                             :viz/banner-color {:r 0xc0 :g 0x00 :b 0xc0 :a 1}
                             :viz/banner-text-color white}}

              {:name        :worldwind-widget
               :basis       :simple
               :type        :worldwind-widget
               :build-fn    vanilla.widgets.worldwind/make-widget
               :ret_types   [:data-format/lat-lon-label :data-format/cont-n]
               :icon        "/images/worldwind-widget.png"
               :label       "3d World"
               :options     {:viz/title        "3d World"
                             :viz/banner-color {:r 0x99 :g 0x00 :b 0xff :a 1}
                             :viz/banner-text-color white}}

              {:name        :timeline-widget
               :basis       :simple
               :type        :timeline-widget
               :build-fn    vanilla.widgets.timeline/make-widget
               :ret_types   [:data-format/task-link]
               :icon        "/images/timeline-widget.png"
               :label       "Timeline"
               :options     {:viz/title        "Timeline"
                             :viz/banner-color {:r 0x99 :g 0xff :b 0x00 :a 1}
                             :viz/banner-text-color black}}

              {:name        :carousel-widget
               :basis       :simple
               :type        :carousel-widget
               :build-fn    vanilla.widgets.carousel/make-widget
               :ret_types   [:data-format/carousel]
               :icon        "/images/carousel-widget.png"
               :label       "Carousel"
               :options     {:viz/title        "Carousel"
                             :viz/banner-color {:r 0x99 :g 0xff :b 0x00 :a 1}
                             :viz/banner-text-color black}}

              {:name      :table-widget
               :basis     :simple
               :type      :table-widget
               :build-fn  vanilla.widgets.table-widget/make-widget
               ;:data-source :table-widget
               :ret_types [:data-format/entities]
               :icon      "/images/table-widget.png"
               :label     "Data Table"
               :options   {:viz/title        "Table"
                           :viz/banner-color {:r 0x00 :g 0xff :b 0xff :a 1}}}

              {:name      :bar-widget
               :basis     :chart
               :type      :bar-chart
               :ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
               :icon      "/images/bar-widget.png"
               :label     "Bar"
               :options   {:viz/title        "Channels (bar)"
                           :viz/banner-color {:r 0xd2 :g 0xbf :b 0xd8 :a 1}
                           :viz/line-width   0.5
                           :viz/animation    false
                           :viz/style-name   "widget"
                           :viz/tooltip      {:followPointer true}}}

              {:name      :continent-map-widget
               :basis     :map
               :type      :continent-map
               :ret_types [:data-format/cont-n]
               :icon      "/images/map-widget.png"
               :label     "Continent Map"
               :options   {:viz/title        "Continents (map)"
                           :viz/banner-color {:r 0xd2 :g 0xbf :b 0xd8 :a 1}
                           :viz/line-width   0.5
                           :viz/animation    false
                           :viz/style-name   "widget"
                           :viz/tooltip      {:followPointer true}}}

              {:name      :australia-map-widget
               :basis     :map
               :type      :australia-map
               :ret_types [:data-format/lat-lon-label]
               :icon      "/images/australia-map-widget.png"
               :label     "Australia Map"
               :options   {:viz/title        "Australia (map)"
                           :viz/banner-color {:r 0xd2 :g 0xbf :b 0xd8 :a 1}
                           :viz/line-width   0.5
                           :viz/animation    false
                           :viz/style-name   "widget"
                           :viz/tooltip      {:followPointer true}}}

              {:name      :bubble-widget
               :basis     :chart
               :type      :bubble-chart
               :ret_types [:data-format/x-y-n :data-format/x-y :data-format/x-y-e :data-format/y]
               :icon      "/images/bubble-widget.png"
               :label     "Bubble"
               :options   {:viz/title             "Bubble"
                           :viz/banner-color      {:r 0x00 :g 0x64 :b 0x00 :a 1}
                           :viz/banner-text-color white
                           :viz/dataLabels        true
                           :viz/labelFormat       "{point.name}"
                           :viz/lineWidth         0
                           :viz/animation         false
                           :viz/data-labels       true}}

              {:name        :column-widget
               :basis       :chart
               :type        :column-chart
               :ret_types   [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
               :icon        "/images/column-widget.png"
               :label       "Column"
               :options     {:viz/title        "Channels (column)"
                             :viz/banner-color {:r 0xff :g 0xff :b 0x00 :a 1}
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}}}

              {:name      :dependency-widget
               :basis     :chart
               :type      :dependency-chart
               :ret_types [:data-format/from-to-n :data-format/from-to-e :data-format/from-to]
               :icon      "/images/deps-widget.png"
               :label     "Dependencies"
               :options   {:viz/title             "Dependency Wheel"
                           :viz/banner-color      {:r 0xdc :g 0x14 :b 0x3c :a 1}
                           :viz/banner-text-color white
                           :viz/dataLabels        true
                           :viz/animation         false}}

              {:name      :heatmap-widget
               :basis     :chart
               :type      :heatmap-chart
               :ret_types [:data-format/grid-n :data-format/grid-e :data-format/lat-lon-n :data-format/lat-lon-e]
               :icon      "/images/heatmap-widget.png"
               :label     "Heatmap"
               :options   {:viz/title        "Heat Map"
                           :viz/banner-color {:r 0x90 :g 0xee :b 0x90 :a 1}
                           :viz/line-width   0.5
                           :viz/animation    false
                           :viz/style-name   "widget"
                           :viz/tooltip      {:followPointer true}
                           :viz/icon         "timeline"}}

              {:name      :line-widget
               :basis     :chart
               :type      :line-chart
               :ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
               :icon      "/images/line-widget.png"
               :label     "Line"
               :options   {:viz/title        "Channels (line)"
                           :viz/banner-color {:r 0xff :g 0x69 :b 0xb4 :a 1}
                           :viz/line-width   0.5
                           :viz/animation    false
                           :viz/style-name   "widget"
                           :viz/tooltip      {:followPointer true}
                           :viz/icon         "timeline"}}

              {:name      :network-widget
               :basis     :chart
               :type      :network-chart
               :ret_types [:data-format/from-to :data-format/from-to-n :data-format/from-to-e]
               :icon      "/images/network-widget.png"
               :label     "Network"
               :options   {:viz/title             "Network (network)"
                           :viz/banner-color      {:r 0x00 :g 0x00 :b 0x00 :a 1}
                           :viz/banner-text-color white
                           :viz/animation         false
                           :viz/data-labels       true}}

              {:name      :org-widget
               :basis     :chart
               :type      :org-chart
               :ret_types [:data-format/from-to :data-format/from-to-n :data-format/from-to-e]
               :icon      "/images/org-widget.png"
               :label     "Org Chart"
               :options   {:viz/title        "Network (org chart)"
                           :viz/banner-color {:r 0xa9 :g 0xa9 :b 0xa9 :a 1}
                           :viz/animation    false
                           :viz/data-labels  true}}

              {:name      :pie-widget
               :basis     :chart
               :type      :pie-chart
               :ret_types [:data-format/label-y]
               :icon      "/images/pie-widget.png"
               :label     "Pie"
               :options   {:viz/title        "Usage Data (pie)"
                           :viz/banner-color {:r 0xda :g 0xa5 :b 0x20 :a 1}
                           :viz/animation    false
                           :viz/dataLabels   true
                           :viz/labelFormat  "{point.name}"
                           :viz/slice-at     20}}

              {:name      :rose-widget
               :basis     :chart
               :type      :rose-chart
               :ret_types [:data-format/rose-y-n :data-format/rose-y-e]
               :icon      "/images/rose-widget.png"
               :label     "Wind Rose"
               :options   {:viz/title        "Channels (rose)"
                           :viz/banner-color {:r 0xff :g 0xc0 :b 0xcb :a 1}
                           :viz/line-width   0.5
                           :viz/animation    false
                           :viz/style-name   "widget"
                           :viz/tooltip      {:followPointer true}
                           :viz/icon         "timeline"}}

              {:name      :sankey-widget
               :basis     :chart
               :type      :sankey-chart
               :ret_types [:data-format/from-to-n :data-format/from-to :data-format/from-to-e]
               :icon      "/images/sankey-widget.png"
               :label     "Sankey"
               :options   {:viz/title             "Sankey"
                           :viz/banner-color      {:r 0x8b :g 0x00 :b 0x8b :a 1}
                           :viz/banner-text-color white
                           :viz/dataLabels        true
                           :viz/labelFormat       "{point.name}"
                           :viz/animation         false}}

              {:name      :scatter-widget
               :basis     :chart
               :type      :scatter-chart
               :ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
               :icon      "/images/scatter-widget.png"
               :label     "Scatter"
               :options   {:viz/title             "Scatter"
                           :viz/banner-color      {:r 0x90 :g 0x00 :b 0x80 :a 1}
                           :viz/banner-text-color white
                           :viz/line-width        0.5
                           :viz/animation         false
                           :viz/style-name        "widget"
                           :viz/tooltip           {:followPointer true}
                           :viz/icon              "timeline"}}

              {:name      :vari-pie-widget
               :basis     :chart
               :type      :vari-pie-chart
               :ret_types [:data-format/label-y-n :data-format/label-y :data-format/label-y-e]
               :icon      "/images/vari-pie-widget.png"
               :label     "Variable Pie"
               :options   {:viz/title             "Usage Data (vari-pie)"
                           :viz/banner-color      {:r 0xff :g 0x00 :b 0x00 :a 1}
                           :viz/banner-text-color white
                           :viz/animation         false
                           :viz/dataLabels        true
                           :viz/labelFormat       "{point.name}"
                           :viz/slice-at          20}}])





(comment

  (def widgets {:area-widget
                {:name      :area-widget
                 :basis     :chart
                 :type      :area-chart
                 ;:data-source :spectrum-traces
                 :ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
                 :icon      "/images/area-widget.png"
                 :label     "Area"
                 :options   {:viz/title             "Channels (area)"
                             :viz/allowDecimals     false
                             :viz/x-title           "frequency"
                             :viz/y-title           "power"
                             :viz/banner-color      "blue"
                             :viz/banner-text-color white
                             :viz/style-name        "widget"
                             :viz/animation         false
                             :viz/tooltip           {:followPointer true}}}

                :bar-widget
                {:name      :bar-widget
                 :basis     :chart
                 :type      :bar-chart
                 ;:data-source :spectrum-traces
                 :ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y]
                 :icon      "/images/bar-widget.png"
                 :label     "Bar"
                 :options   {:viz/title        "Channels (bar)"
                             :viz/banner-color "tan"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}}}})


  ())

