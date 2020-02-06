(ns vanilla.widgets.basic-widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cljsjs.react-color]))




(defn debug-style [options]
  (if (get-in options [:viz :debug] false)
    :dotted
    :none))

(defn- rgba [{:keys [r g b a]}]
  (str "rgba(" r "," g "," b "," a ")"))



(defn basic-widget [name data options custom-content]

  (prn "basic-widget " name
    " //// options " options
    " //// custom-content " custom-content)

  (fn []

    [:div {:class "vanilla.widgets.line-chart container"
           :style {:height (get options :viz/height "100%")
                   :width  "100%"}}
     [:div {:class "title-wrapper"}
      [:container.level {:style    {:background-color (rgba (get options :viz/banner-color {:r 150 :g 150 :b 150 :a 1}))}
                         :on-click #(do
                                      (prn "showing header for " name)
                                      (rf/dispatch-sync [:configure-widget name]))}

       [:div.level-left.has-text-left
        [:h3 {:class    "title"
              :style    {:color (rgba (get options :viz/banner-text-color {:r 0 :g 0 :b 0 :a 1}))}}
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

