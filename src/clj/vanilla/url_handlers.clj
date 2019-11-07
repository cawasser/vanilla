(ns vanilla.url-handlers
  (:require [vanilla.service-deps :as deps]))


;;;;;;;;;;;;;;;;
;
; HTML endpoint handlers


(defn get-services []
  deps/datasources)


