(ns vanilla.widgets.make-chart
  (:require [reagent.core :as reagent]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]))



;;;;;;;;;;;;;;;;;
;
; PRIVATE support functions
;
;
(defn- make-config
  "combine the various config items into the one 'master' config
   for all of Highcharts, since it has only 1 function:

         Highcharts.Chart()

   NOTE: this function will get more sophisticated as we flesh out the
         handling of :data-format/xxx and :src/xxx metadata keys"

  [chart-config data options]

  (.log js/console (str "make-config " options))

  (let [chart-type  (keyword (-> chart-config :chart :type))
        data-config (if (instance? Atom data) @data data)
        base-config {:title       {:text  (get options :viz/chart-title "")
                                   :style {:labels {:fontFamily "monospace"
                                                    :color      "#FFFFFF"}}}

                     :subtitle    {:text ""}

                     :xAxis       {:title      {:text (:src/x-title data-config "")}
                                   :allowDecimals (get-in options [:viz/x-allowDecimals] false)}

                     :tooltip     {:valueSuffix (:src/y-valueSuffix data-config "")}

                     :yAxis       {:title {:text (:src/y-title data-config "")}
                                   :allowDecimals (get-in options [:viz/y-allowDecimals] false)}

                     :plotOptions {:series    {:animation (:viz/animation options false)}
                                   chart-type {:dataLabels
                                               {:enabled (:viz/dataLabels options false)}
                                               :lineWidth (:viz/lineWidth options 1)}
                                   :keys (:src/keys options [])}
                     :credits     {:enabled false}}

        special-config (if (:src/x-categories options)
                         (assoc-in base-config [:xAxis :categories] (:src/x-categories options))
                         base-config)]

    (merge-with clojure.set/union special-config chart-config)))


(defn- merge-configs
  "merge the data into the :series key whenever it change so
   we get the new data to re-render"

  [chart-config data]

  (let [ret (assoc chart-config :series (get-in (if (instance? Atom data) @data data)
                                                [:data :series] []))]

    (.log js/console (str "merge-configs <<<<< " data))
    (.log js/console (str "merge-configs " chart-config " >>>>> " ret))

    ret))




;;;;;;;;;;;;;;;;;
;
; PUBLIC interface
;
;
(defn make-chart
  "creates the correct reagent 'hiccup' and react/class to implement a
  Highcharts.js UI component that can be embedded inside any valid hiccup"

  [chart-config data local-config]

  (let [dom-node (reagent/atom nil)]

    (reagent/create-class
      {:reagent-render
       (fn [args]
         @dom-node                                          ; be sure to render if node changes
         [:div {:style {:width "100%" :height "100%"}}])

       :component-did-mount
       (fn [this]
         (let [node (reagent/dom-node this)]
           ;(.log js/console (str (-> chart-config :chart :type)
           ;                   " component-did-mount"))
           (reset! dom-node node)))

       :component-did-update
       (fn [this old-argv]
         (let [new-args    (rest (reagent/argv this))
               new-data    (js->clj (second new-args))
               base-config (make-config chart-config data local-config)
               all-configs (merge-configs base-config new-data)]

           ;(.log js/console (str (-> chart-config :chart :type)
           ;                      " component-did-update "
           ;                      all-configs))
           ;(.log js/console (str (-> chart-config :chart :type)
           ;                      " PROPS "
           ;                      (reagent/props this)))
           ;(.log js/console (str (-> chart-config :chart :type)
           ;                      " NEW-ARGS "
           ;                      new-args))
           ;(.log js/console (str (-> chart-config :chart :type)
           ;                      " NEWDATA "
           ;                      (if (instance? Atom new-data)
           ;                        @new-data
           ;                        new-data)))

           (js/Highcharts.Chart. (reagent/dom-node this)
                                 (clj->js all-configs))))})))

;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;

(comment

  ())