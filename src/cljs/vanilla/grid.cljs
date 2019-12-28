(ns vanilla.grid
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [vanilla.events]
    [cljsjs.react-grid-layout]))



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
  (let [[b t] (get colors (rand-int 3))
        n-w (merge-with clojure.set/union
              (assoc-in {} [:data-grid :x] (rand-int x-y-range))
              (assoc-in {} [:data-grid :y] (rand-int x-y-range))
              (assoc-in {} [:data-grid :w] (inc (rand-int w-h-range)))
              (assoc-in {} [:data-grid :h] (inc (rand-int w-h-range)))
              (assoc {} :style {:background-color b
                                :color            t}))]

    (prn "new-widget " n-w)

    n-w))





(defn onLayoutChange [on-change prev new]
  ;; note the need to convert the callbacks from js objects

  (prn "onLayoutChange " on-change
    " //// prev " prev
    " //// new " (js->clj new :keywordize-keys true))

  ; TODO: save the updated positions into the widgets

  (on-change prev (js->clj new :keywordize-keys true)))


(defn GridItem [props data]

  (prn (str "GridItem " props " //// " data))

  [:div
   (merge {:data-grid (:layouts props)}
     {:class "grid-toolbar"})
   [:span.text {:on-click #(do
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

       (prn (str "grid render " widgets
              " //// item-props" item-props
              " //// data " data))

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
                                                 :onLayoutChange  (partial onLayoutChange on-change data)}]
          (mapv #(GridItem % (:key %)) data))])}))
