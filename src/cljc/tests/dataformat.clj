(ns tests.dataformat
  (:require [clojure.spec-alpha2 :as s]
            [clojure.tools.logging :as log]
            [vanilla.specs.dataspecs :as vsd]))

(defn run-tests []
  (log/info " ")
  (log/info "RUNNING DATAFORMAT TESTS")
  (log/info " ")

  (log/info "   valid? tests")
  (log/info " ")

  (log/info "      ::vsd/is-keyword :data-format/lat-lon-label should be true = " (s/valid? ::vsd/is-keyword :data-format/lat-lon-label))
  (log/info "      ::vsd/is-keyword 42 should be false = " (s/valid? ::vsd/is-keyword 42))
  (log/info "      ::vsd/data-format-in-set :data-format/lat-lon-label should be true = " (s/valid? ::vsd/data-format-in-set :data-format/lat-lon-label))
  (log/info "      ::vsd/data-format-in-set :data-format/lat-lon should be false = " (s/valid? ::vsd/data-format-in-set :data-format/lat-lon))
  (log/info "      ::vsd/data-format-valid :data-format/lat-lon-label should be true = " (s/valid? ::vsd/data-format-valid :data-format/lat-lon-label))
  (log/info "      ::vsd/data-format-valid 42 should be false = " (s/valid? ::vsd/data-format-valid 42))
  (log/info "      ::vsd/data-format-valid :data-format/lat-lon-label should be true = " (s/valid? ::vsd/data-format-valid :data-format/lat-lon-label))
  (log/info "      ::vsd/data-format-valid :data-format/lat-lon should be false = " (s/valid? ::vsd/data-format-valid :data-format/lat-lon))

  (log/info " ")
  (log/info "   explain-str tests")
  (log/info " ")
  (log/info "      explain failure of ::vsd/is-keyword 42: " (s/explain-str ::vsd/is-keyword 42))

  (log/info " ")
  (log/info "END RUNNING DATAFORMAT TESTS")
  (log/info " ")
  (log/info " "))



