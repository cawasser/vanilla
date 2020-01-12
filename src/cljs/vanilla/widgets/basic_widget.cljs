(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cljsjs.react-color]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; events and subscriptions
;
;

(defn update-header-color [id color widget]
  (prn "update-header-color " id color widget)
  (if (= id (:key widget))
    (assoc-in widget [:options :viz/banner-color] color)
    widget))


(rf/reg-event-db
  :update-header-color
  (fn-traced [db [_ widget-id color]]
    (assoc db :widgets (map #(partial (update-header-color widget-id color %)) (:widgets db)))))


(rf/reg-event-db
  :update-title-color
  (fn-traced [db [_ widget-id color]]
    (assoc-in db [widget-id :options :viz/banner-text-color] color)))



;
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn rgba [{:keys [r g b a]}]
  (str "rgba(" r "," g "," b "," a ")"))



(defn change-header-color [is-active id chosen-color]
  (fn []
    [:div.modal (if @is-active {:class "is-active"})
     [:div.modal-background]
     [:div.modal-card
      [:section.modal-card-body {:on-click #(do
                                              (reset! is-active false)
                                              (.stopPropagation %))}
       [:> js/ReactColor.CompactPicker
        {:color @chosen-color
         :onChangeComplete (fn [color _]
                             (reset! chosen-color (:rgb (js->clj color :keywordize-keys true)))
                             (rf/dispatch-sync [:update-header-color id @chosen-color])
                             (reset! is-active false))}]]]]))





(defn change-title-color []
  (prn "change-title-color!"))





(defn debug-style [options]
  (if (get-in options [:viz :debug] false)
    :dotted
    :none))



(defn basic-widget [name data options custom-content]

  ;(.log js/console (str "basic-widget " options
  ;                   " //// custom-content " custom-content
  ;                   " //// name " name))

  (let [show-title-picker (r/atom false)
        show-header-picker (r/atom false)
        chosen-color (r/atom (get options :viz/banner-color {:r 150 :g 150 :b 150 :a 1}))]

    (fn []

      [:div {:class "vanilla.widgets.line-chart container"
             :style {:height (get options :viz/height "100%")
                     :width "100%"}}
       [:div {:class "title-wrapper"}
        [:container.level {:style {:background-color (rgba @chosen-color)}
                           :on-click #(do
                                        (swap! show-header-picker not)
                                        (.stopPropagation %))}

         [change-header-color show-header-picker name chosen-color]

         [:div.level-left.has-text-left
          [:h3 {:class "title"
                :style {:color (get options :viz/banner-text-color "black")}
                :on-click #(do
                             (change-title-color)
                             (.stopPropagation %))}
           (get options :viz/title)]]

         [:div.level-right.has-text-centered
          [:button.delete.is-large {:style {:margin-right "10px"}
                                     :on-click #(rf/dispatch [:remove-widget name])}]]]]


       [:div {:class (str (get options :viz/style-name "widget"))
              :style {:width "100%"
                      :height "80%"
                      :marginRight "50px"
                      :marginTop "5px"
                      :cursor :default
                      :border-style (debug-style options)
                      :align-items :stretch
                      :display :flex}
              :on-mouse-down #(.stopPropagation %)}

        custom-content]])))

