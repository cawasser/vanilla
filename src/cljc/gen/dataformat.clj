(ns gen.dataformat
  (:require [clojure.spec-alpha2 :as s]
            [clojure.spec-alpha2.gen :as gen]
            [clojure.tools.logging :as log]
            [vanilla.specs.dataspecs :as vsd]))


(defn gen-data []
  (log/info " ")
  (log/info "GENERATING DATAFORMAT DATA")
  (log/info " ")

  (log/info "   single data")
  (log/info " ")

  (log/info "generate ::vsd/data-format-in-set: " (gen/generate (s/gen ::vsd/data-format-in-set)))
  (log/info "generate ::vsd/is-keyword: " (gen/generate (s/gen ::vsd/is-keyword)))

  ;
  ;this didn't work if ::data-format-valid had the keyword check first in the s/and!!!
  (log/info "generate ::vsd/data-format-valid: " (gen/generate (s/gen ::vsd/data-format-valid)))
  ;

  (log/info " ")
  (log/info "   data set")
  (log/info " ")
  (log/info "generate series ::vsd/data-format-valid: " (gen/sample (s/gen ::vsd/data-format-valid)))
  (log/info "generate series ::vsd/data-format-in-set: " (gen/sample (s/gen ::vsd/data-format-in-set)))

  (log/info " ")
  (log/info "END GENERATING DATAFORMAT DATA")
  (log/info " ")
  (log/info " "))

