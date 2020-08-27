(ns vanilla.data-source-subscribe
  (:require [ajax.core :as ajax :refer [GET POST]]
            [dashboard-clj.core :refer [?csrf-token]]
            [re-frame.core :as rf]))



(defn data-source-subscribe [sources]

  (POST "/services"
    {:format          (ajax/json-request-format {:keywords? true})
     :response-format (ajax/json-response-format {:keywords? true})
     :headers         {"x-csrf-token" ?csrf-token}
     :params          {:user @(rf/subscribe [:get-current-user]) :sources (clojure.core/pr-str sources)}
     :handler         #()
     :error-handler   #(prn "Subscribe ERROR: " %)}))


