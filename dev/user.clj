(ns user
  (:require [vanilla.server :as server]
            [figwheel-sidecar.repl-api :as figwheel]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)


(defn run []
    (figwheel/start-figwheel!))

(def browser-repl figwheel/cljs-repl)
