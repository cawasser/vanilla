(ns vanilla.processing
  (:require [dashboard-clj.components.websocket :as ws]))

            ; just to be sure they are compiled
            ;[chocolate.protobuf.person]
            ;[chocolate.protobuf.message]))

(defonce edn-messages-received
         (atom []))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; handler for EDN messages
;

(defn edn-handler
  [processing-fn]
  (fn [body parsed envelope components]
    (swap! edn-messages-received conj parsed)
    (processing-fn body parsed envelope components)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;; Handler and supporting functions for power.queue messages
;
(defonce pwr-measurement-spool (atom [{:name "device-1"
                                       :keys ["x" "y"]
                                       :data []}
                                      {:name "device-2"
                                       :keys ["x" "y"]
                                       :data []}]))

(defn get-map [series id]
  (->> series
       (filter #(= (:name %) id))
       first))


(defn new-measurement [device size]
  ;(prn "HANDLER device: " device
  ;     "/////// atom: " @pwr-measurement-spool)

  (let [current (get-map @pwr-measurement-spool (:name device))]
    (-> current
        :data
        (concat (:data device))
        (#(if (< size (count %))
            (into [] (drop (- (count %) size) (into [] %)))
            %))
        (#(assoc current :data %)))))



(defn pwr-handler
  [processing-fn]
  (fn [body parsed envelope components]

    (reset! pwr-measurement-spool (mapv #(new-measurement % 50) (:series parsed)))   ;;change int to desired size of spool
    ;(prn "POWER PROC ATOM: " @pwr-measurement-spool)

    (processing-fn body (assoc parsed :series @pwr-measurement-spool) envelope components)))




(comment

  (def parsed
    {:title "Device Power",
     :data-format "data-format/date-y",
     :series [{:name "device-1", :keys ["x" "y"], :data [[88888888 -23.935930127916723] [9999999 -24.023795596988155]]}
              {:name "device-2", :keys ["x" "y"], :data [[44444444 -23.531746250776862] [1234567 -24.38571874842697]]}],
     :src/chart-title "dBm",
     :src/x-title "Date/Time",
     :src/y-title "Power"})

  (def series
    [{:name "device-1", :keys ["x" "y"], :data [[1585676125444 -23.647308446033634]]}
     {:name "device-2", :keys ["x" "y"], :data [[1585676125445 -23.587703385067623]]}])


  (def device
    {:name "device-1",
     :keys ["x" "y"],
     :data [[1585590078472 -23.935930127916723] [1585590083609 -24.023795596988155]
            [1585590088612 -23.802975551874162] [1585590093614 -23.967078591100993]]})



  (new-measurement device 4)

  (reset! pwr-measurement-spool [])
  (reset! pwr-measurement-spool [{:name "device-1"
                                         :keys ["x" "y"]
                                         :data [[555555555 -23.647308446033634]]}
                                        {:name "device-2"
                                         :keys ["x" "y"]
                                         :data [[666666666 -23.647308446033634]]}])
  (swap! pwr-measurement-spool conj series)
  @pwr-measurement-spool
  (get @pwr-measurement-spool 0)



    )



