(ns vanilla.widgets.core
  (:require [re-frame.core :as rf]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.make-map :as mm]))

(defn chart-content
  [{:keys [name type]} source options]

  (let [widget @(rf/subscribe [:widget-type name])
        build-fn (:build-fn widget)]

    ;(prn "chart-content " name "of type " type
    ;  " //// widget " widget
      ;" //// data " @data
      ;" //// build-fn " build-fn

    (build-fn widget source options)))


(defn widget
  "analogous to widgets.core/make-widget, but not really.
   This may be where our issue is..."

  [{:keys [name key basis build-fn options] :as w} id s]

  (let [ret (basic/basic-widget key options
              ;[:div
               (chart-content w s options))]
    ;(prn "widget " w
    ;  " //// source " s
    ;  " //// ret " ret)
    ret))




(defn widget-setup
  "analogous to (widgets.core/setup-widget)"

  [w id]
  (let [ret    (widget w id (:data-source w))]
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
