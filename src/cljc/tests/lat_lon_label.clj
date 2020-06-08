(ns tests.lat-lon-label
  (:require [clojure.spec-alpha2 :as s]
            [clojure.tools.logging :as log]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.latlonlabel :as vsd]))

;
; some test data for specing lat-lon-label
;
(def lat-lon-label-data [{:name "Canberra",
                          :lat -35.2809,
                          :lon 149.13}
                         {:name "Brisbane",
                          :lat -27.47012,
                          :lon 153.021072}
                         {:name "Geraldton",
                          :lat -28.782387,
                          :lon 114.607513}
                         {:name "Wagga Wagga",
                          :lat -35.117275,
                          :lon 147.356522}])

(def single-lat-lon-label {::vsd/lat 35.33 ::vsd/name "test name" ::vsd/lon 150.00})
(def single-lat-lon-label-un {:lat 35.33 :name "test name" :lon 150.00})
(def single-lat-lon-label-un-dif-lat-name {:latitude 35.33 :name "test name" :lon 150.00})
(def single-lat-lon-no-label {::vsd/lat 35.33 ::vsd/lon 150.00})
(def single-lat-lon-bad-lat {::vsd/lat 35.33 ::vsd/name "test name" ::vsd/lon 1500.00})


(defn run-tests []
  (log/info " ")
  (log/info "RUNNING LATLONLABEL TESTS")
  (log/info " ")

  (log/info "   valid? tests")
  (log/info " ")

  (log/info "      ::is-vector 42 should be false = " (s/valid? ::vsu/is-vector 42))
  (log/info "      ::is-vector [] should be true = " (s/valid? ::vsu/is-vector []))
  (log/info "      ::vsd/lat-lon-label single-lat-lon-label should be true = " (s/valid? ::vsd/lat-lon-label single-lat-lon-label))
  (log/info "      ::vsd/lat-lon-label-un single-lat-lon-label-un should be false = " (s/valid? ::vsd/lat-lon-label-un single-lat-lon-label-un))
  (log/info "      ::vsd/lat-lon-label-un single-lat-lon-label-un-dif-lat-name should be false = " (s/valid? ::vsd/lat-lon-label-un single-lat-lon-label-un-dif-lat-name))
  (log/info "      ::vsd/lat-lon-label single-lat-lon-no-label should be false = " (s/valid? ::vsd/lat-lon-label single-lat-lon-no-label))
  (log/info "      ::vsd/lat-lon-label single-lat-lon-bad-lat should be false = " (s/valid? ::vsd/lat-lon-label single-lat-lon-bad-lat))
  (log/info "      ::vsd/lat-lon-label-vector lat-lon-label-data should be true = " (s/valid? ::vsd/lat-lon-label-vector lat-lon-label-data))

  (log/info " ")
  (log/info "   conform tests")
  (log/info " ")
  (log/info "      ::is-vector 42 should return :clojure.spec-alpha2/invalid = " (s/conform ::vsu/is-vector 42))
  (log/info "      ::is-vector [] should return an empty vector '[]' = " (s/conform ::vsu/is-vector []))
  (log/info "      ::vsd/lat-lon-label single-lat-lon-label should return :clojure.spec-alpha2/invalid = " (s/conform ::vsd/lat-lon-label single-lat-lon-label))
  (log/info "      ::vsd/lat-lon-label-un single-lat-lon-label-un should return a valid single map = " (s/conform ::vsd/lat-lon-label-un single-lat-lon-label-un))
  (log/info "      ::vsd/lat-lon-label-un single-lat-lon-label-un-dif-lat-name should return :clojure.spec-alpha2/invalid = " (s/conform ::vsd/lat-lon-label-un single-lat-lon-label-un-dif-lat-name))
  (log/info "      ::vsd/lat-lon-label single-lat-lon-no-label should return :clojure.spec-alpha2/invalid = " (s/conform ::vsd/lat-lon-label single-lat-lon-no-label))
  (log/info "      ::vsd/lat-lon-label single-lat-lon-bad-lat should return :clojure.spec-alpha2/invalid = " (s/conform ::vsd/lat-lon-label single-lat-lon-bad-lat))
  (log/info "      ::vsd/lat-lon-label-vector lat-lon-label-data should return a vector of valid maps = " (s/conform ::vsd/lat-lon-label-vector lat-lon-label-data))

  (log/info " ")
  (log/info "   explain-str tests")
  (log/info " ")

  (log/info "      explain failure of ::vsd/lat-lon-label single-lat-lon-label: " (s/explain-str ::vsd/lat-lon-label single-lat-lon-label))
  (log/info "      explain failure of ::vsd/lat-lon-label-un single-lat-lon-label-un: " (s/explain-str ::vsd/lat-lon-label-un single-lat-lon-label-un))
  (log/info "      explain failure of ::vsd/lat-lon-label-un single-lat-lon-label-un-dif-lat-name: " (s/explain-str ::vsd/lat-lon-label-un single-lat-lon-label-un-dif-lat-name))
  (log/info "      explain failure of ::vsd/lat-lon-label single-lat-lon-no-label: " (s/explain-str ::vsd/lat-lon-label single-lat-lon-no-label))
  (log/info "      explain failure of ::vsd/lat-lon-label single-lat-lon-bad-lat: " (s/explain-str ::vsd/lat-lon-label single-lat-lon-bad-lat))
  (log/info "      explain failure of ::vsd/lat-lon-label-vector lat-lon-label-data: " (s/explain-str ::vsd/lat-lon-label-vector lat-lon-label-data))

  (log/info " ")
  (log/info "END RUNNING LATLONLABEL TESTS")
  (log/info " ")
  (log/info " "))
