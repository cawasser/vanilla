(ns vanilla.widget-layout)


(def widget-layout
  {
   ;; Left column wide widgets
   :network-widget            {:layout-opts
                               {:position {:lg {:x 0 :y 0 :w 4 :h 4}
                                           :md {:x 0 :y 0 :w 4 :h 4}
                                           :sm {:x 0 :y 0 :w 4 :h 4 :static true}}}}
   :org-widget                {:layout-opts
                               {:position {:lg {:x 0 :y 4 :w 4 :h 4}
                                           :md {:x 0 :y 4 :w 4 :h 4}
                                           :sm {:x 0 :y 4 :w 4 :h 4 :static true}}}}
   :spectrum-dual-widget      {:layout-opts
                               {:position {:lg {:x 0 :y 8 :w 4 :h 3}
                                           :md {:x 0 :y 8 :w 4 :h 3}
                                           :sm {:x 0 :y 8 :w 2 :h 3 :static true}}}}
   :sankey-widget             {:layout-opts
                               {:position {:lg {:x 0 :y 11 :w 3 :h 3}
                                           :md {:x 0 :y 11 :w 3 :h 3}
                                           :sm {:x 0 :y 11 :w 3 :h 3 :static true}}}}
   :usage-side-by-side-widget {:layout-opts
                               {:position {:lg {:x 0 :y 14 :w 4 :h 2}
                                           :md {:x 0 :y 14 :w 4 :h 2}
                                           :sm {:x 0 :y 14 :w 2 :h 2 :static true}}}}
   :spectrum-area-widget      {:layout-opts
                               {:position {:lg {:x 0 :y 16 :w 2 :h 2}
                                           :md {:x 0 :y 16 :w 2 :h 2}
                                           :sm {:x 0 :y 16 :w 2 :h 2 :static true}}}}
   :map-widget                {:layout-opts
                               {:position {:lg {:x 4 :y 5 :w 2 :h 3}
                                           :md {:x 4 :y 5 :w 2 :h 3}
                                           :sm {:x 0 :y 5 :w 2 :h 3 :static true}}}}
   :heatmap-widget            {:layout-opts
                               {:position {:lg {:x 0 :y 18 :w 4 :h 4}
                                           :md {:x 0 :y 18 :w 4 :h 4}
                                           :sm {:x 0 :y 18 :w 4 :h 4 :static true}}}}

   ;; Right column small widgets
   :time-widget               {:layout-opts
                               {:position {:lg {:x 4 :y 0 :w 2 :h 1}
                                           :md {:x 4 :y 0 :w 2 :h 1}
                                           :sm {:x 0 :y 0 :w 2 :h 1 :static true}}}}
   :health-and-status-widget  {:layout-opts
                               {:position {:lg {:x 4 :y 1 :w 2 :h 2}
                                           :md {:x 4 :y 1 :w 2 :h 2}
                                           :sm {:x 0 :y 1 :w 2 :h 2 :static true}}}}
   :pie-widget                {:layout-opts
                               {:position {:lg {:x 4 :y 3 :w 2 :h 3}
                                           :md {:x 4 :y 3 :w 2 :h 3}
                                           :sm {:x 0 :y 3 :w 2 :h 3 :static true}}}}
   :bubble-widget             {:layout-opts
                               {:position {:lg {:x 4 :y 14 :w 2 :h 2}
                                           :md {:x 4 :y 14 :w 2 :h 2}
                                           :sm {:x 0 :y 14 :w 2 :h 2 :static true}}}}
   :spectrum-line-widget      {:layout-opts
                               {:position {:lg {:x 4 :y 9 :w 2 :h 2}
                                           :md {:x 4 :y 9 :w 2 :h 2}
                                           :sm {:x 0 :y 9 :w 2 :h 2 :static true}}}}
   :dependency-widget         {:layout-opts
                               {:position {:lg {:x 3 :y 11 :w 3 :h 3}
                                           :md {:x 3 :y 11 :w 3 :h 3}
                                           :sm {:x 0 :y 11 :w 3 :h 3 :static true}}}}
   :spectrum-column-widget    {:layout-opts
                               {:position {:lg {:x 2 :y 16 :w 2 :h 2}
                                           :md {:x 2 :y 16 :w 2 :h 2}
                                           :sm {:x 0 :y 16 :w 2 :h 2 :static true}}}}
   :spectrum-rose-widget      {:layout-opts
                               {:position {:lg {:x 4 :y 18 :w 2 :h 3}
                                           :md {:x 4 :y 18 :w 2 :h 3}
                                           :sm {:x 0 :y 18 :w 2 :h 3 :static true}}}}})