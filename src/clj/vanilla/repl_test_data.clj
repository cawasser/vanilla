(ns vanilla.repl-test-data
  (:require [clojure.tools.logging :as log]))
            ;x[vanilla.subscription-manager :as sm]))



(defn fetch-data []
  (log/info "REPL Test Data")

  {:title "REPL test data"
   :data "orignal fetch data"})




;(sm/refresh-source :vanilla.repl-test-data/fetch-data)
