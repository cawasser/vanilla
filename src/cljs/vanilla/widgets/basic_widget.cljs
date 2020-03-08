(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cljsjs.react-color]
            [vanilla.widgets.util :as util]
            [ajax.core :as ajax :refer [GET POST]]))



(defn delete-widget
  "delete the selected widget from the server

  *widget-id* (string 'guid') we use a guid as the :key or name of the widget, so we can
  add multiple widgets of the same type, even when they get their data from the same
  data-source. Each widget is unique.

  Both callbacks provide an updated 'layout' of all the widgets, so the UI will be redrawn.
  In the case of a failure, we assume the layout data will not have changed"

  [widget-id]
  ;(prn "removing widget: " widget-id)

  (POST "/delete-widget"
    {:format          (ajax/json-request-format {:keywords? true})
     :response-format (ajax/json-response-format {:keywords? true})
     :params          {:id widget-id}
     :handler         #(rf/dispatch [:layout-message %])
     :error-handler   #(rf/dispatch [:layout-message %])}))


; remove a widget from the app-db
;
; *widget-id* (string 'guid') we use a guid as the :key or name of the widget, so we can
; add multiple widgets of the same type, even when they get their data from the same
; data-source. Each widget is unique.
;
; There is a bug here: we delete the widget form the local app-db EVEN if the server update call FAILS!
;
(rf/reg-event-db
  :remove-widget
  (fn-traced
    [db [_ widget-id]]
    (delete-widget widget-id)
    ; TODO: this looks like a bug - we delete the widget from the app-db even if the server update fails!
    (assoc db :widgets (remove #(= (:key %) widget-id) (:widgets db)))))



(defn debug-style
  "uses the :viz/debug key in widget options to determine if we show a dotted border around the widget

  *options* (map) the map of all the options for the widget, may or may not contain :viz/debug

  The call defaults to 'false' so the border does NOT draw.

  _returns_ (keyword)  :dotted or :none (default). This value is used to make the :border <val> key for drawing"

  [options]
  (if (get options :viz/debug false)
    :dotted
    :none))



(defn basic-widget
  "'wrapper' for all widgets.

  Provides a hiccup structure to get all widgets, regardless of the specific
  content, have the same look, feel, and functionality (drag, change title text, etc.)

  *name* (string 'guid')    we use a guid as the :key or name of the widget, so we can add multiple widgets
  of the same type, even when they get their data from the same data-source. Each widget is unique.

  *data* (unused) remove?

  *options* (map)           map of options for drawing the widget. this call looks for the following keys:

  :viz/height (string)         height of the widget, defaults to '100%', can be '%' or 'px'

  :viz/banner-color (map)      color for the banner/header on the widget, defaults to 'gray' (rgba 150,150,150,1)

  :viz/banner-text-color (map) color for the title of the widget, defaults to 'black' (rgba 0,0,0,1)

  :viz/title (string)          text of the title

  *custom-content* (hiccup)  the specific content for this widgets. This can be any hiccup, or a hiccup producing function. It
  will be embedded as the most 'inside' content of the widget

  _returns_ (hiccup) the content of a complete widget that can be embedded into the UI"

  [name data options custom-content]

  ;(prn "basic-widget " name
  ;  " //// options " options
  ;  " //// custom-content " custom-content)

  (fn []
    [:div.widgets.container
     {:style {:height   (get options :viz/height "100%")
              :width    "100%"
              :overflow "hidden"}}
     [:div.title-wrapper.grid-toolbar.move-cursor {:cursor "move"}
      [:div.level
       {:style {:background-color (util/rgba (get options :viz/banner-color {:r 150 :g 150 :b 150 :a 1}))}}

       [:div.level-left.has-text-left
        [:h3.title.grid-content.menu-cursor
         {:cursor        "context-menu"
          :on-mouse-down #(.stopPropagation %)
          :on-click      #(do
                            ;(prn "showing header for " name)
                            (rf/dispatch-sync [:configure-widget name]))
          :style         {:color (util/rgba (get options :viz/banner-text-color {:r 0 :g 0 :b 0 :a 1}))}}
         (get options :viz/title)]]

       [:div.level-right.has-text-centered
        [:button.delete.is-large {:style         {:margin-right "10px"}
                                  :on-mouse-down #(.stopPropagation %)
                                  :on-click      #(do
                                                    (rf/dispatch [:remove-widget name])
                                                    (.stopPropagation %))}]]]]

     [:div {:class         @(rf/subscribe [:theme])
            :style         {
                            :width        "100%"
                            :height       "90%"
                            ;:marginRight  "50px"
                            :margin       "auto"
                            :marginTop    "5px"
                            :marginBottom "5px"
                            :cursor       :default
                            :border-style (debug-style options)
                            :align-items  :stretch
                            :overflow     "hidden"
                            :display      :flex}
            :on-mouse-down #(.stopPropagation %)}

      custom-content]]))

