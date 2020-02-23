(ns vanilla.maps.utils
  (:require [reagent.core :as reagent]))



; take (almost) directly from https://github.com/cfelde/re-frame-highcharts/blob/master/src/re_frame_highcharts/utils.cljs


; Highcharts wants to maintain its own instance, with mutating state.
; So we'll need to break from our lovely pure world and manage these.
; The below atom holds a map of these chart instances, keys by a chart id.
(defonce map-instances (atom {}))

(defn hc-map
  [{:keys [chart-meta]}]
  (let [style (or (:style chart-meta) {:height "100%" :width "100%"})]
    (letfn [(render-chart
              []
              [:div {:style style}])
            (mount-chart
              [this]
              (let [[_ {:keys [chart-meta chart-data]}] (reagent/argv this)
                    chart-id (:id chart-meta)
                    chart-instance (js/Highcharts.mapChart. (reagent/dom-node this)
                                     (clj->js chart-data))]
                (swap! map-instances assoc chart-id chart-instance)))
            (ensure-series
              [chart-instance all-ids id-chart-data]
              (let [current-ids (into #{} (remove nil? (map #(-> % .-options .-id) (.-series chart-instance))))
                    unwanted-ids (remove #(= % "highcharts-navigator-series") (reduce disj current-ids all-ids))
                    existing-ids (filter (partial contains? current-ids) all-ids)
                    new-ids (remove (partial contains? current-ids) all-ids)]
                (doall (map #(-> chart-instance (.get %) (.remove false)) unwanted-ids))
                (doall (map #(-> chart-instance (.get %) (.setData (clj->js (:data (get id-chart-data %))) false)) existing-ids))
                (doall (map #(-> chart-instance (.addSeries (clj->js (get id-chart-data %)) false)) new-ids))
                (.redraw chart-instance false)))
            (update-chart
              [this]
              (let [[_ {:keys [chart-meta chart-data]}] (reagent/argv this)
                    chart-id (:id chart-meta)]
                (if (:redo chart-meta)
                  (swap! map-instances dissoc chart-id))
                (if-let [chart-instance (get @map-instances chart-id)]
                  (ensure-series chart-instance (map :id (:series chart-data)) (into {} (map #(vector (:id %) %) (:series chart-data))))
                  (mount-chart this))))]
      (reagent/create-class {:reagent-render render-chart
                             :component-did-mount mount-chart
                             :component-did-update update-chart}))))

