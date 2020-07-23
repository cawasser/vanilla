(ns vanilla.data-sidebar.main-page
  (:require [vanilla.grid :as grid]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [vanilla.add-widget :as add-wid]
            [vanilla.widgets.configure-widget :as wc]
            [vanilla.login :as login]
            [vanilla.widgets.util :as util]
            [react-beautiful-dnd :refer [Draggable Droppable DragDropContext]]))



(def width 1536)
(def height 1024)
(def rows 50)



(defn sidebar []
  [:nav.panel.is-primary
   [:p.panel-heading "Data Sources"]
   [:div.panel-block
    [:p.control.has-icons-left
     [:input.input {:type      "text" :placeholder "Search"
                    :on-change #()}]
     [:span.icon.is-left
      [:i.fas.fa-search {:aria-hidden "true"}]]]]
   ;[:p.panel-tabs
   ; [:a.is-active "All"]
   ; [:a "Public"]
   ; [:a "Private"]
   ; [:a "Sources"]
   ; [:a "Forks"]]
   [:a.panel-block.is-active
    [:button.is-dark.Draggable "spectrum"]]
   [:a.panel-block
    [:button.is-dark.Draggable "signal path"]]])




(defn- widgets-grid []
  [grid/Grid {:id          "dashboard-widget-grid"
              :class       "Droppable"
              :cols        {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}
              :width       width
              :row-height  (/ height rows)
              :breakpoints {:lg 2048 :md 1024 :sm 768 :xs 480 :xxs 0}
              :data        @(rf/subscribe [:widgets])
              :on-change   #()                              ;prn (str "layout change. prev " %1 " //// new " %2))
              :item-props  {:class "widget-component"}}])


(defn top-right-buttons
  "Determine whether buttons in the top right of the dashboard show either:
  - A login button
  - An add widget button alongside a logout button"
  []
  (if (some? @(rf/subscribe [:get-current-user]))
    [:div.level-right.has-text-right
     ;[add-wid/add-widget-button]
     [login/logout-button]]
    [:div.level-right.has-text-right
     [login/login-button]]))



(defn home-page
  "This is the start of the UI for our SPA"
  []

  (rf/dispatch [:add-widget :line-chart :spectrum-traces])

  [:div {:width "100%"}
   [:div.container
    [:div.content {:width "100%"}
     [:div.container.level.is-fluid {:width "100%"}
      [:div.level-left.has-text-left
       [wc/change-header (rf/subscribe [:configure-widget])]
       [add-wid/version-number]
       [add-wid/current-user]]
      [top-right-buttons]]]]
   (if @(rf/subscribe [:get-current-user])
     [:div.columns.DragDropContext
      [:div.column.is-one-fifth
       [sidebar]]
      [:div.column
       [widgets-grid]]])])
