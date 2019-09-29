(ns vanilla.db)


(def spectrum-data [{:name "trace-1"
                     :values (into [] (take 200 (repeatedly #(+ 5.0 (rand 5)))))}])