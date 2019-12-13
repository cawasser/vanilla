(ns dashboard-clj.widgets.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]))

(def widget-store (atom {}))

; TODO - is having a widget registry even worth doing?

(defn register-widget [name w]
  (swap! widget-store assoc name w))





(defn setup-widget [{:keys [data-source type options]}]

  ;(.log js/console (str "setup-widget " data-source
  ;                   " //// type " type
  ;                   " //// options " options))

  (if data-source
    (let [data (rf/subscribe [:app-db data-source])]
      [(get @widget-store type) @data options])
    ((get @widget-store type) nil options)))
