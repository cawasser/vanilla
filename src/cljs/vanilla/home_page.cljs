(ns vanilla.home-page
  (:require
    [re-frame.core :as rf]

    [vanilla.login :as login]
    [vanilla.version :as ver]
    [vanilla.widgets.configure-widget :as wc]
    [vanilla.add-widget :as add-wid]
    [vanilla.grid :as grid]))



(def width 1536)
(def height 1024)
(def rows 50)


(defn- widgets-grid []
  [grid/Grid {:id          "dashboard-widget-grid"
              :cols        {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}
              :width       width
              :row-height  (/ height rows)
              :breakpoints {:lg 2048 :md 1024 :sm 768 :xs 480 :xxs 0}
              :data        @(rf/subscribe [:widgets])
              :on-change   #();prn (str "layout change. prev " %1 " //// new " %2))
              :item-props  {:class "widget-component"}}])



(defn top-right-buttons
  "Buttons in the top right of the dashboard, which right now is just an add widget button"
  []
  (if (some? @(rf/subscribe [:get-current-user]))
    [:div.level-right.has-text-right
     [add-wid/add-widget-button]]))

(defn home-page
  "This is the start of the UI for our SPA"
  []
  ;[:p "hello"]
  [:div {:width "100%"}
   [:div.container
    [:div.content {:width "100%"}
     [:div.container.level.is-fluid {:width "100%"}
      [:div.level-left.has-text-left
       [wc/change-header (rf/subscribe [:configure-widget])]
       [ver/version-number]]
      [top-right-buttons]]]]
   [widgets-grid]])