(ns vanilla.url-handlers
  (:require
    [vanilla.db.core :as db]
    [vanilla.subscription-manager :as subman]
    [clojure.core]
    [clojure.tools.logging :as log]))


;;;;;;;;;;;;;;;;
;
; HTML endpoint handlers

(defn get-services
  "Retrieves full service list from the db"
  []
  (db/get-services db/vanilla-db))

(defn subscribe-to-services
  "Handles subscribing a user to the vector of data sources currently on their dashboard"
  [user services]
  (if (some? user)        ;; TODO: This nil check can likely be removed once we add support for multi-data-source widget subscribes
    (let [distinct-str (into [] (distinct (clojure.core/read-string services)))]
      ;(prn "URL handler subscribing: " distinct-str)
      (subman/add-subscribers user (subman/clean-sources distinct-str)))
    (log/info "Tried to subscribe a nil user to " services)))




;;;;;;;;;;
;; Layouts


; fixup the key changes caused by SQL with rename-keys
; :id -> key
; :data_source -> :data-source
; :data_grid -> :data-grid
(def from-sql {:id :key :data_source :data-source :data_grid :data-grid})
(def to-sql {:key :id :data-source :data_source :data-grid :data_grid})

(defn get-layout
  "Retrieves a users layout from the db"
  [user]
  (map #(clojure.set/rename-keys % from-sql) (db/get-user-layout db/vanilla-db {:username user})))


(defn save-layout
  "Saves a users entire widget layout to the db.
  Take in the new layout, and transformed the data into a workable format for the
  postgres database."
  ;@TODO - Clean up the let block? It is working and all steps are necessary - see comment block below
  [new-layout]
  ;read in the stringed json, order the maps by keys, then remove all keys
  (let [sql-ready (map #(clojure.set/rename-keys % to-sql) (clojure.core/read-string new-layout))
        ordered (map #(into (sorted-map) %) sql-ready)
        vec-ord (map #(into [] %)  ordered)
        fun (fn [thing] (mapv #(str (second %)) thing))
        sorted-vals (mapv #(fun %) vec-ord)]
    (db/save-layout! db/vanilla-db {:layout sorted-vals})))


; @TODO - this is no longer supported, we update all widgets at once
;(defn update-widget
;  "Updates a single widget for a user"
;  [widget]
;  (log/info widget)
;  (db/create-layout! db/vanilla-db (assoc (clojure.core/read-string widget) :username "testHuman")))

(defn delete-widget
  "Removes a widget by its uuid"
  [id]
  (db/delete-layout! db/vanilla-db {:id id}))


;;;;;;;
;; User

(defn create-user
  "Makes a call to the hugsql functions that create a user"
  [credentials]
  ;; credentials
  ;  (prn "uri handlers - " credentials)
  (db/create-new-user! db/vanilla-db credentials))

(defn verify-user-password
    "Take in a user and password and determine if they are in the database"
    [credentials]
    ;(prn "url handlers verify creds - " credentials)
    (some? (db/verify-credentials db/vanilla-db credentials)))

(defn get-requested-user
  "Makes a call to the hugsql functions that return a requested user if it exist"
  [username]
  (db/get-user  db/vanilla-db {:username username}))

(defn get-all-users
  "Returns all users in the db"
  []
  (db/get-users db/vanilla-db))



;;;;;;;;;;;;;;;;
;; RICH COMMENTS
;;
;;;;;;;;;;;;;;;;


;;;;;;;;
;; User
(comment

  (create-user
    {:username "chad-uri-handler"
     :pass "123"})

  (get-requested-user "chad")
  (get-requested-user "chad-uri-handler")

  (verify-user-password
    {:username "chad"
     :pass "123"})

  (verify-user-password
                      {:username "chad"
                       :pass "321"})

  (get-all-users)
  ())


;;;;;;;;;;
;; Layouts
;; - Mostly editing layout data into usable/parse-able data.
(comment

  (def layout1 "({:ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y], :key \"751fe45a-10b4-4788-a56d-2012681dd0b3\", :name :area-widget, :username \"chad\",
 :basis :chart, :data-source :spectrum-traces, :type :area-chart, :icon \"/images/area-widget.png\", :label \"Area\", :data-grid {:x 0, :y 0, :w 5, :h 15}, :options {:viz/style-name \"widget\", :viz/y-title \"p
ower\", :viz/x-title \"frequency\", :viz/allowDecimals false, :viz/banner-color {:r 0, :g 0, :b 255, :a 1}, :viz/tooltip {:followPointer true}, :viz/title \"Channels (area)\", :viz/banner-text-color {:r 255, :g
 255, :b 255, :a 1}, :viz/animation false}})")
  (def layout2 (str "(" {:ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y],
                         :key "97e96773-0e11-4dfd-9208-75b2b5699818",
                         :name :area-widget,
                         :username "chad",
                         :basis :chart,
                         :data-source :spectrum-traces,
                         :type :area-chart,
                         :icon "/images/area-widget.png",
                         :label "Area",
                         :data-grid {:x 0, :y 0, :w 5, :h 15},
                         :options {:viz/style-name "widget",
                                   :viz/y-title "power",
                                   :viz/x-title "frequency",
                                   :viz/allowDecimals false,
                                   :viz/banner-color {:r 0, :g 0, :b 255, :a 1},
                                   :viz/tooltip {:followPointer true},
                                   :viz/title "Channels (area)",
                                   :viz/banner-text-color {:r 255, :g 255, :b 255, :a 1},
                                   :viz/animation false}}
                    ")"))

  (def test (sorted-set layout2))
  (def sorted (mapv #(into (sorted-map) %) (clojure.core/read-string layout2)))
  sorted
  ;(def sorted-vals (map #(vals %) sorted))
  ;sorted-vals
  (def sorted-vec (apply #(into [] %) sorted))

  ;(def sorted-vec (map #(into [] %) sorted))
  sorted-vec
  (second sorted-vec)

  (def sorted-vals (map #(str (second %)) sorted-vec))
  sorted-vals
  (def val-vex (into [] sorted-vals))
  val-vex

  ())

;;;;;;;;;;;;;;;;;;;;;
;; Save-layout reform
; This goes through the transformation in save-layout, lots of data transmutations
(comment


  (def data "({:ret_types [:data-format/x-y :data-format/x-y-n :data-format/x-y-e :data-format/y], :key \"07f9d07a-d315-4459-80f3-5e93ea0b4350\", :name :area-widget, :username \"chad2\", :basis :chart, :data-source :spectrum-traces, :type :area-chart, :icon \"/images/area-widget.png\", :label \"Area\", :data-grid {:x 0, :y 0, :w 5, :h 15}, :options {:viz/style-name \"widget\", :viz/y-title \"power\", :viz/x-title \"frequency\", :viz/allowDecimals false, :viz/banner-color {:r 0, :g 0, :b 255, :a 1}, :viz/tooltip {:followPointer true}, :viz/title \"Channels (area)\", :viz/banner-text-color {:r 255, :g 255, :b 255, :a 1}, :viz/animation false}} {:ret_types [:data-format/x-y-n :data-format/x-y :data-format/x-y-e :data-format/y], :key \"46e31202-a746-43b4-af53-549833e5188e\", :name :bubble-widget, :username \"chad2\", :basis :chart, :data-source :bubble-service, :type :bubble-chart, :icon \"/images/bubble-widget.png\", :label \"Bubble\", :data-grid {:x 1, :y 15, :w 5, :h 15}, :options {:viz/title \"Bubble\", :viz/banner-color {:r 0, :g 100, :b 0, :a 1}, :viz/banner-text-color {:r 255, :g 255, :b 255, :a 1}, :viz/dataLabels true, :viz/labelFormat \"{point.name}\", :viz/lineWidth 0, :viz/animation false, :viz/data-labels true}})")

  (def sql (map #(clojure.set/rename-keys % to-sql) (clojure.core/read-string data)))
  sql

  (def sorted (map #(into (sorted-map) %) sql))
  sorted


  (def sorted-vec (map #(into [] %) sorted))
  sorted-vec
  ;(second sorted-vec)

  (defn fun
    "Pass in a thing, take the second value of it's vector2 and make it string"
    [thing]
    (mapv #(str (second %)) thing))

  (def sorted-vals (mapv #(fun %) sorted-vec))
  sorted-vals

  (db/save-layout! db/vanilla-db {:layout sorted-vals})


  ;
  ;(def vec-vals (mapv #(str %) sorted-vals))
  ;vec-vals




  ())



