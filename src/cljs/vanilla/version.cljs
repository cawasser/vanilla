(ns vanilla.version
  (:require
    [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [ajax.core :refer [GET POST] :as ajax]))




(rf/reg-event-db
  :set-version
  (fn-traced [db [_ version]]
             ;(prn ":set-version " version)
             (assoc db :version (:version version))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; VERSION NUMBER
;
;

(defn get-version []
  (GET "/version" {:headers         {"Accept" "application/transit+json"}
                   :response-format (ajax/json-response-format {:keywords? true})
                   :handler         #(rf/dispatch-sync [:set-version %])}))


(defn version-number
  "Returns the version number wrapped in a h6 element."
  []
  (let [version (rf/subscribe [:version])]
    (fn []
      ;[:div.level-left.has-text-left
      [:h6.subtitle.is-6 @version])))
