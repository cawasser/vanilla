(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [vanilla.widgets.util :as util]
            [ajax.core :as ajax :refer [GET POST]]))


;;;;
;; This namespace is in charge of:
;;  - standardized widget hiccup elements
;;  -



(defn delete-widget
  "Make an AJAX call to delete the widget based on its ID"
  [widget-id]
  ;(prn "removing widget: " widget-id)
  (POST "/delete-widget"
    {:format          (ajax/json-request-format {:keywords? true})
     :response-format (ajax/json-response-format {:keywords? true})
     :params          {:id widget-id}
     :handler         #(rf/dispatch [:layout-message %])
     :error-handler   #(rf/dispatch [:layout-message %])}))


(defn- get-data-source
  "Helper function to pull the data sources out of all the widgets.
  'all widgets' are determined by their key value, since they are unique"
  [widgets key]
  (:data-source (->> widgets
                     (filter #(= (:key %) key))
                     first)))

(rf/reg-event-db
  :remove-widget
  (fn-traced [db [_ widget-id]]
    (delete-widget widget-id)
    (assoc db :widgets (remove #(= (:key %) widget-id) (:widgets db))
              :data-sources (dissoc (:data-sources db) (get-data-source @(rf/subscribe [:active-widgets-by-user]) widget-id)))))




; @TODO - what does this pertain to specifically? - what does each do?
(defn debug-style
  "What?"
  [options]
  (if (get-in options [:viz :debug] false)
    :dotted
    :none))


(defn- widget-title-bar
  "Creates the title bar for the widgets and any interaction with the bar.
   The options change the banner color, text, etc."
  [name options]
  [:div.widget-banner.title-wrapper.grid-toolbar.move-cursor
   {:cursor "move"
    :style  {:width            "auto"
             :background-color (util/rgba (get options :viz/banner-color {:r 150 :g 150 :b 150 :a 1}))}}

   [:div.container.level {:style {:width "auto"}}
    [:div.level-left.has-text-left
     [:h3.title.grid-content.menu-cursor
      {:cursor        "context-menu"
       :on-mouse-down #(.stopPropagation %)
       :on-click      #(do
                         (prn "showing header for " name)
                         (rf/dispatch-sync [:configure-widget name]))
       :style         {:color (util/rgba (get options :viz/banner-text-color {:r 0 :g 0 :b 0 :a 1}))}}
      (get options :viz/title)]]

    [:div.level-right.has-text-centered
     [:button.delete.is-large {:style         {:margin-right "10px"}
                               :on-mouse-down #(.stopPropagation %)
                               :on-click      #(do
                                                 (rf/dispatch [:remove-widget name])
                                                 (.stopPropagation %))}]]]])

;@TODO - what does data do?
(defn basic-widget
  "In charge of basic hiccup components of the widget, such as the title and
  widget frame. The custom-content is what makes each widget unique, showing different data.
   - Name    = Name of widget, as displayed in title bar
   - Data    =
   - Options = some of the viz element options for the widget
   - Custom-content = The actual content of the widget"
  [name data options custom-content]

  ;(prn "basic-widget " name
  ;  " //// options " options
  ;  " //// custom-content " custom-content)

  ;(fn []
  ;
  [:div.widget-parent {:style {:height (get options :viz/height "100%")
                               :width  (get options :viz/width "100%")}}
   (widget-title-bar name options)

   [:div.widget.widget-content
    {:style         {:width        "100%"
                     :height       "100%"
                     ;:marginRight  "50px"
                     ;:marginTop    "5px"
                     :cursor       :default
                     :border-style (debug-style options)
                     :align-items  :stretch
                     :display      :flex}
     :on-mouse-down #(.stopPropagation %)}

    custom-content]])

