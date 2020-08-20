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


;(def from-sqlite {:id :key :data_source :data-source :data_grid :data-grid})
;(def to-sqlite {:key :id :data-source :data_source :data-grid :data_grid})

;;;;;;;;;;
;; Layouts

; fixup the key changes caused by SQLite with rename-keys
; :id -> :key
; :data_source -> :data-source
; :data_grid -> :data-grid

(defn get-layout
  "Retrieves a users layout from the db"
  [user]
  (db/get-user-layout db/vanilla-db {:username user}))


(defn save-layout
  "Saves a users entire widget layout to the db"
  [new-layout]
  (log/info "SAVE-LAYOUT Handler(not ordered): " new-layout)
  ;read in the stringed json, order the maps by keys, then remove all keys
  (let [ordered (map #(into (sorted-map) %) (clojure.core/read-string new-layout))
        sorted (apply #(into [] %) ordered)
        sorted-str (map #(str (second %)) sorted)
        vec-str (into [] sorted-str)]
    (log/info "SAVE-LAYOUT Handler(ordered): " ordered)
    (log/info "SAVE-LAYOUT Handler(string): " sorted)
    (log/info "SAVE-LAYOUT Handler(sorted-str): " sorted-str)
    (log/info "SAVE-LAYOUT Handler(vec-str): " vec-str)
    (db/save-layout! db/vanilla-db {:layout [vec-str]})))


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










  (apply #(str %) sorted-vals)
  (apply str sorted-vals)
  (map #(into [] %) sorted-vec)
  (map #(str "*" % "*") (get-in sorted-vec))





  (def inter (clojure.string/join "/n" sorted-vals))
  (clojure.string/join "\n"
        (clojure.string/split inter #"\s"))
  (clojure.string/split (map #(str %) sorted-vec) " ")

  (organize-layout layout1)

  ;(defn organize-layout
  ;  [old-layout]
  ;  {:id          (:id layout)
  ;   :username    (:username layout)
  ;   :name        (:name layout)
  ;   :ret_types   (:ret_types layout)
  ;   :basis       (:basis layout)
  ;   :data_source (:data_source layout)
  ;   :type        (:type layout)
  ;   :icon        (:icon layout)
  ;   :label       (:label layout)
  ;   :data_grid   (:data_grid layout)
  ;   :options     (:options layout)})

  (def proper
    {:id          "\"123\""
     :username    "\"APaine\""
     :name        :area-widget
     :ret_types   [:data-format/x-y]
     :basis       :chart
     :data_source :spectrum-traces
     :type        :area-chart
     :icon        "\"/images/area-widget.png\""
     :label       "\"Area\""
     :data_grid   {:x 0 :y 0 :w 4 :h 14}
     :options     {:viz/style-name        "widget"
                   :viz/y-title           "power"
                   :viz/x-title           "frequency"
                   :viz/allowDecimals     false
                   :viz/banner-color      {:r 0x00 :g 0x00 :b 0xff :a 1}
                   :viz/tooltip           {:followPointer true}
                   :viz/title             "Channels (area)"
                   :viz/banner-text-color {:r 255, :g 255, :b 255, :a 1}
                   :viz/animation         false}})
  (proper :id)
  (:id proper)
  (into {} (map #(clojure.core/read-string %) proper))

  ())





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

  (get-all-users))


