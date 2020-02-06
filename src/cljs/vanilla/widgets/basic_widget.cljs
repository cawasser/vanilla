(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cljsjs.react-color]
            [vanilla.widgets.util :as util]
            [ajax.core :as ajax :refer [GET POST]]))


(defn delete-widget [widget-id]
  ;(prn "removing widget: " widget-id)

  (POST "/delete-widget"
        {:format          (ajax/json-request-format {:keywords? true})
         :response-format (ajax/json-response-format {:keywords? true})
         :params          {:id widget-id}
         :handler         #(prn "widget removed")
         :on-error        #(prn "ERROR deleting the widget " %)}))

(defn update-widget [widget]
  ;(prn "updating widget: " widget)

  (POST "/update-widget"
        {:format          (ajax/json-request-format {:keywords? true})
         :response-format (ajax/json-response-format {:keywords? true})
         :params          {:widget (clojure.core/pr-str (dissoc widget :build-fn))}
         :handler         #(prn "widget updated")
         :on-error        #(prn "ERROR updating the widget " %)}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; events and helpers
;
;

(defn update-color [id option color widget]
  (if (= id (:key widget))
    (do       ;; SIDE-EFFECT: the next line sends the updated widget to the db
      (update-widget (assoc-in widget [:options option] color))
      (assoc-in widget [:options option] color))
    widget))


(rf/reg-event-db
  :update-color
  (fn-traced [db [_ widget-id option color]]
    (assoc db :widgets (map #(partial (update-color widget-id option color %)) (:widgets db)))))


(rf/reg-event-db
  :remove-widget
  (fn-traced [db [_ widget-id]]
             (delete-widget widget-id)
             (assoc db :widgets (remove #(= (:key %) widget-id) (:widgets db)))))


;
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn rgba [{:keys [r g b a]}]
  (str "rgba(" r "," g "," b "," a ")"))



(defn change-color [is-active id item chosen-color]
  (fn []
    [:div.modal (if @is-active {:class "is-active"})
     [:div.modal-background]
     [:div.modal-content {:on-click #(do
                                       (reset! is-active false)
                                       (.stopPropagation %))}
      [:> js/ReactColor.CompactPicker
       {:style {:top "5px" :left "10px"}
        :color @chosen-color
        :onChangeComplete (fn [color _]
                            (reset! chosen-color (:rgb (js->clj color :keywordize-keys true)))
                            (rf/dispatch-sync [:update-color id item @chosen-color])
                            (reset! is-active false))}]]]))




(defn debug-style [options]
  (if (get-in options [:viz :debug] false)
    :dotted
    :none))



(defn basic-widget [name data options custom-content]

  ;(prn "basic-widget " name
  ;  " //// options " options
  ;  " //// custom-content " custom-content)

  (fn []

    [:div {:class "vanilla.widgets.line-chart container"
           :style {:height (get options :viz/height "100%")
                   :width  "100%"}}
     [:div {:class "title-wrapper"}
      [:container.level {:style    {:background-color (util/rgba (get options :viz/banner-color {:r 150 :g 150 :b 150 :a 1}))}
                         :on-click #(do
                                      ;(prn "showing header for " name)
                                      (rf/dispatch-sync [:configure-widget name]))}

       [:div.level-left.has-text-left
        [:h3 {:class    "title"
              :style    {:color (util/rgba (get options :viz/banner-text-color {:r 0 :g 0 :b 0 :a 1}))}}
         (get options :viz/title)]]

       [:div.level-right.has-text-centered
        [:button.delete.is-large {:style    {:margin-right "10px"}
                                  :on-click #(do
                                               (rf/dispatch [:remove-widget name])
                                               (.stopPropagation %))}]]]]


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

      custom-content]]))

