(ns vanilla.widget-defs)


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
                           :viz/banner-color      "blue"
                           :viz/banner-text-color "white"
                           :viz/style-name        "widget"
                           :viz/animation         false
                           :viz/tooltip           {:followPointer true}}}

              {:name        :stoplight-widget
               :basis       :simple
               :type        :stoplight-chart
               :data-source :health-and-status-data
               :ret_types   [:data-format/entity]
               :icon        "/images/stoplight-widget.png"
               :label       "Stoplight"
               :options     {:viz/title        "Status"
                             :viz/banner-color "aqua"}}

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
                           :viz/tooltip      {:followPointer true}}}

              {:name      :bubble-widget
               :basis     :chart
               :type      :bubble-chart
               ;:data-source :bubble-service
               :ret_types [:data-format/x-y-n]
               :icon      "/images/bubble-widget.png"
               :label     "Bubble"
               :options   {:viz/title             "Bubble"
                           :viz/banner-color      "darkgreen"
                           :viz/banner-text-color "white"
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
                             :viz/banner-color "yellow"
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
                           :viz/banner-color      "crimson"
                           :viz/banner-text-color "white"
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
                           :viz/banner-color "lightgreen"
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
                           :viz/banner-color "lightgreen"
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
                           :viz/banner-color      "black"
                           :viz/banner-text-color "white"
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
                           :viz/banner-color "darkgray"
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
                           :viz/banner-color "goldenrod"
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
                           :viz/banner-color "pink"
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
                           :viz/banner-color      "darkmagenta"
                           :viz/banner-text-color "white"
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
                           :viz/banner-color      "purple"
                           :viz/banner-text-color "white"
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
                           :viz/banner-color      "red"
                           :viz/banner-text-color "white"
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

; TODO - init :stacked-chart correctly
;{:name        :dual-widget
; :basis       :stacked-chart
; :type        :line-column-stack
; :chart-types [:line-chart :column-chart]
; :data-source :spectrum-traces
; :options     {:viz/title        "Channels (stacked)"
;               :viz/banner-color "lightsalmon"
;               :viz/line-width   0.5
;               :viz/animation    false
;               :viz/style-name   "widget"
;               :viz/tooltip      {:followPointer true}}}

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

; TODO - inint :side-by-side-chart correctlt
;{:name        :side-by-side-widget
; :basis       :side-by-side-chart
; :type        :bar-pie-sbs
; :chart-types [:bar-chart :pie-chart]
; :data-source :usage-data
; :options     {:viz/title             "Usage Data (side-by-side)"
;               :viz/banner-color      "lavender"
;               :viz/banner-text-color "black"
;               :viz/line-width        0.5
;               :viz/style-name        "widget"
;               :viz/animation         false
;               :viz/dataLabels        true
;               :viz/labelFormat       "{point.name}"
;               :viz/tooltip           {:followPointer true}}}

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
                             :viz/banner-text-color "white"
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

