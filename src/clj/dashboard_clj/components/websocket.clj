(ns dashboard-clj.components.websocket
  (:require [com.stuartsierra.component :as component]
    [taoensso.sente :as sente]
    [clojure.tools.logging :as log]
    [clojure.core.async :as async]
    [vanilla.subscription-manager :as subman]
     [dashboard-clj.components.system :as system]
    [dashboard-clj.data-source :as ds]))

(defmulti -client-ev-handler (fn [_ y] (:id y)))

(defn client-ev-handler [sources ev-msg]
  (-client-ev-handler sources ev-msg))

(defmethod -client-ev-handler :default
  [sources {:as ev-msg :keys [?reply-fn ch-recv client-id connected-uids uid event id ring-req ?data send-fn]}]
  (case id
    :chsk/uidport-open  (do
                          (log/info "Port open to UID: " uid)
                          (subman/add-empty-user uid))          ;; when new user connection opens, create a subscription for them
    :chsk/uidport-close (do
                          (log/info "Port closed to UID: " uid)
                          (subman/remove-user uid))         ;; when connection closes, cleanup users subscriptions.
    :chsk/ws-ping       (log/info "Websocket ping")
    (println "un-handled client event" id)))


(defmethod -client-ev-handler :dashboard-clj.core/sync
  [{:as ctx :keys [data-sources chsk-send!]} {:as ev-msg :keys [?reply-fn ch-recv client-id connected-uids uid event id ring-req ?data send-fn]}]
  (doseq [event (map #(ds/data->event (:name %) (deref (:data %))) data-sources)]
    (when (some #(= (first (second event)) %) (subman/get-subbed-sources uid))
      (log/info "Dashboard/sync event sending: " (get-in (second (second event)) [:data :title]) " to UID: " uid)
      (chsk-send! uid event))))      ;; chsk-send! and send-fn are the same here, dont think it matters which we use




(defrecord WebSocketServer [data-sources handler webserver-adapter options ring-ajax-post ring-ajax-get-or-ws-handshake ch-recv chsk-send! connected-uids router]
  component/Lifecycle
  (start [component]

    (let [{:keys [ch-recv send-fn ajax-post-fn
                  ajax-get-or-ws-handshake-fn connected-uids]} (sente/make-channel-socket-server! webserver-adapter options)
          ch-out (async/chan (async/sliding-buffer 1000))
          mix-out (async/mix ch-out)]

      (doseq [in-chan (map #(.output-chan %) data-sources)]
        (async/admix mix-out in-chan))

      (async/go-loop []
        (let [event (async/<! ch-out)]
          (doseq [cid (:any @connected-uids)]
            (when (some #(= (first (second event)) %) (subman/get-subbed-sources cid))
              ;(prn "Sending " (first (second event)) " to " cid "from channel" ch-out)
              (send-fn cid event)))
          (recur)))

      (assoc component
        :ring-ajax-post ajax-post-fn
        :ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
        :ch-recv ch-recv
        :chsk-send! send-fn
        :connected-uids connected-uids
        :router (sente/start-chsk-router! ch-recv
                  (partial handler {:data-sources data-sources
                                    :chsk-send!   send-fn})))))

  (stop [component]
    (when router
      (router)
      (assoc component
        :ring-ajax-post nil
        :ring-ajax-get-or-ws-handshake nil
        :ch-recv nil
        :chsk-send! nil
        :connected-uids nil
        :router nil))))

(defn new-websocket-server [data-sources webserver-adapter options]
  (map->WebSocketServer {:data-sources      data-sources
                         :handler           client-ev-handler
                         :webserver-adapter webserver-adapter
                         :options           options}))


(comment

  ;; Leaving this large rich comment block here to allow anyone doing future work on this websocket to easily
  ;; see what the contents of the maps are that come in from the websocket events, as well as be able to test
  ;; functions on them from the repl without needing to add prints back in and re-parse large console outputs.
  ;;
  ;; the names match up the variable names being used in the handler functions above.

  (def ev-msg

  {:?reply-fn nil,
   :ch-recv [clojure.core.async.impl.channels.ManyToManyChannel 0x6fd7dd4a "clojure.core.async.impl.channels.ManyToManyChannel@6fd7dd4a"],
   :client-id "3ca8d589-fe0a-4742-a9b1-76b7861c9320",
   :connected-uids [clojure.lang.Atom 0x202d24fb {:status :ready,
                                                         :val {:ws #{:taoensso.sente/nil-uid},
                                                               :ajax #{},
                                                               :any #{:taoensso.sente/nil-uid}}}],
   :uid :taoensso.sente/nil-uid,
   :event [:dashboard-clj.core/sync],
   :id :dashboard-clj.core/sync,
   :ring-req {:remote-addr "0:0:0:0:0:0:0:1",
              :params {:client-id "3ca8d589-fe0a-4742-a9b1-76b7861c9320"},
              :route-params {},
              :headers {"origin" "http://localhost:5000",
                        "host" "localhost:5000",
                        "upgrade" "websocket",
                        "user-agent" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.113 Safari/537.36",
                        "connection" "Upgrade",
                        "pragma" "no-cache",
                        "sec-websocket-key" "Kco5vxZ+I9KeU7JfFRS1Nw==",
                        "accept-language" "en-US,en;q=0.9",
                        "sec-websocket-version" "13",
                        "accept-encoding" "gzip, deflate, br",
                        "sec-websocket-extensions" "permessage-deflate; client_max_window_bits",
                        "cache-control" "no-cache"},
              :async-channel [org.httpkit.server.AsyncChannel 0x71c18039 "/0:0:0:0:0:0:0:1:5000<->/0:0:0:0:0:0:0:1:55471"],
              :server-port 5000,
              :content-length 0,
              :form-params {},
              :compojure/route [:get "/chsk"],
              :websocket? true,
              :query-params {"client-id" "3ca8d589-fe0a-4742-a9b1-76b7861c9320"},
              :content-type nil,
              :character-encoding "utf8",
              :uri "/chsk",
              :server-name "localhost",
              :query-string "client-id=3ca8d589-fe0a-4742-a9b1-76b7861c9320",
              :body nil,
              :scheme :http,
              :request-method :get},
   :?data nil,
   :send-fn [taoensso.sente$make_channel_socket_server_BANG_$send_fn__25287 0x4002dbb4 "taoensso.sente$make_channel_socket_server_BANG_$send_fn__25287@4002dbb4"]})


  ;;dashboard core sync

  ;(def ctx
  ;  {:data-sources (#dashboard_clj.data_source.DataSource{:name :usage-data,
  ;                                                        :read-fn :vanilla.usage-data-service/fetch-data,
  ;                                                        :params nil,
  ;                                                        :schedule nil,
  ;                                                        :data #object[clojure.lang.Atom 0x7601f8e1 {:status :ready,
  ;                                                                                                    :val {:title "Usage Data",
  ;                                                                                                          :data-format :data-format/label-y,
  ;                                                                                                          :src/x-title "Fruit",
  ;                                                                                                          :src/y-title "Qty.",
  ;                                                                                                          :series [{:keys ["name" "y"],
  ;                                                                                                                    :data [["Apples" 86.5233863614918]
  ;                                                                                                                           ["Pears" 95.2867326648154]
  ;                                                                                                                           ["Oranges" 69.16955373938431]
  ;                                                                                                                           ["Plums" 25.517902763676116]
  ;                                                                                                                           ["Bananas" 79.40914129582353]
  ;                                                                                                                           ["Peaches" 30.47924606668271]
  ;                                                                                                                           ["Prunes" 22.77266086451992]
  ;                                                                                                                           ["Avocados" 87.56229626275015]]}]}}],
  ;                                                        :output-chan #object[clojure.core.async.impl.channels.ManyToManyChannel 0x33036845 "clojure.core.async.impl.channels.ManyToManyChannel@33036845"]}
  ;                   #dashboard_clj.data_source.DataSource{:name :carousel-service,
  ;                                                         :read-fn :vanilla.carousel-service/fetch-data,
  ;                                                         :params nil,
  ;                                                         :schedule nil,
  ;                                                         :data #object[clojure.lang.Atom 0x3d600af {:status :ready,
  ;                                                                                                    :val {:title "carousel data",
  ;                                                                                                          :data-format
  ;                                                                                                                 :data-format/carousel,
  ;                                                                                                          :data "heatmap-data"}}],
  ;                                                         :output-chan #object[clojure.core.async.impl.channels.ManyToManyChannel 0x2fabf39 "clojure.core.async.impl.channels.ManyToManyChannel@2fabf39"]}),
  ;   :chsk-send! #object[taoensso.sente$make_channel_socket_server_BANG_$send_fn__25287 0x604c36b8 "taoensso.sente$make_channel_socket_server_BANG_$send_fn__25287@604c36b8"]} )
  ;
  ;
  (def event
    [:data-source/event [:carousel-service {:data
                                            {:title "carousel data",
                                             :data-format :data-format/carousel,
                                             :data "heatmap-data"}}]])

  ;(def data-sources
  ;  (#dashboard_clj.data_source.DataSource{:name :usage-data,
  ;                                         :read-fn :vanilla.usage-data-service/fetch-data,
  ;                                         :params nil,
  ;                                         :schedule nil,
  ;                                         :data #object[clojure.lang.Atom 0x7601f8e1 {:status :ready,
  ;                                                                                     :val {:title "Usage Data",
  ;                                                                                           :data-format :data-format/label-y,
  ;                                                                                           :src/x-title "Fruit",
  ;                                                                                           :src/y-title "Qty.",
  ;                                                                                           :series [{:keys ["name" "y"],
  ;                                                                                                     :data [["Apples" 86.5233863614918]
  ;                                                                                                            ["Pears" 95.2867326648154]
  ;                                                                                                            ["Oranges" 69.16955373938431]
  ;                                                                                                            ["Plums" 25.517902763676116]
  ;                                                                                                            ["Bananas" 79.40914129582353]
  ;                                                                                                            ["Peaches" 30.47924606668271]
  ;                                                                                                            ["Prunes" 22.77266086451992]
  ;                                                                                                            ["Avocados" 87.56229626275015]]}]}}],
  ;                                         :output-chan #object[clojure.core.async.impl.channels.ManyToManyChannel 0x33036845 "clojure.core.async.impl.channels.ManyToManyChannel@33036845"]}
  ;    #dashboard_clj.data_source.DataSource{:name :carousel-service,
  ;                                          :read-fn :vanilla.carousel-service/fetch-data,
  ;                                          :params nil, :schedule nil,
  ;                                          :data #object[clojure.lang.Atom 0x3d600af {:status :ready,
  ;                                                                                     :val {:title "carousel data",
  ;                                                                                           :data-format :data-format/carousel,
  ;                                                                                           :data "heatmap-data"}}],
  ;                                          :output-chan #object[clojure.core.async.impl.channels.ManyToManyChannel 0x2fabf39 "clojure.core.async.impl.channels.ManyToManyChannel@2fabf39"]}))



  ()
  )
