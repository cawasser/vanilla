(ns vanilla.carousel-service
  (:require [clojure.tools.logging :as log]))


(defn fetch-data []
  (log/info "Carousel FAKE Service")

  {:title       "carousel Data"
   :data-format :data-format/carousel

   :data        {:data  "carousel fake data"}})

