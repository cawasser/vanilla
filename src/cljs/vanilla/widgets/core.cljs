(ns vanilla.widgets.core
  (:require [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.make-map :as mm]))

;(defn make-widget [name id chart-config data options]
;
;  ;(prn "make-widget " name "of type " id ", " chart-config)
;
;  ;(fn [data options]
;
;    ;(prn "in widget " id " / " name
;    ;  ;" //// data " data
;    ;  " //// options " options
;    ;  " //// chart-config " chart-config)
;
;    [basic/basic-widget name options
;     [:div {:style {:width "95%" :height "100%"}}
;      [mc/make-chart chart-config @data options]]])
;
;(defn make-map-widget [name id chart-config data options]
;
;  ;(prn "make-widget " name "of type " id ", " chart-config)
;
;  ;(fn [data options]
;
;    ;(prn "in widget " id " / " name
;    ;  " //// data " data
;    ;  " //// options " options
;    ;  " //// chart-config " chart-config)
;
;    [basic/basic-widget name options
;     [:div {:style {:width "95%" :height "100%"}}
;      [mm/make-chart chart-config @data options]]])
;
;
;
;(defn make-simple-widget [name type data options]
;
;  (let [widget @(rf/subscribe [:widget-type type])
;        build-fn (:build-fn widget)]
;
;    (prn "make-simple-widget " name "of type " type
;      " //// widget " widget
;      " //// data " @data
;      " //// build-fn " build-fn
;
;    ;(fn [data options]
;
;      [basic/basic-widget name options
;       [:div.container
;        (build-fn name @data options)]])))
;
;
;
;(defn build-widget [{:keys [key basis type chart-types] :as widget} data options]
;
;  (let [chart-config @(rf/subscribe [:hc-type type])]
;
;    (prn "build-widget " key " of " type
;      " //// " basis "/" chart-types
;      " //// chart-config " chart-config
;      " //// data " data
;      " //// widget " widget)
;
;    (condp = basis
;      :chart (make-widget key type chart-config data options)
;
;      :simple (make-simple-widget key type data options)
;
;      :map (make-map-widget key type chart-config data options))))
;
;
;(defn setup-widget [{:keys [key data-source type options] :as props}]
;
;  (prn "setup-widget " key "/" type
;                     " //// data-source " data-source
;                     " //// options " options
;                     " //// props " props)
;
;  (if data-source
;    (let [data (rf/subscribe [:app-db data-source])]
;
;      (prn "attaching data " data-source
;        " //// data "@data)
;
;      (build-widget props data options))
;
;    (build-widget props {} options)))


(defn chart-content
  [{:keys [name type]} data options]

  (let [widget @(rf/subscribe [:widget-type name])
        build-fn (:build-fn widget)]

    ;(prn "chart-content " name "of type " type
    ;  " //// widget " widget
    ;  ;" //// data " @data
    ;  " //// build-fn " build-fn)

    (build-fn widget data options)))


(defn widget
  "analogous to widgets.core/make-widget, but not really.
   This may be where our issue is..."

  [{:keys [name key basis build-fn options] :as w} id s]

  (let [ret (basic/basic-widget key options
              [:div
               (chart-content w s options)])]
    ;(prn "widget " w
    ;  " //// source " s
    ;  " //// ret " ret)
    ret))




(defn widget-setup
  "analogous to (widgets.core/setup-widget)"

  [w id]
  (let [source (rf/subscribe [:app-db (:data-source w)])
        ret    (widget w id source)]
    ;(prn "widget-setup " id
    ;  " //// data-source " (:data-source w)
    ;  " //// source " source
    ;  " //// ret")
    ret))




(defn widget-wrapper
  "analogous to grid/widget-wrapper"

  [w id]

  ;(prn "widget-wrapper (in) " w)

  (let [content (widget-setup w id)
        ret     [:div.widget w content]]

    ;(prn "widget-wrapper " w " / " id
    ;  " //// content " ret)

    ret))
