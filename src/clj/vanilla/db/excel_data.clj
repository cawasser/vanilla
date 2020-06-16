(ns vanilla.db.excel-data
  (:require [dk.ative.docjure.spreadsheet :refer :all]
            [datascript.core :as d]
            [clojure.tools.logging :as log]))



(def schema {})
(def conn (d/create-conn schema))

(def filename "public/excel/Demo CONUS.xlsx")

; drf: "data reduction factor"
(def drf 10)

(def excel-defs [;{:sheet      "X-Beams"
                  ;:column-map {:A :band
                  ;             :B :beam-id
                  ;             :C :lat
                  ;             :D :lon
                  ;             :E :radius
                  ;             :G :type}
                  ;:post-fn    (fn [x] x)
                 ;{:sheet      "Ka-Beams"
                 ; :column-map {:A :band
                 ;              :B :beam-id
                 ;              :C :lat
                 ;              :D :lon
                 ;              :E :radius
                 ;              :G :type}
                 ; :post-fn    (fn [x] x)}
                 ;{:sheet      "SCN_NETWORK_CARRIER_VW"
                 ; :column-map {:A :satellite-id
                 ;              :B :tx-beam
                 ;              :C :tx-channel
                 ;              :D :rx-beam
                 ;              :E :rx-channel
                 ;              :F :plan
                 ;              :G :mission-id
                 ;              :H :service
                 ;              :R :data-rate}
                 ; :post-fn    (fn [x] x)}
                 ;{:sheet      "Missions"
                 ; :column-map {:A :task-name
                 ;              :B :organization
                 ;              :C :start-time
                 ;              :D :end-time}
                 ; :post-fn    (fn [x] x)}
                 ;{:sheet      "Terminals"
                 ; :column-map {:C :terminal-id
                 ;              :E :lat
                 ;              :F :lon
                 ;              :M :satellite-id
                 ;              :N :tx-beam
                 ;              :O :tx-channel
                 ;              :P :rx-beam
                 ;              :Q :rx-channel}
                 ; :post-fn    (fn [x] x)}
                 {:sheet      "Sat-Power-1"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}
                 {:sheet      "Sat-Power-2"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}])
                 ;{:sheet      "Sat-Power-3000"
                 ; :column-map {:A :satellite-id
                 ;              :B :freq
                 ;              :C :channel-1-power
                 ;              :D :channel-2-power
                 ;              :E :channel-3-power}
                 ; :post-fn    (fn [x] (take-nth drf x))}
                 ;{:sheet      "Sat-Power-4000"
                 ; :column-map {:A :satellite-id
                 ;              :B :freq
                 ;              :C :channel-1-power
                 ;              :D :channel-2-power
                 ;              :E :channel-3-power}
                 ; :post-fn    (fn [x] (take-nth drf x))}])



(defn- load-data
  ([workbook sheet column-map]
   (load-data workbook sheet column-map (fn [x] x)))

  ([workbook sheet column-map post-fn]
   (log/info "Loading " sheet)
   (->> workbook
     (select-sheet sheet)
     (select-columns column-map)
     (drop 1)
     post-fn
     (d/transact! conn))))


(defn init-from-excel [filename defs]
  (log/info "Init From Excel" filename)

  (try
    (with-open [workbook (load-workbook-from-resource filename)]
      (doall
        (map (fn [{:keys [sheet column-map post-fn]}]
               (load-data workbook sheet column-map post-fn))
          defs)))
    (catch Exception e (log/error "Exception: " (.getMessage e)))
    (finally (log/info "Excel file" filename "not loaded!"))))




; basics building blocks of processing SCN_ and Ka Beams sheets as 'event logs'
(comment
  (def signal-path-context
    {:sheet      "SCN_NETWORK_CARRIER_VW"
     :column-map {:A :satellite-id
                  :B :tx-beam
                  :C :tx-channel
                  :D :rx-beam
                  :E :rx-channel
                  :F :plan-id
                  :G :mission-id
                  :K :tx-term-lat
                  :L :tx-term-lon
                  :N :rx-term-lat
                  :O :rx-term-lon
                  :Q :tx-term-id
                  :R :rx-term-id
                  :S :start-epoch
                  :T :end-epoch
                  :U :data-rate}
     :post-fn    (fn [x] x)})


  (def scn (->> (load-workbook "resources/public/excel/Demo.xlsx")
             (select-sheet (:sheet signal-path-context))
             (select-columns (:column-map signal-path-context))
             (drop 1)))
  (count scn)

  (def epoch-list (->> (apply conj (map :start-epoch scn)
                         (map :end-epoch scn))
                    (into #{})
                    sort))
  (first epoch-list)
  (rest epoch-list)
  (count epoch-list)

  ; compute all the add and remove events (anytime some thing changes, so again 'start' and 'end')
  (defn extract-add-event [s]
    {:epoch (:start-epoch s) :event :add :data s})
  (defn extract-remove-event [s]
    {:epoch (:end-epoch s) :event :remove :data s})

  (def events (->> (map
                     (fn [s]
                       [(extract-add-event s) (extract-remove-event s)])
                     scn)
                flatten))
  (take 3 events)



  ())


(comment
  ; lets start from scratch with a simpler situation
  ;
  ;(def events [{:epoch 1 :event :add :data "Alpha"}
  ;             {:epoch 1 :event :add :data "Beta"}
  ;             {:epoch 2 :event :add :data "Delta"}
  ;             {:epoch 3 :event :add :data "Gamma"}
  ;             {:epoch 4 :event :remove :data "Alpha"}
  ;             {:epoch 4 :event :add :data "Epsilon"}
  ;             {:epoch 5 :event :remove :data "Beta"}
  ;             {:epoch 6 :event :remove :data "Epsilon"}])

  ; so in the end we should have
  ;
  ;(def state-map {1 #{"Alpha" "Beta"}
  ;                2 #{"Alpha" "Beta" "Delta"}
  ;                3 #{"Alpha" "Beta" "Delta" "Gamma"}
  ;                4 #{"Beta" "Delta" "Gamma" "Epsilon"}
  ;                5 #{"Delta" "Gamma" "Epsilon"}
  ;                6 #{"Delta" "Gamma"}})

  ; get all the epochs
  ;
  (def epochs (->> (map #(-> % :epoch) events)
                (into #{})
                sort))

  ; get the evens for a given epoch
  ;
  (defn events-for [events epoch]
    (filter #(= epoch (:epoch %)) events))
  (events-for events (nth epochs 0))
  (events-for events (nth epochs 4))


  (defn apply-event [accum event]
    (condp = (:event event)
      :add (conj accum (:data event))
      :remove (disj accum (:data event))))



  ; apply all the events for a given epoch into the state-map
  (defn apply-epoch-events [accum events]
    (loop [a accum
           e events]
      ;(prn "apply-epoch-events" a)
      (if (empty? e)
        a
        (recur (apply-event a (first e)) (rest e)))))
  (apply-epoch-events #{} (events-for events (nth epochs 0)))

  ; this runs each epoch against an empty set
  (map #(apply-epoch-events #{} (events-for events %)) epochs)


  ; we need to get each invocation to start with the results of the LAST invocation
  ;
  ; can we make an atom work?
  ;

  (defn empty-state-map [epochs]
    (zipmap epochs (repeat #{})))

  (let [last-accum (atom #{})]
    (for [epoch (keys @state-atom)]
      (let [current-accum (apply-epoch-events @last-accum (events-for events epoch))]
        (prn @last-accum "/" current-accum)
        (swap! state-atom assoc epoch current-accum)
        (reset! last-accum current-accum))))



  (defn apply-events [state-map events]
    (let [last-accum (atom #{})]
      (for [epoch (sort (into #{} (map #(:epoch %) events)))] ;(keys @state-atom)]
        (let [current-accum (apply-epoch-events @last-accum (events-for events epoch))]
          (prn @last-accum "/" current-accum)
          (swap! state-atom assoc epoch current-accum)
          (reset! last-accum current-accum)))))


  (def state-atom (atom (empty-state-map (take 5 epochs))))

  (apply-events state-atom (take 6 events))
  (apply-events state-atom events)


  (count @state-atom)
  (sort @state-atom)
  (first @state-atom)

  ; so now, what do we do with this data?
  ;
  ; 1. try to use it directly?
  ;
  ; 2. put "something" into datascript so we can use some part of our existing queries?


  ; let's start with what the results should look like. Keeping it simple to start,
  ; let's use Missions, since we really don't care about the "epochs"
  ;
  (def missions-list {:data [["Mission-1" "000000Z JAN 2020" "010000Z JAN 2020"]
                             ["Mission-2" "000000Z JAN 2020" "020000Z JAN 2020"]
                             ["Mission-3" "010000Z JAN 2020" "050000Z JAN 2020"]]})


  ; signal-path - this DOES depend on the epochs, each epoch should be it's own :series
  ;
  (def test-signal-path [{:name         "000000Z JAN 2020"
                          :showInLegend true
                          :keys         ["from" "to" "weight"]
                          :data         [["txCH-1" "txB-1" 7186] ["txB-1" "SAT-1" 7186] ["SAT-1" "rxB-1" 7186] ["rxB-1" "rxCH-1" 7186]
                                         ["txCH-a" "txB-a" 7186] ["txB-a" "SAT-2" 7186] ["SAT-2" "rxB-a" 7186] ["rxB-a" "rxCH-a" 7186]]}
                         {:name         "010000Z JAN 2020"
                          :showInLegend true
                          :keys         ["from" "to" "weight"]
                          :data         [["txCH-2" "txB-1" 7186] ["txB-1" "SAT-1" 7186] ["SAT-1" "rxB-1" 7186] ["rxB-1" "rxCH-4" 7186]
                                         ["txCH-a" "txB-a" 7186] ["txB-a" "SAT-2" 7186] ["SAT-2" "rxB-a" 7186] ["rxB-a" "rxCH-a" 7186]
                                         ["txCH-b" "txB-a" 7186] ["txB-a" "SAT-2" 7186] ["SAT-2" "rxB-a" 7186] ["rxB-a" "rxCH-q" 7186]]}
                         {:name         "020000Z JAN 2020"
                          :showInLegend true
                          :keys         ["from" "to" "weight"]
                          :data         [["txCH-1" "txB-1" 7186] ["txB-1" "SAT-1" 7186] ["SAT-1" "rxB-1" 7186] ["rxB-1" "rxCH-1" 7186]
                                         ["txCH-2" "txB-1" 7186] ["txB-1" "SAT-1" 7186] ["SAT-1" "rxB-1" 7186] ["rxB-1" "rxCH-4" 7186]
                                         ["txCH-a" "txB-a" 7186] ["txB-a" "SAT-2" 7186] ["SAT-2" "rxB-a" 7186] ["rxB-a" "rxCH-a" 7186]
                                         ["txCH-b" "txB-a" 7186] ["txB-a" "SAT-2" 7186] ["SAT-2" "rxB-a" 7186] ["rxB-a" "rxCH-q" 7186]]}])

  (defn event-path [{:keys [satellite-id tx-beam tx-channel rx-beam
                            rx-channel tx-term-id rx-term-id data-rate]}]
    [[tx-term-id tx-channel data-rate]
     [tx-channel tx-beam data-rate]
     [tx-beam satellite-id data-rate]
     [satellite-id :rx-beam data-rate]
     [rx-beam rx-channel data-rate]
     [rx-channel rx-term-id data-rate]])

  (defn build-signal-path [epoch events]
    {:name         epoch
     :showInLegend true
     :keys         ["from" "to" "weight"]
     :data         (map #(event-path %) events)})

  (defn signal-path-query [epoch-map]
    (for [[epoch events] epoch-map]
      (build-signal-path epoch events)))

  (signal-path-query @state-atom)


  ())














