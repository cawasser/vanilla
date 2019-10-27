(ns vanilla.widgets.util)





(defn line->bar [data options]
  (let [dats (get-in data [:data (get-in options [:src :extract])])
        num  (count dats)]

    ;(.log js/console (str "line->bar " data))

    (into []
          (for [n (range num)]
            {:name (get-in dats
                           [n (get-in options [:src :name] :name)]
                           (str "set " n))
             :data (into [] (get-in dats
                                    [n (get-in options [:src :values] :values)]))}))))


