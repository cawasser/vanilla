(ns vanilla.update-layout)


(defn- apply-updates [new-layout widget]
  (prn new-layout)
  (-> widget
    (assoc-in [:data-grid :x] (:x new-layout))
    (assoc-in [:data-grid :y] (:y new-layout))
    (assoc-in [:data-grid :w] (:w new-layout))
    (assoc-in [:data-grid :h] (:h new-layout))))




(defn reduce-layouts [layout]
  (map (fn [n]
         {:y   (:y n)
          :x   (:x n)
          :w   (:w n)
          :h   (:h n)
          :key (:i n)})
    layout))




(defn update-layout [widgets layout]
  (->> (for [w widgets
             l layout]
         (if (= (str (:key w)) (:key l))
           (apply-updates l w)))
    (remove nil?)
    (into [])))

