(ns dashboard-clj.layouts.core)

(def layout-store (atom {}))

(defn register-layout [name l]

  (.log js/console (str "register-layout " name)
                   " //// l " l)

  (swap! layout-store assoc name l))

(defn setup-layout [name options widgets]

  (.log js/console (str "setup-layout " name
                     " //// options " options
                     " //// widgets " widgets
                     " //// cache-hit" (get @layout-store name)))

  ; TODO - add the react-cache here

  ((get @layout-store name) widgets options))
