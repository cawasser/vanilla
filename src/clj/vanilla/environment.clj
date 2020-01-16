(ns vanilla.environment
  (:require [cprop.core :refer [load-config]]
            [cprop.source :as source]))


(def environment (load-config
                   :merge
                   [(source/from-system-props)
                    (source/from-env)]))
