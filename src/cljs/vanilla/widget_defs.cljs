(ns vanilla.widget-defs)


(def widgets [
              ; 1
              {:name        :sankey-widget
               :basis       :chart
               :type        :sankey-chart
               :data-source :sankey-service
               :options     {:viz/title             "Sankey"
                             :viz/banner-color      "darkmagenta"
                             :viz/banner-text-color "white"
                             :viz/dataLabels        true
                             :viz/labelFormat       "{point.name}"
                             :viz/animation         false}}

              ; 2
              {:name        :dependency-widget
               :basis       :chart
               :type        :dependency-chart
               :data-source :sankey-service
               :options     {:viz/title             "Dependency Wheel"
                             :viz/banner-color      "crimson"
                             :viz/banner-text-color "white"
                             :viz/dataLabels        true
                             :viz/animation         false}}


              ; 3
              {:name        :spectrum-area-widget
               :basis       :chart
               :type        :area-chart
               :data-source :spectrum-traces
               :options     {:viz/title             "Channels (area)"
                             :viz/allowDecimals     false
                             :viz/x-title           "frequency"
                             :viz/y-title           "power"
                             :viz/banner-color      "blue"
                             :viz/banner-text-color "white"
                             :viz/style-name        "widget"
                             :viz/animation         false
                             :viz/tooltip           {:followPointer true}}}

              ; 4
              {:name        :bubble-widget
               :basis       :chart
               :type        :bubble-chart
               :data-source :bubble-service
               :options     {:viz/title             "Bubble"
                             :viz/banner-color      "darkgreen"
                             :viz/banner-text-color "white"
                             :viz/dataLabels        true
                             :viz/labelFormat       "{point.name}"
                             :viz/lineWidth         0
                             :viz/animation         false
                             :viz/data-labels       true}}

              ; 5
              {:name        :spectrum-column-widget
               :basis       :chart
               :type        :column-chart
               :data-source :spectrum-traces
               :options     {:viz/title        "Channels (column)"
                             :viz/banner-color "yellow"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}

              ; 6
              {:name        :spectrum-line-widget
               :basis       :chart
               :type        :line-chart
               :data-source :spectrum-traces
               :options     {:viz/title        "Channels (line)"
                             :viz/banner-color "lightgreen"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}

              ; 7
              {:name        :network-widget
               :basis       :chart
               :type        :network-chart
               :data-source :network-service
               :options     {:viz/title             "Network (network)"
                             :viz/banner-color      "black"
                             :viz/banner-text-color "white"
                             :viz/animation         false
                             :viz/data-labels       true}}

              ; 8
              {:name        :org-widget
               :basis       :chart
               :type        :org-chart
               :data-source :network-service
               :options     {:viz/title        "Network (org chart)"
                             :viz/banner-color "darkgray"
                             :viz/animation    false
                             :viz/data-labels  true}}

              ; 9
              {:name        :pie-widget
               :basis       :chart
               :type        :pie-chart
               :data-source :usage-data
               :options     {:viz/title        "Usage Data (pie)"
                             :viz/banner-color "goldenrod"
                             :viz/animation    false
                             :viz/dataLabels   true
                             :viz/labelFormat  "{point.name}"
                             :viz/slice-at     20}}

              ; 10
              {:name        :vari-pie-widget
               :basis       :chart
               :type        :vari-pie-chart
               :data-source :usage-data
               :options     {:viz/title             "Usage Data (vari-pie)"
                             :viz/banner-color      "red"
                             :viz/banner-text-color "white"
                             :viz/animation         false
                             :viz/dataLabels        true
                             :viz/labelFormat       "{point.name}"
                             :viz/slice-at          20}}


              ;{:name        :time-widget
              ; :basis       :simple
              ; :type        :simple-text
              ; :data-source :current-time
              ; :options     {:viz/title        "Current Time"
              ;               :viz/banner-color "lightblue"
              ;               :viz/style        {}
              ;               :viz/height       "100px"}}

              ; TODO - init :stacked-chart correctly
              ;{:name        :spectrum-dual-widget
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
              ; :options {:viz/title        "Map Widget"
              ;           :viz/banner-color "lightblue"
              ;           :viz/style        {}
              ;           :viz/height       "500px"}}

              ; 11
              {:name    :heatmap-widget
               :basis   :chart
               :type    :heatmap-chart
               ;:data-source :spectrum-traces
               :options {:viz/title        "Heat Map"
                         :viz/banner-color "lightgreen"
                         :viz/line-width   0.5
                         :viz/animation    false
                         :viz/style-name   "widget"
                         :viz/tooltip      {:followPointer true}
                         :viz/icon         "timeline"}}

              ; TODO - inint :side-by-side-chart correctlt
              ;{:name        :usage-side-by-side-widget
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
              ; :options     {:viz/title        "Status"
              ;               :viz/banner-color "aqua"}}

              ; 12
              {:name        :spectrum-rose-widget
               :basis       :chart
               :type        :rose-chart
               :data-source :usage-24-hour-service
               :options     {:viz/title        "Channels (rose)"
                             :viz/banner-color "pink"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}

              ; 13
              {:name        :scatter-widget
               :basis       :chart
               :type        :scatter-chart
               :data-source :scatter-service-data
               :options     {:viz/title        "Scatter"
                             :viz/banner-color "purple"
                             :viz/banner-text-color "white"
                             :viz/line-width   0.5
                             :viz/animation    false
                             :viz/style-name   "widget"
                             :viz/tooltip      {:followPointer true}
                             :viz/icon         "timeline"}}])

