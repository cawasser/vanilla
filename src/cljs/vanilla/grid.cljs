(ns vanilla.grid
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [vanilla.events]
    [cljsjs.react-grid-layout]
    [vanilla.widgets.core :as widget]
    [clojure.edn :as edn]
    [ajax.core :as ajax :refer [GET POST]]))



;(defn save-layout [layout]
;  (prn "saving layout: " (clojure.core/pr-str layout))
;
;  (POST "/save-layout"
;    {:format          (ajax/json-request-format {:keywords? true})
;     :response-format (ajax/json-response-format {:keywords? true})
;     :params          {:widgets (clojure.core/pr-str layout)}       ;convert the whole layout struct to a string to preserve values
;     :handler         #(prn "Layout SAVED!")
;     :on-error        #(prn "ERROR saving the layout " %)}))

(defn fixup-new-widget [widget]
  (merge widget {:data-grid {:x 0 :y 0 :w 5 :h 15}}))



(defn onLayoutChange [on-change prev new]
  ;; note the need to convert the callbacks from js objects

  (let [chg (js->clj new :keywordize-keys true)
        fst (first chg)]
    (if (and
          (not (empty? chg))
          (<= 1 (count chg))
          (not= (:i fst) "null"))
      (do
        (prn "onLayoutChange " on-change
          " //// prev " prev
          " //// new " new
          " //// empty? " (empty? chg)
          " //// count " (count chg)
          " //// first id " (:i fst))

        (rf/dispatch [:update-layout (js->clj new :keywordize-keys true)])
        ;(save-layout (js->clj prev :keywordize-keys true))
        ))


    (on-change prev (js->clj new :keywordize-keys true))))



(defn widget-wrapper [props data]

  ;(prn "widget-wrapper..." props
  ;  " //// data " data)

  (let [content (widget/setup-widget props)
        ret     [:div
                 (merge props {:class "widget grid-toolbar"
                               :style {:background-color (get-in props [:options :viz/banner-color] "yellow")
                                       :color            (get-in props [:options :viz/banner-text-color] "black")}})
                 content]]

    ;(prn "widget-wrapper "
    ;  " //// props " props
    ;  " //// data " data
    ;  " //// content " content
    ;  " //// ret " ret)

    ret))






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
                                                 :onLayoutChange  (partial onLayoutChange on-change data)}]
          (mapv #(widget-wrapper % (:key %)) data))])}))
