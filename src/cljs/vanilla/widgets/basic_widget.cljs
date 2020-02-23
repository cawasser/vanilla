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
     :handler         #(rf/dispatch [:layout-message %])
     :error-handler   #(rf/dispatch [:layout-message %])}))



(rf/reg-event-db
  :remove-widget
  (fn-traced [db [_ widget-id]]
    (delete-widget widget-id)
    (assoc db :widgets (remove #(= (:key %) widget-id) (:widgets db)))))





(defn debug-style [options]
  (if (get-in options [:viz :debug] false)
    :dotted
    :none))



(defn basic-widget [name options custom-content]

  ;(prn "basic-widget " name
  ;  " //// options " options)

  (let [ret [:div {:style {:height (get options :viz/height "100%")
                           :width  "100%"}}
             [:div.title-wrapper.grid-toolbar
              [:container.level {:style {:background-color (util/rgba (get options :viz/banner-color {:r 150 :g 150 :b 150 :a 1}))}}


               [:div.level-left.has-text-left
                [:h3.title.grid-content
                 {:style         {:color (util/rgba (get options :viz/banner-text-color {:r 0 :g 0 :b 0 :a 1}))}
                  :on-mouse-down #(.stopPropagation %)
                  :isDraggable   false
                  :on-click      #(do
                                    ;(prn "showing header for " name)
                                    (rf/dispatch-sync [:configure-widget name]))}
                 (get options :viz/title)]]

               [:div.level-right.has-text-centered
                [:button.delete.is-large {:style         {:margin-right "10px"}
                                          :on-mouse-down #(.stopPropagation %)
                                          :on-click      #(do
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

              custom-content]]]

    ;(prn "basic-widget " name
    ;  " //// ret " ret)
    ;" //// options " options
    ;  " //// custom-content " custom-content)

    ret))

