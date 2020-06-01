(ns vanilla.widgets.core
  (:require [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.make-map :as mm]))

(defn make-widget [name id chart-config]

  ;(prn "make-widget " name "of type " id ", " chart-config)

  (fn [data options]

    ;(prn "in widget " id " / " name
    ;  ;" //// data " data
    ;  " //// options " options
    ;  " //// chart-config " chart-config)

    [basic/basic-widget name data options
     [:div {:style {:width "100%" :height "100%" :display :flex}}
      [mc/make-chart chart-config data options]]]))

(defn make-map-widget [name id chart-config]

  ;(prn "make-widget " name "of type " id ", " chart-config)

  (fn [data options]

    ;(prn "in widget " id " / " name
    ;  " //// data " data
    ;  " //// options " options
    ;  " //// chart-config " chart-config)

    [basic/basic-widget name data options
     [:div {:style {:width "100%" :height "100%"  :display :flex}}
      [mm/make-chart chart-config data options]]]))



(defn make-simple-widget [name type]

  (let [widget @(rf/subscribe [:widget-type type])
        build-fn (:build-fn widget)]

    ;(prn "make-simple-widget " name "of type " type
    ;  " //// widget " widget
    ;  " //// build-fn " build-fn)

    (fn [data options]

      [basic/basic-widget name data options
       ;[:div.container
        (build-fn name data options)])))



(defn build-widget [{:keys [key basis type chart-types] :as widget}]

  (let [chart-config @(rf/subscribe [:hc-type type])]

    (prn "building widget " key " of " type
      " //// " basis "/" chart-types
      " //// chart-config " chart-config
      " //// widget " widget)


    (condp = basis
      :chart (make-widget key type chart-config)

      :simple (make-simple-widget key type)

      :map (make-map-widget key type chart-config))))


(defn setup-widget [{:keys [key data-source type options] :as props}]

  ;(prn "setup-widget " key "/" type
  ;                   " //// data-source " data-source
  ;                   " //// options " options
  ;                   " //// props " props)

  (if data-source
    (let [data (rf/subscribe [:app-db data-source])]

      ;(prn "attaching data " data-source
      ;  " //// data "@data)

      [(build-widget props) @data options])

    ((build-widget props) {} options)))
