(ns vanilla.carousel-service
  (:require [clojure.tools.logging :as log]))


(defn fetch-data []
  (log/info "Carousel FAKE Service")

  {:title       "carousel data"
   :data-format :data-format/carousel
   :data        "heatmap-data"})

