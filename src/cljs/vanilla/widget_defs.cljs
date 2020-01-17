(ns vanilla.widget-defs)


(def black {:r 0x00 :g 0x00 :b 0x00 :a 1})
(def white {:r 0xff :g 0xff :b 0xff :a 1})

(def widgets [
              {:name      :area-widget
               :basis     :chart
               :type      :area-chart
               ;:data-source :spectrum-traces
               :ret_types [:data-format/x-y]
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


              {:name      :bar-widget
               :basis     :chart
               :type      :bar-chart
               ;:data-source :spectrum-traces
               :ret_types [:data-format/x-y]
               :icon      "/images/bar-widget.png"
               :label     "Bar"
               :options   {:viz/title        "Channels (bar)"
                           :viz/banner-color {:r 0xd2 :g 0xbf :b 0xd8 :a 1}
                           :viz/line-width   0.5
                           :viz/animation    false
                           :viz/style-name   "widget"
                           :viz/tooltip      {:followPointer true}}}

              {:name      :bubble-widget
               :basis     :chart
               :type      :bubble-chart
               ;:data-source :bubble-service
               :ret_types [:data-format/x-y-n]
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
               ;:data-source :spectrum-traces
               :ret_types   [:data-format/x-y]
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
               ;:data-source :sankey-service
               :ret_types [:data-format/x-y :data-format/from-to :data-format/form-to-n]
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
               ;:data-source :spectrum-traces
               :ret_types [:data-format/x-y-n]
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
               ;:data-source :spectrum-traces
               :ret_types [:data-format/x-y]
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
               ;:data-source :network-service
               :ret_types [:data-format/x-y :data-format/from-to :data-format/form-to-n]
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
               ;:data-source :network-service
               :ret_types [:data-format/x-y :data-format/from-to :data-format/form-to-n]
               :icon      "/images/org-widget.png"
               :label     "Org Chart"
               :options   {:viz/title        "Network (org chart)"
                           :viz/banner-color {:r 0xa9 :g 0xa9 :b 0xa9 :a 1}
                           :viz/animation    false
                           :viz/data-labels  true}}

              {:name      :pie-widget
               :basis     :chart
               :type      :pie-chart
               ;:data-source :usage-data
               :ret_types [:data-format/x-y :data-format/name-y]
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
               ;:data-source :usage-24-hour-service
               :ret_types [:data-format/x-y-n]
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
               ;:data-source :sankey-service
               :ret_types [:data-format/x-y :data-format/from-to :data-format/form-to-n]
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
               ;:data-source :scatter-service-data
               :ret_types [:data-format/x-y]
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
               ;:data-source :usage-data
               :ret_types [:data-format/x-y-n]
               :icon      "/images/vari-pie-widget.png"
               :label     "Variable Pie"
               :options   {:viz/title             "Usage Data (vari-pie)"
                           :viz/banner-color      {:r 0xff :g 0x00 :b 0x00 :a 1}
                           :viz/banner-text-color white
                           :viz/animation         false
                           :viz/dataLabels        true
                           :viz/labelFormat       "{point.name}"
                           :viz/slice-at          20}}])


; TODO - init :time-widget correctly
;{:name        :time-widget
; :basis       :simple
; :type        :simple-text
; :data-source :current-time
; :options     {:viz/title        "Current Time"
;               :viz/banner-color "lightblue"
;               :viz/style        {}
;               :viz/height       "100px"}}


; TODO - init :map-container correctly
;{:name    :map-widget
; :type    :map-container
; ;:data-source :current-time
;:ret_types [:data-format/lat-lon-n]
;:icon "/images/map-widget.png"
;:label "Map"
; :options {:viz/title        "Map Widget"
;           :viz/banner-color "lightblue"
;           :viz/style        {}
;           :viz/height       "500px"}}


; TODO - init :simple correctly
;{:type        :stoplight-widget
; :basis       :simple
; :name        :health-and-status-widget
; :data-source :health-and-status-data
; :ret_types [:data-format/entity]
; :icon      "/images/stoplight-widget.png"
; :label     "Stoplight"
; :options     {:viz/title        "Status"
;               :viz/banner-color "aqua"}}



(comment

  (def widgets {:area-widget
                {:name      :area-widget
                 :basis     :chart
                 :type      :area-chart
                 ;:data-source :spectrum-traces
                 :ret_types [:data-format/x-y-n]
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
                 :ret_types [:data-format/x-y]
                 :icon      "/images/bar-widget.png"
                 :label     "Bar"
                 :options   {:viz/title        "Channels (bar)"
                             :viz/banner-color "tan"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}}}})


  ())

