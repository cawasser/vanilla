(ns vanilla.dataformat-test
  (:require [clojure.test :refer :all]
            [clojure.spec-alpha2 :as s]
            [vanilla.specs.util :as vsu]
            [vanilla.specs.dataformat :as vsd]))

(comment [(deftest dataformat-is-keyword-test-1
            (is (s/valid? ::vsu/is-keyword :data-format/lat-lon-label)))

          (deftest dataformat-is-keyword-test-2
            (is (s/valid? ::vsu/is-keyword 42)))

          (deftest dataformat-in-set-test-1
            (is (s/valid? ::vsd/data-format-in-set :data-format/lat-lon-label)))

          (deftest dataformat-in-set-test-2
            (is (s/valid? ::vsd/data-format-in-set :data-format/lat-lon)))

          (deftest dataformat-valid-test-1
            (is (s/valid? ::vsd/data-format-valid :data-format/lat-lon-label)))

          (deftest dataformat-valid-test-2
            (is (s/valid? ::vsd/data-format-valid 42)))

          (deftest dataformat-valid-test-3
            (is (s/valid? ::vsd/data-format-valid :data-format/lat-lon-label)))

          (deftest dataformat-valid-test-4
            (is (s/valid? ::vsd/data-format-valid :data-format/lat-lon)))
          ])