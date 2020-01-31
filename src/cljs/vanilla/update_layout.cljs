(ns vanilla.update-layout
  (:require
    [ajax.core :as ajax :refer [GET POST]]
    [cljsjs.toastr]
    [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]))


(rf/reg-event-db
  :layout-message
  (fn-traced [db [_ success?]]
             (if success?
               (js/toastr.success "Layout Saved!")
               (js/toastr.error "Layout Save Failed"))
             db))


(defn save-layout [layout]
  ;(prn "saving layout: " (clojure.core/pr-str layout))

  (POST "/save-layout"
        {:format          (ajax/json-request-format {:keywords? true})
         :response-format (ajax/json-response-format {:keywords? true})
         :params          {:widgets (clojure.core/pr-str layout)}       ;convert the whole layout struct to a string to preserve values
         :on-success      (rf/dispatch [:layout-message true])
         :on-error        (rf/dispatch [:layout-message false])}))


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
         (if (= (str (:key w)) (str (:key l)))
           (apply-updates l w)))
    (remove nil?)
    (into [])))

