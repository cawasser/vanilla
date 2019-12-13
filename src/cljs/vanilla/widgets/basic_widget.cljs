(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-frame.core :as rf]
            [dashboard-clj.widgets.core :as widget-common]))



(defn debug-style [options]
  (if (get-in options [:viz :debug] false)
    :dotted
    :none))



(defn basic-widget [name data options custom-content]

  ;(.log js/console (str "basic-widget " name " of " type))

  [:div {:class "vanilla.widgets.line-chart container"
         :style {:height (get options :viz/height "100%")
                 :width "100%"}}
   [:div {:class "title-wrapper"}
    [:container.level {:style {:background-color
                                      (get options :viz/banner-color "lightblue")}}

     [:div.level-left.has-text-left
      [:h3 {:class "title"
            :style {:color (get options :viz/banner-text-color "black")}}
       (get options :viz/title)]]
     [:div.level-right.has-text-centered
      [:button.delete.is-large {:style {:margin-right "10px"}
                                :on-click #(do
                                             (.log js/console
                                                   (str "Close widget "))
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

    custom-content]])

