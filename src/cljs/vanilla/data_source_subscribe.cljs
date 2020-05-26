(ns vanilla.data-source-subscribe
  (:require [ajax.core :as ajax :refer [GET POST]]
            [re-frame.core :as rf]))



(defn data-source-subscribe [sources]

  (POST "/services"
    {:format          (ajax/json-request-format {:keywords? true})
     :response-format (ajax/json-response-format {:keywords? true})
     :params          {:user @(rf/subscribe [:get-current-user]) :sources (clojure.core/pr-str sources)}}))


