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

(rf/reg-event-db
  :chosen-bg-color
  (fn-traced [db [_ color]]
    (assoc db :chosen-bg-color color)))

(rf/reg-event-db
  :chosen-txt-color
  (fn-traced [db [_ color]]
    (assoc db :chosen-txt-color color)))



(rf/reg-sub
  :chosen-bg-color
  (fn [db _]
    (:chosen-bg-color db)))

(rf/reg-sub
  :chosen-txt-color
  (fn [db _]
    (:chosen-txt-color db)))

;
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn rgba [{:keys [r g b a]}]
  (str "rgba(" r "," g "," b "," a ")"))



(defn change-header-color [is-active chosen-color orig-color]

  (fn []
    [:div.modal (if @is-active {:class "is-active"})
     [:div.modal-background]
     [:div.modal-card
      [:section.modal-card-body {:on-click #(do
                                              (reset! is-active false)
                                              (prn "show-header-picker " @is-active)
                                              (.stopPropagation %))}

       [:> js/ReactColor.CompactPicker
        {:color @chosen-color
         :onChangeComplete (fn [color evt]
                             (reset! chosen-color (:rgb (js->clj color :keywordize-keys true)))
                             (reset! is-active false)

                             (prn "new color! " (:rgb (js->clj color :keywordize-keys true))
                               " //// " @chosen-color))}]]]]))





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

      (prn "basic-widget " name
        " //// chosen-color " (rgba @chosen-color))

      [:div {:class "vanilla.widgets.line-chart container"
             :style {:height (get options :viz/height "100%")
                     :width "100%"}}
       [:div {:class "title-wrapper"}
        [:container.level {:style {:background-color (rgba @chosen-color)}
                           :on-click #(do
                                        (swap! show-header-picker not)
                                        (prn "show-header-picker " @show-header-picker)
                                        (.stopPropagation %))}


         [change-header-color show-header-picker chosen-color {:r 150 :g 150 :b 150 :a 1}]

         [:div.level-left.has-text-left
          [:h3 {:class "title"
                :style {:color (get options :viz/banner-text-color "black")}
                :on-click #(do
                             (change-title-color)
                             (.stopPropagation %))}
           (get options :viz/title)]]

         [:div.level-right.has-text-centered
          [:button.delete.is-large {:style {:margin-right "10px"}
                                     :on-click #(do
                                                  ;(.log js/console
                                                  ;      (str "Close widget " name))
                                                  (rf/dispatch [:remove-widget name]))}]]]]; name))}]]]]


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

