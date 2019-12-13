(ns vanilla.widgets.map
  (:require [reagent.core :as r :refer [atom]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]))

(defn- render
  []
  [:div {:style {:width "100%" :height "100%"}}])


(defn- show-map [this]
  (let [config {:chart {:map "custom/world-continents"}

                :title {:text ""}

                :subtitle {:text ""}

                :mapNavigation {:enabled true
                                :buttonOptions {:verticalAlign "bottom"}}

                :colorAxis {:min 0}


                :series [{:data [["eu" 0]
                                 ["oc" 1]
                                 ["af" 2]
                                 ["as" 3]
                                 ["na" 4]
                                 ["sa" 5]]
                          ;:map  "custom/world-continents" 
                          :name "Tons produced" 
                          :states {:hover {:color "#BADA55"}}
                          :dataLabels {:enabled true
                                       :format "{point.name}"}}]}]

    (js/Highcharts.mapChart (r/dom-node this)
                            (clj->js config))))

(defn map-container
  [map-options]
  (r/create-class {:reagent-render render
                   :component-did-mount show-map
                   :component-did-update show-map}))

(widget-common/register-widget
  :map-container
  (fn [data options]
    ;(.log js/console ":simple-text" (str data) (str options))

    [basic/basic-widget :map-container data options

     [:div {:style {:width "100%" :height "100%"}}
      [map-container]]]))
