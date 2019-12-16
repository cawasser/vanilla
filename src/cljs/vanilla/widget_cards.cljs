(ns vanilla.widget-cards)




(def widget-cards
  [{:keywrd :line-widget,
    :ret_types [:data-format/x-y],
    :icon "/images/line-widget.png",
    :label "Line"
    :dummy-name :line-chart}

   {:keywrd :area-widget,
    :ret_types [:data-format/x-y],
    :icon "/images/area-widget.png",
    :label "Area"
    :dummy-name :area-chart}

   {:keywrd :bar-widget,
    :ret_types [:data-format/x-y],
    :icon "/images/bar-widget.png",
    :label "Bar"
    :dummy-name :none}

   {:keywrd :column-widget,
    :ret_types [:data-format/x-y],
    :icon "/images/column-widget.png",
    :label "Column"
    :dummy-name :column-chart}

   {:keywrd :pie-widget,
    :ret_types [:data-format/x-y :data-format/name-y],
    :icon "/images/pie-widget.png",
    :label "Pie"
    :dummy-name :pie-chart}

   {:keywrd :bubble-widget,
    :ret_types [:data-format/x-y-n],
    :icon "/images/bubble-widget.png",
    :label "Bubble"
    :dummy-name :bubble-chart}

   {:keywrd    :vari-pie-widget,
    :ret_types [:data-format/x-y-n],
    :icon      "/images/vari-pie-widget.png",
    :label     "Variable Pie"
    :dummy-name :vari-pie-chart}

   {:keywrd :rose-widget,
    :ret_types [:data-format/x-y-n],
    :icon "/images/rose-widget.png",
    :label "Wind Rose"
    :dummy-name :rose-chart}

   {:keywrd    :stoplight-widget,
    :ret_types [:data-format/entity],
    :icon      "/images/stoplight-widget.png",
    :label     "Stoplight"
    :dummy-name :stoplight-widget}

   {:keywrd :map-widget,
    :ret_types [:data-format/lat-lon-n],
    :icon "/images/map-widget.png",
    :label "Map"
    :dummy-name :map-container}

   {:keywrd :sankey-widget,
    :ret_types [:data-format/x-y :data-format/from-to :data-format/form-to-n],
    :icon "/images/sankey-widget.png",
    :label "Sankey"
    :dummy-name :sankey-chart}

   {:keywrd :deps-widget,
    :ret_types [:data-format/x-y :data-format/from-to :data-format/form-to-n],
    :icon "/images/deps-widget.png",
    :label "Dependencies"
    :dummy-name :dependency-chart}

   {:keywrd :network-widget,
    :ret_types [:data-format/x-y :data-format/from-to :data-format/form-to-n],
    :icon "/images/network-widget.png",
    :label "Network"
    :dummy-name :network-chart}

   {:keywrd :org-widget,
    :ret_types [:data-format/x-y :data-format/from-to :data-format/form-to-n],
    :icon "/images/org-widget.png",
    :label "Org Chart"
    :dummy-name :org-chart}


   {:keywrd :heatmap-widget,
    :ret_types [:data-format/x-y-n],
    :icon "/images/heatmap-widget.png",
    :label "Heatmap"
    :dummy-name :heatmap-chart}

   {:keywrd :scatter-widget,
    :ret_types [:data-format/x-y],
    :icon "/images/scatter-widget.png",
    :label "Scatter"
    :dummy-name :scatter-chart}])