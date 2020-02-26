(ns vanilla.processing
  (:require [dashboard-clj.components.websocket :as ws]))

            ; just to be sure they are compiled
            ;[chocolate.protobuf.person]
            ;[chocolate.protobuf.message]))

(defonce edn-messages-received
         (atom []))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; handler for EDN messages
;

(defn edn-handler
  [processing-fn]
  (fn [body parsed envelope components]
    (swap! edn-messages-received conj parsed)
    (processing-fn body parsed envelope components)))






;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; handler for Protobuf messages
;


;(defn pb-processing-fn
;  [pb_type body parsed envelope components]
;
;  (let [decoded (pb-if/decode-content pb_type body)]
;    (ws/send-to-all! decoded))
;  :ack)
;




