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
                               :G :mission-name
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
                     :E :tx-channel :F :plan :G :mission-name :H :service
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
                             ;:F :plan
                             :G :mission-name
                             ;:J :tx-term-lat
                             ;:K :tx-term-lon
                             ;:L :rx-term-lat
                             ;:M :rx-term-lon
                             ;:N :tx-term-id
                             ;:O :rx-term-id
                             :P :start-epoch
                             :Q :end-epoch}
                ;:R :data-rate}
                :post-fn    (fn [x] x)})

  (->> (load-workbook-from-resource "public/excel/Demo - 9102.xlsx")
    (select-sheet (:sheet context))
    (select-columns (:column-map context))
    (drop 1))

  ;(def scn (->> (load-workbook-from-resource "public/excel/Demo - 9102.xlsx")
  ;           (select-sheet (:sheet context))
  ;           (select-columns (:column-map context))
  ;           (drop 1)))
  ;
  ;
  ;; find all the 'epoch edges' (any time something changes, so the 'start' and 'end')
  ;(map :start-epoch scn)
  ;(map :end-epoch scn)
  ;
  ;(->> (apply conj (map :start-epoch scn)
  ;       (map :end-epoch scn))
  ;  (into #{})
  ;  sort)
  ;
  (def epoch-list (->> (apply conj (map :start-epoch scn)
                         (map :end-epoch scn))
                    (into #{})
                    sort))
  (first epoch-list)
  (rest epoch-list)
  (count epoch-list)
  ;
  ; compute all the add and remove events (anytime some thing changes, so again 'start' and 'end')
  (defn extract-add-event [s]
    {:epoch (:start-epoch s) :event :add :data s})
  (defn extract-remove-event [s]
    {:epoch (:end-epoch s) :event :remove :data s})


  ;(extract-add-event (first scn))
  ;(extract-remove-event (first scn))

  (->> (map
         (fn [s]
           [(extract-add-event s) (extract-remove-event s)])
         scn)
    flatten)

  (def events (->> (map
                     (fn [s]
                       [(extract-add-event s) (extract-remove-event s)])
                     scn)
                flatten))
  (take 3 events)
  ;
  ;; at this point we have the epochs (notional):
  ;;
  ;#{"0000000Z JAN 2020" "010000Z JAN 2020" "020000Z JAN 2020"}
  ;
  ;; and the "events" (notional):
  ;;
  ;'({:epoch "000000Z JAN 2020" :event :add :data {:mission-name "M-1"}}
  ;  {:epoch "000000Z JAN 2020" :event :add :data {:mission-name "M-2"}}
  ;  {:epoch "010000Z JAN 2020" :event :add :data {:mission-name "M-3"}}
  ;  {:epoch "020000Z JAN 2020" :event :remove :data {:mission-name "M-1"}})
  ;
  ;; our goal is to have the data organized like this (notional):
  ;;
  ;; NB: this represents "materialized views" or "reductions over the events"
  ;;
  ;{"000000Z JAN 2020" #{{:mission-name "M-1"} {:mission-name "M-2"}} ;; add M-1, :add M-2
  ; "010000Z JAN 2020" #{{:mission-name "M-1"} {:mission-name "M-2"} {:mission-name "M-3"}} ;; :add M-3
  ; "020000Z JAN 2020" #{{:mission-name "M-2"} {:mission-name "M-3"}}} ;; :remove M-1
  ;
  ;
  ;

  ; could we just run through all the events regardless of the epoch and do the accumulation directly?

  ;(defn helper [accum event]
  ;  (condp = (:event event)
  ;    :add (conj accum (:data event))
  ;    :remove (disj accum (:data event))))
  ;
  ;(helper #{} (first events))   ;; this is an :add, so #{{...stuff...}}
  ;(helper #{} (second events))  ;; this is a :remove, so #{}
  ;(map #(helper #{} %) events)
  ;
  ;(def acc-map {})
  ;(def event (first events))
  ;(get acc-map (:epoch event) #{})
  ;
  ;(defn f [acc-map event]
  ;  (let [epoch (:epoch event)
  ;        sub-accum (get acc-map epoch #{})]
  ;    (assoc acc-map epoch (helper sub-accum event))))
  ;
  ;
  ;(def a {})
  ;(def e (sort-by :epoch events))
  ;
  ;(f a (first e))
  ;(f (f a (first e)) (second e))
  ;(f (f (f a (first e)) (second e)) (nth e 3))
  ;
  ;(defn single-pass-accum-events [a e]
  ;  (loop [event e
  ;         accum a]
  ;    (if (empty? event)
  ;      accum
  ;      (recur (f accum (first event)) (rest event)))))
  ;
  ;
  ;(sort (single-pass-accum-events {} events))
  ;; well... not really. this only accumulates each epoch, not ACROSS epochs
  ;; which is what we need







  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;
  ; POSSIBLE ABANDONED CODE BELOW
  ;
  ; DANGER DANGER DANGER
  ;

  ; so by here we have all the 'events', now we need to 'reduce' all the events into
  ; 'materialized views' for each epoch

  ;; get all the events for an epoch
  ;(first epoch-list)
  ;(count epoch-list)
  ;
  ;; get all events for a given epoch (add or remove)
  ;(filter #(= (first epoch-list) (:epoch %)) events)
  ;
  ;; gather all the events for each epoch
  ;(map (fn [e]
  ;       {:epoch e
  ;        :data  (filter #(= e (:epoch %)) events)})
  ;  epoch-list)
  ;
  ;(def epoch-events (map (fn [e]
  ;                         {:epoch e
  ;                          :data  (filter #(= e (:epoch %)) events)})
  ;                    epoch-list))
  ;
  ;(first epoch-events)
  ;(count epoch-events)
  ;
  ;
  ;(= (-> epoch-events first :data first :data)
  ;  (-> epoch-events first :data second :data))

  ;; some experiments with 'set'
  ;(conj {} (-> epoch-events first :data first :data))
  ;(conj
  ;  (conj #{}
  ;    (-> epoch-events first :data first :data))
  ;  (-> epoch-events first :data second :data))             ; these both show because they are different (see :xx-beam)
  ;
  ;(filter #(= (first epoch-list) (:epoch %)) events)
  ;
  ;(for [event (filter #(= (first epoch-list) (:epoch %)) events)]
  ;  (condp = (:event event)
  ;    :add (conj #{} (:data event))
  ;    :remove (disj #{} (:data event))))
  ;
  ;
  ;
  ;; loop/recur over 'accumulate-events' as a reduce...
  ;
  ;(loop [x 10]
  ;  (when (> x 1)
  ;    (println x)
  ;    (recur (- x 2))))
  ;
  ;(loop [x 50
  ;       y 10]
  ;  (when (> x 1)
  ;    (println x)
  ;    (recur (- x 2 y) (dec y))))
  ;
  ;(loop [n (bigint 5), accumulator 1]
  ;  (if (zero? n)
  ;    accumulator                                           ; we're done
  ;    (recur (dec n) (* accumulator n))))
  ;

  (defn accumulate-events* [accum event]
    (prn "accum" event)
    (condp = (:event event)
      :add (conj accum (:data event))
      :remove (disj accum (:data event))))

  (accumulate-events* #{} (first events))
  (accumulate-events* #{} (second events))
  (map #(accumulate-events* #{} %) events)


  (accumulate-events* #{} (-> epoch-events first :data first))
  (accumulate-events* #{} (-> epoch-events first :data second))
  (map #(accumulate-events* #{} %) (-> epoch-events first :data))

  ; lets work the 'outermost' recursion first, the one that processes a single epoch
  (defn accumulate-epoch [accum evt]
    (loop [a accum
           e evt]
      (prn "recur" a)
      (if (empty? e)
        a
        (recur (accumulate-events* a (first e)) (rest e)))))

  (def accum #{})
  (def evt (-> epoch-events first :data))
  (first evt)
  (rest evt)
  (empty? (rest (rest evt)))
  (accumulate-epoch #{} (-> epoch-events first :data))
  (accumulate-epoch #{} (-> epoch-events second :data))

  (accumulate-epoch
    (accumulate-epoch #{} (-> epoch-events first :data))
    (-> epoch-events second :data))

  ; so far, so good

  ; now let's accumulate all the epochs
  (def acc-map {})
  (def epochs (take 2 epoch-list))

  (defn find-events [epoch events]
    (filter #(= epoch (:epoch %)) events))

  (defn accumulate-epochs [acc-map epochs events]
    (loop [a   acc-map
           e   epochs
           evt events]
      (prn "acc-epochs" a)
      (if (empty? e)
        a
        (recur
          (assoc a (first e)
            (merge
              (get a (first e))
              (accumulate-epoch (get a (first e)) (find-events (first e) evt))))
          (rest e)
          evt))))




  ;(defn inner [evt a]
  ;  (loop [event evt
  ;         accum a]
  ;    (if (empty? event)
  ;      accum
  ;      (recur
  ;        (rest event)
  ;        (accumulate-events* accum (first event))))))
  ;
  ;
  ;(defn outer [e-l a events]
  ;  (loop [epoch e-l
  ;         accum a]
  ;    (if (empty? epoch)
  ;      accum
  ;      (recur
  ;        (rest epoch)
  ;        (inner accum
  ;          (filter #(= (first epoch) (:epoch %)) events))))))
  ;
  ;(outer (take 2 epoch-list) #{} events)
  ;
  ;(loop [epoch (take 2 epoch-list)
  ;       accum #{}]
  ;  (if (empty? epoch)
  ;    accum
  ;    (recur
  ;      (rest epoch)
  ;      (conj
  ;        accum
  ;        (filter #(= (first epoch) (:epoch %)) events)))))
  ;
  ;
  ;
  ;
  ;
  ;
  ;
  ;
  ;
  ;(def one-epoch (first epoch-list))
  ;(def accum #{})
  ;(filter #(= one-epoch (:epoch %)) events)
  ;
  ;(accumulate-events* #{} (-> epoch-events first :data first))
  ;(accumulate-events* #{} (-> epoch-events first :data second))
  ;(accumulate-events*
  ;  (accumulate-events* #{} (-> epoch-events first :data first))
  ;  (-> epoch-events first :data second))
  ;
  ;
  ;(defn accumulate-events [events accum one-epoch]
  ;  (apply merge
  ;    (map #(accumulate-events* accum %)
  ;      (filter #(= one-epoch (:epoch %)) events))))
  ;
  ;(accumulate-events events #{} one-epoch)
  ;
  ;











  ())


; lets start from scratch with a simpler situation
;
(def events [{:epoch 1 :event :add :data "Alpha"}
             {:epoch 1 :event :add :data "Beta"}
             {:epoch 2 :event :add :data "Delta"}
             {:epoch 3 :event :add :data "Gamma"}
             {:epoch 4 :event :remove :data "Alpha"}
             {:epoch 4 :event :add :data "Epsilon"}
             {:epoch 5 :event :remove :data "Beta"}
             {:epoch 6 :event :remove :data "Epsilon"}])

; so in the end we should have
;
(def state-map {1 #{"Alpha" "Beta"}
                2 #{"Alpha" "Beta" "Delta"}
                3 #{"Alpha" "Beta" "Delta" "Gamma"}
                4 #{"Beta" "Delta" "Gamma" "Epsilon"}
                5 #{"Delta" "Gamma" "Epsilon"}
                6 #{"Delta" "Gamma"}})

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
(def empty-state-map (zipmap epochs (repeat #{})))


(defn apply-event [accum event]
  (condp = (:event event)
    :add (conj accum (:data event))
    :remove (disj accum (:data event))))

;(first events)
;(rest events)
;(count events)
;
;(def epoch-1 (apply-event #{} (nth events 0)))
;(def epoch-1 (apply-event #{"Alpha"} (nth events 1)))
;(def epoch-2 (apply-event #{"Alpha" "Beta"} (nth events 2)))
;(def epoch-3 (apply-event #{"Alpha" "Beta" "Delta"} (nth events 3)))
;(def epoch-4 (apply-event #{"Delta" "Alpha" "Gamma" "Beta"} (nth events 4)))
;(def epoch-4 (apply-event #{"Delta" "Gamma" "Beta"} (nth events 5)))
;(def epoch-5 (apply-event #{"Epsilon" "Delta" "Gamma" "Beta"} (nth events 6)))
;(def epoch-6 (apply-event #{"Epsilon" "Delta" "Gamma"} (nth events 7)))
;
;(def hand-built-state-map {1 epoch-1
;                           2 epoch-2
;                           3 epoch-3
;                           4 epoch-4
;                           5 epoch-5
;                           6 epoch-6})
;(= hand-built-state-map state-map)                          ; GOOD!

; now we need ot do it with a function(s)


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
(def state-atom (atom empty-state-map))

(let [last-accum (atom #{})]
  (for [epoch (keys @state-atom)]
    (let [current-accum (apply-epoch-events @last-accum (events-for events epoch))]
      (prn @last-accum "/" current-accum)
      (swap! state-atom assoc epoch current-accum)
      (reset! last-accum current-accum))))



(defn apply-events [state-map events]
  (let [last-accum (atom #{})]
    (for [epoch (keys @state-atom)]
      (let [current-accum (apply-epoch-events @last-accum (events-for events epoch))]
        (prn @last-accum "/" current-accum)
        (swap! state-atom assoc epoch current-accum)
        (reset! last-accum current-accum)))))

(apply-events state-atom events)














