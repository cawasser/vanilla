(ns vanilla.widgets.util)



(defn process-data [data slice-at]
  ;(.log js/console "process-data" (str data))

  [{:colorByPoint true
    :keys ["name" "y" "selected" "sliced"]
    :data (map #(conj % false (> (second %) slice-at)) data)}])



(defn line->bar [data options]
  (let [dats (get-in data [:data (get-in options [:src :extract])])
        num  (count dats)]

    (into []
          (for [n (range num)]
            {:name (get-in dats
                           [n (get-in options [:src :name] :name)]
                           (str "set " n))
             :data (into [] (get-in dats
                                    [n (get-in options [:src :values] :values)]))}))))



(defn pie->bar [data options slice-at]
  (let [dats (get-in data [:data (get-in options [:src :extract])])
        new-data (into [] (map (fn [[n v]] {:name n :data [v]}) dats))]

    (.log js/console (str "pie->bar " data " -> " new-data))

    (assoc data :series new-data)))
  