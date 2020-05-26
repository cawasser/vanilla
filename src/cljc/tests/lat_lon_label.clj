(ns tests.lat-lon-label
  (:require [clojure.spec-alpha2 :as s]
            [clojure.tools.logging :as log]
            [vanilla.specs.dataspecs :as vsd]))

;
; some test data for specing lat-lon-label
;
(def lat-lon-label-data [{:name "Canberra",
                          :lat -35.2809,
                          :lon 149.13
                          :dataLabels {:align "left" :x 5 :verticalAlign "middle"}}
                         {:name "Brisbane",
                          :lat -27.47012,
                          :lon 153.021072}
                         {:name "Geraldton",
                          :lat -28.782387,
                          :lon 114.607513}
                         {:name "Wagga Wagga",
                          :lat -35.117275,
                          :lon 147.356522,
                          :dataLabels {:align "right" :y 5 :verticalAlign "middle"}}])

(defn run-tests []
  (log/info " ")
  (log/info "RUNNING LATLONLABEL TESTS")
  (log/info " ")

  (log/info "   valid? tests")
  (log/info " ")

  (log/info "      ::is-vector 42 should be false = " (s/valid? ::vsd/is-vector 42))
  (log/info "      ::is-vector [] should be true = " (s/valid? ::vsd/is-vector []))

  (log/info " ")
  (log/info "   explain-str tests")
  (log/info " ")
  ;(log/info "      explain failure of ::vsd/is-keyword 42: " (s/explain-str ::vsd/is-keyword 42))

  (log/info " ")
  (log/info "END RUNNING LATLONLABEL TESTS")
  (log/info " ")
  (log/info " "))
