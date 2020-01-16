(ns vanilla.widgets.core
  (:require [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.make-chart :as mc]))

(def widget-store (atom {}))

; TODO - is having a widget registry even worth doing?

(defn register-widget [name w]
  (swap! widget-store assoc name w))

;(prn "register-widget " type
;  " //// w " w))



(defn make-widget [name id chart-config]

  ;(prn "make-widget " name "of type " id ", " chart-config)

  (fn [data options]

    ;(prn "in widget " id " / " name
    ;  ;" //// data " data
    ;  " //// options " options
    ;  " //// chart-config " chart-config)

    [basic/basic-widget name data options
     [:div {:style {:width "95%" :height "100%"}}
      [mc/make-chart chart-config data options]]]))



;(defn make-stacked-widget [id [top-chart bottom-chart]]
;
;  ;(prn (str "make-stacked-widget " id
;  ;                      "/" top-chart "/" bottom-chart))
;
;    (fn [data options]
;
;      ;(prn (str "composing stacked-chart " id
;      ;                      " //// (data)" data
;      ;                      " //// (options)" options))
;
;      [basic/basic-widget data options
;
;       [:div {:style {:width "95%" :height "65%"}}
;        [mc/make-chart (get-config top-chart) data options]
;
;        [:div {:style {:width "100%" :height "65%"}}
;         [mc/make-chart (get-config bottom-chart) data options]]]])))
;
;
;(defn make-side-by-side-widget [id [left-chart right-chart]]
;
;  ;(prn (str "make-side-by-side-widget " id
;  ;                      "/" left-chart "/" right-chart))
;
;    (fn [data options]
;
;      ;(prn (str "side-by-side-chart " id "/" left-chart "/" right-chart
;      ;                      " //// (data)" data
;      ;                      " //// (options)" options))
;
;      [basic/basic-widget data options
;
;       [:div.columns {:style {:height "100%" :width "100%" :marginTop "10px"}}
;
;        [:div.column.is-two-thirds {:style {:height "80%"}}
;         [mc/make-chart (get-config left-chart) data options]]
;
;        [:div.column.is-one-third {:style {:height "80%"}}
;         [mc/make-chart (get-config right-chart) data options]]]])))
;



(defn build-widget [{:keys [key basis type chart-types] :as widget}]

  (let [chart-config @(rf/subscribe [:hc-type type])]

    ;(prn "building widget " key " of " type
    ;  " //// " basis "/" chart-types
    ;  " //// chart-config " chart-config
    ;  " //// widget " widget)


    (condp = basis
      :chart (make-widget key type chart-config)

      ;:stacked-chart (do
      ;                 ;(prn (str "calling make-stacked-widget " type
      ;                 ;                      "/" chart-types))
      ;                 (make-stacked-widget type chart-types))
      ;
      ;:side-by-side-chart (do
      ;                      ;(prn (str "calling make-side-by-side-widget " type
      ;                      ;                      "/" chart-types))
      ;                      (make-side-by-side-widget type chart-types))

      ())))



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
