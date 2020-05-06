(ns vanilla.update-layout
  (:require
    [ajax.core :as ajax :refer [GET POST]]
    ["toastr" :as toastr]
    [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [clojure.edn :as edn]
    [dashboard-clj.core :as d]
    [vanilla.widget-defs :as widget-defs]
    [cljs-uuid-utils.core :as uuid]))


(defn get-services []
  (GET "/services" {:headers         {"Accept" "application/transit+json"}
                    :response-format (ajax/json-response-format {:keywords? true})
                    :handler         #(rf/dispatch-sync [:set-services %])}))


;(rf/reg-sub
;  :active-widgets-by-user
;  (fn [db _]
;    (get db :widgets)))

(rf/reg-event-db
  :layout-message
  (fn-traced [db [_ response]]
             (if (not (:new-login db))          ; if its first login dont show saved toast
               (if (= (:status response) 200)
                 (toastr/success "Layout Saved!")
                 (toastr/error "Layout Save Failed")))
             (assoc db :new-login false)))

(defn data-source-subscribe [sources]

  (POST "/services"
        {:format          (ajax/json-request-format {:keywords? true})
         :response-format (ajax/json-response-format {:keywords? true})
         :params          {:user @(rf/subscribe [:get-current-user]) :sources (clojure.core/pr-str sources)}
         :handler         (prn "Handled DS subscribe: " sources)
         :error-handler   (prn "Error handling DS subscribe" sources)}))



(defn save-layout [layout]
  ;(prn "saving layout: " (clojure.core/pr-str layout))

  (data-source-subscribe (mapv #(:data-source %) layout))

  (POST "/save-layout"
        {:format          (ajax/json-request-format {:keywords? true})
         :response-format (ajax/json-response-format {:keywords? true})
         :params          {:widgets (clojure.core/pr-str (map #(dissoc % :build-fn) layout))}       ;convert the whole layout struct to a string to preserve values
         :handler         #(rf/dispatch [:layout-message %])
         :error-handler   #(rf/dispatch [:layout-message %])}))


(defn get-build-fn [name]
  (:build-fn (->> widget-defs/widgets
                  (filter #(= (:name %) name))
                  first)))

;; Helper function to set-layout to define function called for each value
(def conversion {:ret_types edn/read-string
                 :name edn/read-string
                 :basis edn/read-string
                 :data-grid edn/read-string
                 :build-fn get-build-fn    ;;give me the widget_def build function from the :name
                 :type edn/read-string
                 :data-source edn/read-string
                 :options edn/read-string})

(rf/reg-event-db
  :set-layout
  (fn-traced [db [_ layout-data]]
             ;(prn "Set-layout start: " (:layout layout-data))
             (if (not (empty? (:layout layout-data)))             ; if its not initial page load with empty layout-table
               (let [read (:layout layout-data)
                     data (map #(assoc % :build-fn "temp") read)    ;re-add build-fn key and fake value to all widget maps
                     converted-data (mapv (fn [{:keys [ret_types name basis data-grid type data-source options] :as original}]
                                            (assoc original
                                              :ret_types ((:ret_types conversion) ret_types)
                                              :name ((:name conversion) name)
                                              :basis ((:basis conversion) basis)
                                              :build-fn ((:build-fn conversion) name)     ; pass it the name, just a temp str in :build-fn right now
                                              :data-grid ((:data-grid conversion) data-grid)
                                              :type ((:type conversion) type)
                                              :data-source ((:data-source conversion) data-source)
                                              :options ((:options conversion) options)))
                                          data)]

                 ;(prn ":set-layout CONVERTED:  " converted-data)

                 (data-source-subscribe (mapv #(:data-source %) converted-data))
                 ;(d/connect-to-data-sources)

                 (assoc db :widgets converted-data
                           :next-id (uuid/uuid-string (uuid/make-random-uuid))))
               ; else it is initial page load with empty layout-table
               (assoc db :widgets []
                         :next-id (uuid/uuid-string (uuid/make-random-uuid))))))

(defn get-layout [user]
  (if (some? user)    ;if a user is logged in, go get their widgets, otherwise clear screen
    (GET "/layout" {:headers          {"Accept" "application/transit+json"}
                    :response-format  (ajax/json-response-format {:keywords? true})
                    :params           {:username user}
                    :handler          #(rf/dispatch-sync [:set-layout %])})
    (rf/dispatch [:set-layout []])))


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

