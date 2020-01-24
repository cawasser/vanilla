(ns vanilla.update-layout
  (:require
    [ajax.core :as ajax :refer [GET POST]]))


(defn save-layout [layout]
  (prn "saving layout: " (clojure.core/pr-str layout))

  (POST "/save-layout"
        {:format          (ajax/json-request-format {:keywords? true})
         :response-format (ajax/json-response-format {:keywords? true})
         :params          {:widgets (clojure.core/pr-str layout)}       ;convert the whole layout struct to a string to preserve values
         :handler         #(prn "Layout SAVED!")
         :on-error        #(prn "ERROR saving the layout " %)}))

(defn- apply-updates [new-layout widget]
  ;(prn "apply layout " new-layout)
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

