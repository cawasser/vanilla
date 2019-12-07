(ns vanilla.widget-layout)


(def widget-layout
  {
   ; row 1
   :network-widget            {:layout-opts
                               {:position {:lg {:x 0 :y 0 :w 3 :h 4}
                                           :md {:x 0 :y 0 :w 3 :h 4}
                                           :sm {:x 0 :y 0 :w 3 :h 4 :static true}}}}
   :org-widget                {:layout-opts
                               {:position {:lg {:x 3 :y 0 :w 3 :h 4}
                                           :md {:x 3 :y 0 :w 3 :h 4}
                                           :sm {:x 3 :y 0 :w 3 :h 4 :static true}}}}

   ; row 2
   :health-and-status-widget  {:layout-opts
                               {:position {:lg {:x 0 :y 4 :w 2 :h 3}
                                           :md {:x 0 :y 4 :w 2 :h 3}
                                           :sm {:x 0 :y 4 :w 2 :h 3 :static true}}}}
   :bubble-widget             {:layout-opts
                               {:position {:lg {:x 2 :y 4 :w 2 :h 3}
                                           :md {:x 2 :y 4 :w 2 :h 3}
                                           :sm {:x 2 :y 4 :w 2 :h 3 :static true}}}}
   :spectrum-rose-widget      {:layout-opts
                               {:position {:lg {:x 4 :y 4 :w 2 :h 3}
                                           :md {:x 4 :y 4 :w 2 :h 3}
                                           :sm {:x 4 :y 4 :w 2 :h 3 :static true}}}}

   ; row 3
   :spectrum-area-widget      {:layout-opts
                               {:position {:lg {:x 0 :y 7 :w 2 :h 3}
                                           :md {:x 0 :y 7 :w 2 :h 3}
                                           :sm {:x 0 :y 7 :w 2 :h 3 :static true}}}}
   :pie-widget                {:layout-opts
                               {:position {:lg {:x 2 :y 7 :w 2 :h 3}
                                           :md {:x 2 :y 7 :w 2 :h 3}
                                           :sm {:x 2 :y 7 :w 2 :h 3 :static true}}}}
   :vari-pie-widget           {:layout-opts
                               {:position {:lg {:x 4 :y 7 :w 2 :h 3}
                                           :md {:x 4 :y 7 :w 2 :h 3}
                                           :sm {:x 4 :y 7 :w 2 :h 3 :static true}}}}

   ; row 4
   :spectrum-dual-widget      {:layout-opts
                               {:position {:lg {:x 0 :y 10 :w 4 :h 3}
                                           :md {:x 0 :y 10 :w 4 :h 3}
                                           :sm {:x 0 :y 10 :w 4 :h 3 :static true}}}}
   :spectrum-line-widget      {:layout-opts
                               {:position {:lg {:x 4 :y 10 :w 2 :h 3}
                                           :md {:x 4 :y 10 :w 2 :h 3}
                                           :sm {:x 0 :y 10 :w 2 :h 3 :static true}}}}

   ; row 5
   :map-widget                {:layout-opts
                               {:position {:lg {:x 0 :y 13 :w 2 :h 4}
                                           :md {:x 0 :y 13 :w 2 :h 4}
                                           :sm {:x 0 :y 13 :w 2 :h 4 :static true}}}}
   :heatmap-widget            {:layout-opts
                               {:position {:lg {:x 2 :y 13 :w 4 :h 4}
                                           :md {:x 2 :y 13 :w 4 :h 4}
                                           :sm {:x 2 :y 13 :w 4 :h 4 :static true}}}}

   ; row 6
   :sankey-widget             {:layout-opts
                               {:position {:lg {:x 0 :y 17 :w 3 :h 3}
                                           :md {:x 0 :y 17 :w 3 :h 3}
                                           :sm {:x 0 :y 17 :w 3 :h 3 :static true}}}}
   :dependency-widget         {:layout-opts
                               {:position {:lg {:x 3 :y 17 :w 3 :h 3}
                                           :md {:x 3 :y 17 :w 3 :h 3}
                                           :sm {:x 0 :y 17 :w 3 :h 3 :static true}}}}


   ; row 7
   :usage-side-by-side-widget {:layout-opts
                               {:position {:lg {:x 0 :y 20 :w 4 :h 2}
                                           :md {:x 0 :y 20 :w 4 :h 2}
                                           :sm {:x 0 :y 20 :w 2 :h 2 :static true}}}}

   :spectrum-column-widget    {:layout-opts
                               {:position {:lg {:x 4 :y 20 :w 2 :h 2}
                                           :md {:x 4 :y 20 :w 2 :h 2}
                                           :sm {:x 4 :y 20 :w 2 :h 2 :static true}}}}

   ; row 8
   :scatter-widget            {:layout-opts
                               {:position {:lg {:x 0 :y 21 :w 2 :h 2}
                                           :md {:x 0 :y 21 :w 2 :h 2}
                                           :sm {:x 0 :y 21 :w 2 :h 2 :static true}}}}})



;:time-widget               {:layout-opts
;                            {:position {:lg {:x 4 :y 0 :w 2 :h 1}
;                                        :md {:x 4 :y 0 :w 2 :h 1}
;                                        :sm {:x 0 :y 0 :w 2 :h 1 :static true}}}}
