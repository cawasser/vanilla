(ns vanilla.grid
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [vanilla.events]
    [cljsjs.react-grid-layout]
    [vanilla.widgets.core :as widget]))



(def colors [["purple" "white"] ["yellow" "black"] ["blue" "white"]])
(def x-y-range 5)
(def w-h-range 4)

(def widgets [{:key 1 :data-grid {:x 0 :y 0 :w 3 :h 3} :style {:background-color "green"
                                                               :color            "white"}}
              {:key 2 :data-grid {:x 0 :y 3 :w 3 :h 3} :style {:background-color "gray"
                                                               :color            "white"}}
              {:key 3 :data-grid {:x 2 :y 0 :w 3 :h 3} :style {:background-color "lightsalmon"}}])

(def new-layout {:data-grid {:x 0 :y 0 :w 4 :h 4} :style {:background-color "purple"
                                                          :color            "white"}})



(defn new-widget []
  (let [[b t] (get colors (rand-int 3))]

    {:data-grid   {:x 0 :y 4 :w 5 :h 9}
     :basis       :chart
     :type        :area-chart
     :data-source :spectrum-traces
     :options     {:viz/title             "Channels (area)"
                   :viz/allowDecimals     false
                   :viz/x-title           "frequency"
                   :viz/y-title           "power"
                   :viz/banner-color      b
                   :viz/banner-text-color t
                   :viz/style-name        "widget"
                   :viz/animation         false
                   :viz/tooltip           {:followPointer true}}
     :layout-opts {:position {:lg {:x 0 :y 4 :w 2 :h 3}
                              :md {:x 0 :y 2 :w 2 :h 3}
                              :sm {:x 0 :y 0 :w 1 :h 2 :static true}}}}))



(defn onLayoutChange [on-change prev new]
  ;; note the need to convert the callbacks from js objects

  ;(prn "onLayoutChange " on-change
  ;  " //// prev " prev
  ;  " //// new " (js->clj new :keywordize-keys true))

  (rf/dispatch [:update-layout (js->clj new :keywordize-keys true)])

  (on-change prev (js->clj new :keywordize-keys true)))



(defn widget-wrapper [props data]

  ;(prn "widget-wrapper..." props
  ;  " //// data " data)

  (let [content (widget/setup-widget props)]

    ;(prn (str "widget-wrapper " props
    ;       " //// data " data
    ;       " //// content " content))

    [:div
     (merge props {:class "widget grid-toolbar"
                   :style {:background-color (get-in props [:options :viz/banner-color] "yellow")
                           :color            (get-in props [:options :viz/banner-text-color] "black")}})
     content]))



(defn GridItem [props data]

  (prn (str "GridItem " props " //// " data))

  [:div
   (merge props
     {:class "grid-toolbar"})
   [:span.text {:on-click      #(do
                                  (prn (str "remove-widget " data))
                                  (rf/dispatch [:remove-widget data]))
                :on-mouse-down #(prn "on-mouse-down")}
    data]])




(defn Grid
  [args]

  (r/create-class
    ;; probably dont need this..
    {:should-component-update
     (fn [this [_ old-props] [_ new-props]]
       ;; just re-render when data changes and width changes
       (or (not= (:width old-props) (:width new-props))
         (not= (:data old-props) (:data new-props))))



     :reagent-render
     (fn [{:keys [id data width row-height cols breakpoints item-props on-change empty-text] :as props}]

       ;(prn (str "grid render args " args
       ;       " //// props " props
       ;       " //// item-props" item-props
       ;       " //// data " data))

       [:div.grid-container
        (into [:> js/ReactGridLayout.Responsive {:id              id
                                                 :class           "layout"
                                                 :cols            cols
                                                 :width           width
                                                 :rowHeight       row-height
                                                 :isDraggable     true
                                                 :isResizable     true
                                                 :draggableHandle ".grid-toolbar"
                                                 :draggableCancel ".grid-content"
                                                 :breakpoints     breakpoints
                                                 ;:onResize        (partial onLayoutChange on-change data)
                                                 ;:onDrag          (partial onLayoutChange on-change data)
                                                 :onLayoutChange  (partial onLayoutChange on-change data)}]
          ;(mapv #(GridItem % (:key %)) data)
          (mapv #(widget-wrapper % (:key %)) data))])}))
