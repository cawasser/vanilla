(ns vanilla.db.excel-data
  (:require [dk.ative.docjure.spreadsheet :refer :all]
            [datascript.core :as d]
            [clojure.tools.logging :as log]))



(def schema {})
(def conn (d/create-conn schema))

(def filename "public/excel/Demo.xlsx")

; drf: "data reduction factor"
(def drf 10)

(def excel-defs [{:sheet      "X-Beams"
                  :column-map {:A :band
                               :B :beam-id
                               :C :lat
                               :D :lon
                               :E :radius
                               :G :type}
                  :post-fn    (fn [x] x)}
                 {:sheet      "Ka-Beams"
                  :column-map {:A :band
                               :B :beam-id
                               :C :lat
                               :D :lon
                               :E :radius
                               :G :type}
                  :post-fn    (fn [x] x)}
                 {:sheet      "SCN_NETWORK_CARRIER_VW"
                  :column-map {:A :satellite-id
                               :B :tx-beam
                               :C :tx-channel
                               :D :rx-beam
                               :E :rx-channel
                               :F :plan
                               :G :mission-id
                               :H :service
                               :R :data-rate}
                  :post-fn    (fn [x] x)}
                 {:sheet      "Missions"
                  :column-map {:A :task-name
                               :B :organization
                               :C :start-time
                               :D :end-time}
                  :post-fn    (fn [x] x)}
                 {:sheet      "Terminals"
                  :column-map {:C :terminal-id
                               :E :lat
                               :F :lon
                               :M :satellite-id
                               :N :tx-beam
                               :O :tx-channel
                               :P :rx-beam
                               :Q :rx-channel}
                  :post-fn    (fn [x] x)}
                 {:sheet      "Sat-Power-1000"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}
                 {:sheet      "Sat-Power-2000"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}
                 {:sheet      "Sat-Power-3000"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}
                 {:sheet      "Sat-Power-4000"
                  :column-map {:A :satellite-id
                               :B :freq
                               :C :channel-1-power
                               :D :channel-2-power
                               :E :channel-3-power}
                  :post-fn    (fn [x] (take-nth drf x))}])



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




; basic building block of fetching data from a spreadsheet
(comment


  (with-open [workbook (load-workbook-from-resource filename)]
    workbook)
  (with-open [workbook (load-workbook-from-resource "shabbay")]
    workbook)

  (try
    (with-open [workbook (load-workbook-from-resource "shabbay")]
      workbook)
    (catch Exception e (log/error "Exception: " (.getMessage e)))
    (finally (log/info "No Excel file found" filename)))



  (try
    (/ 1 0)
    (catch Exception e (log/error "caught exception: " (.getMessage e))))

  (def sheet "Missions")
  (def column-map {:A :name
                   :B :organization
                   :C :start-time
                   :D :end-time})
  (def post-fn (fn [x] x))

  (load-workbook filename)

  (->> (load-workbook filename)
    (select-sheet "Missions"))

  (->> (load-workbook filename)
    (select-sheet "Missions")
    row-seq)

  (->> (load-workbook "resources/public/excel/Demo - 9102.xlsx")
    (select-sheet "Missions")
    (select-columns {:A :name :B :organization :C :start-time :D :end-time})
    (drop 1))

  (->> (load-workbook filename)
    (select-sheet "SCN_NETWORK_CARRIER_VW")
    (select-columns {:A :satellite :B :rx-beam :C :rx-channel :D :tx-beam
                     :E :tx-channel :F :plan :G :mission-id :H :service
                     :R :data-rate})
    (drop 1))


  (map (fn [{:keys [sheet column-map post-fn]}]
         [filename sheet column-map post-fn])
    excel-defs)


  (with-open [workbook (load-workbook-from-resource filename)]
    (load-data workbook "Missions" {:A :task-name
                                    :B :organization
                                    :C :start-time
                                    :D :end-time}))

  (d/q
    '[:find ?task-name
      :where [?e :task-name ?task-name]]
    @conn)

  (d/q
    '[:find [(pull ?e [*]) ...]
      :where [?e :task-name _]]
    @conn)


  ())


; basics building blocks of processing SCN_ and Ka Beams sheets as 'event logs'
(comment
  (def context {:sheet      "SCN_NETWORK_CARRIER_VW"
                :column-map {:A :satellite-id
                             ;:B :tx-beam
                             ;:C :tx-channel
                             ;:D :rx-beam
                             ;:E :rx-channel
                             ;:F :plan-id
                             :G :mission-id
                             ;:K :tx-term-lat
                             ;:L :tx-term-lon
                             ;:N :rx-term-lat
                             ;:O :rx-term-lon
                             ;:Q :tx-term-id
                             ;:R :rx-term-id
                             :S :start-epoch
                             :T :end-epoch
                             :U :data-rate}
                :post-fn    (fn [x] x)})


  (def scn (->> (load-workbook "resources/public/excel/Demo - 9102.xlsx")
             (select-sheet (:sheet context))
             (select-columns (:column-map context))
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


  ;; at this point we have the epochs (notional):
  ;;
  ;#{"0000000Z JAN 2020" "010000Z JAN 2020" "020000Z JAN 2020"}
  ;
  ;; and the "events" (notional):
  ;;
  ;'({:epoch "000000Z JAN 2020" :event :add :data {:mission-id "M-1"}}
  ;  {:epoch "000000Z JAN 2020" :event :add :data {:mission-id "M-2"}}
  ;  {:epoch "010000Z JAN 2020" :event :add :data {:mission-id "M-3"}}
  ;  {:epoch "020000Z JAN 2020" :event :remove :data {:mission-id "M-1"}})
  ;
  ;; our goal is to have the data organized like this (notional):
  ;;
  ;; NB: this represents "materialized views" or "reductions over the events"
  ;;
  ;{"000000Z JAN 2020" #{{:mission-id "M-1"} {:mission-id "M-2"}} ;; add M-1, :add M-2
  ; "010000Z JAN 2020" #{{:mission-id "M-1"} {:mission-id "M-2"} {:mission-id "M-3"}} ;; :add M-3
  ; "020000Z JAN 2020" #{{:mission-id "M-2"} {:mission-id "M-3"}}} ;; :remove M-1
  ;
  ;
  ;


  ;(defn accumulate-events* [accum event]
  ;  (prn "accum" event)
  ;  (condp = (:event event)
  ;    :add (conj accum (:data event))
  ;    :remove (disj accum (:data event))))
  ;
  ;(accumulate-events* #{} (first events))
  ;(accumulate-events* #{} (second events))
  ;(map #(accumulate-events* #{} %) events)
  ;
  ;
  ;(accumulate-events* #{} (-> epoch-events first :data first))
  ;(accumulate-events* #{} (-> epoch-events first :data second))
  ;(map #(accumulate-events* #{} %) (-> epoch-events first :data))
  ;
  ;; lets work the 'outermost' recursion first, the one that processes a single epoch
  ;(defn accumulate-epoch [accum evt]
  ;  (loop [a accum
  ;         e evt]
  ;    (prn "recur" a)
  ;    (if (empty? e)
  ;      a
  ;      (recur (accumulate-events* a (first e)) (rest e)))))
  ;
  ;(def accum #{})
  ;(def evt (-> epoch-events first :data))
  ;(first evt)
  ;(rest evt)
  ;(empty? (rest (rest evt)))
  ;(accumulate-epoch #{} (-> epoch-events first :data))
  ;(accumulate-epoch #{} (-> epoch-events second :data))
  ;
  ;(accumulate-epoch
  ;  (accumulate-epoch #{} (-> epoch-events first :data))
  ;  (-> epoch-events second :data))
  ;
  ;; so far, so good
  ;
  ;; now let's accumulate all the epochs
  ;(def acc-map {})
  ;(def epochs (take 2 epoch-list))
  ;
  ;(defn find-events [epoch events]
  ;  (filter #(= epoch (:epoch %)) events))
  ;
  ;(defn accumulate-epochs [acc-map epochs events]
  ;  (loop [a   acc-map
  ;         e   epochs
  ;         evt events]
  ;    (prn "acc-epochs" a)
  ;    (if (empty? e)
  ;      a
  ;      (recur
  ;        (assoc a (first e)
  ;          (merge
  ;            (get a (first e))
  ;            (accumulate-epoch (get a (first e)) (find-events (first e) evt))))
  ;        (rest e)
  ;        evt))))
  ;




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


  ; build an empty state-map
  ;


  (defn apply-event [accum event]
    (condp = (:event event)
      :add (conj accum (:data event))
      :remove (disj accum (:data event))))



  ; apply all the events for a given epoch into the state-map
  (defn apply-epoch-events [accum events]
    (loop [a accum
           e events]
      (prn "apply-epoch-events" a)
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

  (def state-atom (atom (empty-state-map (take 5 epochs))))

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
  (def signal-path {:series [{:name "000000Z JAN 2020"
                              :keys ["from" "to" "weight"]
                              :data [["txCH-1" "txB-1" 7186]
                                     ["txB-1" "SAT-1" 7186]
                                     ["SAT-1" "rxB-1" 7186]
                                     ["rxB-1" "rxCH-1" 7186]]}
                             {:name "010000Z JAN 2020"
                              :keys ["from" "to" "weight"]
                              :data [["txCH-2" "txB-1" 7186]
                                     ["txB-1" "SAT-1" 7186]
                                     ["SAT-1" "rxB-1" 7186]
                                     ["rxB-1" "rxCH-4" 7186]]}
                             {:name "020000Z JAN 2020"
                              :keys ["from" "to" "weight"]
                              :data [["txCH-1" "txB-1" 7186]
                                     ["txB-1" "SAT-1" 7186]
                                     ["SAT-1" "rxB-1" 7186]
                                     ["rxB-1" "rxCH-1" 7186]]}]})
  ())














