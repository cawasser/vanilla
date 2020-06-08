(ns gen.lat-lon-label
  (:require [clojure.spec-alpha2 :as s]
            [clojure.spec-alpha2.gen :as gen]
            [clojure.tools.logging :as log]
            [vanilla.specs.latlonlabel :as vsd]))


(defn gen-data []
  (log/info " ")
  (log/info "GENERATING LATLONLABEL DATA")
  (log/info " ")

  (log/info "   single data")
  (log/info " ")
  (log/info "generate ::vsd/data-format-valid: " (gen/generate (s/gen ::vsd/lat-lon-label-vector)))

  (log/info " ")
  (log/info "   data set")
  (log/info " ")
  (log/info "generate series ::vsd/data-format-valid: " (gen/sample (s/gen ::vsd/lat-lon-label-vector)))

  (log/info " ")
  (log/info "END GENERATING LATLONLABEL DATA")
  (log/info " ")
  (log/info " "))
