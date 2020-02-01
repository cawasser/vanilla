(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cljsjs.react-color]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; events and helpers
;
;

(defn update-color [id option color widget]
  (if (= id (:key widget))
    (assoc-in widget [:options option] color)
    widget))

(defn update-title [id option title widget]
  (if (= id (:key widget))
    (assoc-in widget [:options option] title)
    widget))

(rf/reg-event-db
  :update-color
  (fn-traced [db [_ widget-id option color]]
    (assoc db :widgets (map #(partial (update-color widget-id option color %)) (:widgets db)))))

(rf/reg-event-db
  :update-title
  (fn-traced [db [_ widget-id option title]]
             (assoc db :widgets (map #(partial (update-title widget-id option title %)) (:widgets db)))))


;
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn rgba [{:keys [r g b a]}]
  (str "rgba(" r "," g "," b "," a ")"))

(defn input-field [tag type data]
  [:div.field
   [tag
    {:type      type
     :value     @data
     :on-click #(.stopPropagation %)
     :on-change #(do
                   (prn "new title " %)
                   (reset! data %))}]])



(def title-data (atom "widget-title"))

(defn change-header [is-active id color-item chosen-color]
  (fn []
    [:div.modal (if (= "header" @is-active) {:class "is-active"})
     [:div.modal-background]
     [:div.modal-card
      [:header.modal-card-head
       [:p.modal-card-title "Change Banner Color"]]


      [:> js/ReactColor.CompactPicker
       {:style            {:top "5px" :left "10px"}
        :color            @chosen-color
        :onChangeComplete (fn [color _]
                            (reset! chosen-color (:rgb (js->clj color :keywordize-keys true))))}]

      [:footer.modal-card-foot
       [:button.button.is-success {:on-click #(do
                                                ;(prn "picked widget " @chosen-widget @selected)
                                                (rf/dispatch-sync [:update-color id color-item @chosen-color])
                                                (reset! is-active "")
                                                (.stopPropagation %))} "OK"]
       [:button.button {:on-click #(do
                                     (prn "inactive header")
                                     (reset! is-active "")
                                     (.stopPropagation %))} "Cancel"]]]]))


(defn change-title [is-active id color-item chosen-color title-item widget-title]
  (fn []
    [:div.modal (if (= "title" @is-active) {:class "is-active"})
     [:div.modal-background]

     [:div.modal-card

      [:header.modal-card-head
       [:p.modal-card-title "Change Tile and Text Color"]]

      [:div.field
       [:input.input
        {:type      :text
         :value     @widget-title
         :on-change #(do
                       (prn "new title " (-> % .-target .-value))
                       (reset! widget-title (-> % .-target .-value)))}]]

      [:> js/ReactColor.CompactPicker
       {:style            {:top "5px" :left "10px"}
        :color            @chosen-color
        :onChangeComplete (fn [color _]
                            (reset! chosen-color (:rgb (js->clj color :keywordize-keys true))))}]

      [:footer.modal-card-foot
       [:button.button.is-success {:on-click #(do
                                                ;(prn "picked widget " @chosen-widget @selected)
                                                (rf/dispatch-sync [:update-color id color-item @chosen-color])
                                                (rf/dispatch-sync [:update-title id title-item @widget-title])
                                                (reset! is-active "")
                                                (.stopPropagation %))} "OK"]
       [:button.button {:on-click #(do
                                     (prn "inactive title")
                                     (reset! is-active "")
                                     (.stopPropagation %))} "Cancel"]]]]))



(defn debug-style [options]
  (if (get-in options [:viz :debug] false)
    :dotted
    :none))



(defn basic-widget [name data options custom-content]

  (prn "basic-widget " name
    " //// options " options
    " //// custom-content " custom-content)

  (let [show-picker  (r/atom "")
        header-color       (r/atom (get options :viz/banner-color {:r 150 :g 150 :b 150 :a 1}))
        title-color        (r/atom (get options :viz/banner-text-color {:r 0 :g 0 :b 0 :a 1}))
        title              (r/atom (get options :viz/title "dummy title"))]

    (fn []

      [:div {:class "vanilla.widgets.line-chart container"
             :style {:height (get options :viz/height "100%")
                     :width  "100%"}}
       [:div {:class "title-wrapper"}
        [:container.level {:style    {:background-color (rgba @header-color)}
                           :on-click #(do
                                        (if (= "" @show-picker)
                                          (do
                                            (prn "showing header")
                                            (reset! show-picker "header")))
                                        )}

         [change-header show-picker name :viz/banner-color header-color]
         [change-title show-picker name :viz/banner-text-color title-color :viz/title title]

         [:div.level-left.has-text-left
          [:h3 {:class    "title"
                :style    {:color (rgba @title-color)}
                :on-click #(do
                             (if (= "" @show-picker)
                               (do
                                 (prn "showing title")
                                 (reset! show-picker "title")))
                             (.stopPropagation %))}
           (get options :viz/title)]]

         [:div.level-right.has-text-centered
          [:button.delete.is-large {:style    {:margin-right "10px"}
                                    :on-click #(rf/dispatch [:remove-widget name])}]]]]


       [:div {:class         (str (get options :viz/style-name "widget"))
              :style         {:width        "100%"
                              :height       "80%"
                              :marginRight  "50px"
                              :marginTop    "5px"
                              :cursor       :default
                              :border-style (debug-style options)
                              :align-items  :stretch
                              :display      :flex}
              :on-mouse-down #(.stopPropagation %)}

        custom-content]])))

