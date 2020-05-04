(ns dashboard-clj.components.websocket
  (:require [com.stuartsierra.component :as component]
    [taoensso.sente :as sente]
    [clojure.core.async :as async]
    [dashboard-clj.data-source :as ds]))

(defmulti -client-ev-handler (fn [_ y] (:id y)))

(defn client-ev-handler [data-sources ev-msg]
  (-client-ev-handler data-sources ev-msg))

(defmethod -client-ev-handler :default [sources {:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid (:uid session)]
  (println "un handled client event" uid
           "////" id
           "////" event
           "////" ?data)))

;(defmethod -client-ev-handler :chsk/uidport-open
;  [uid]
;  (prn "UID ***** OPEN: " uid )) ;" //// " event " //// " ?data " //// " id))
;
;(defmethod -client-ev-handler :chsk/ws-ping
;  [id]
;  (prn "Pinged from ws: " id ))  ;" uid " uid " event " event))

(defmethod -client-ev-handler :dashboard-clj.core/sync
  [{:as ctx :keys [data-sources chsk-send!]} {:as ev-msg :keys [?reply-fn ch-recv client-id connected-uids uid event id ring-req ?data send-fn]}]
  (doseq [event (map #(ds/data->event (:name %) (deref (:data %)))
                  data-sources)]
    (prn "Dashboard/sync: " ev-msg
         "////" chsk-send!)
    (chsk-send! uid event)))  ;:sente/all-users-without-uid




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
            ;(prn "sending " event " to " cid)
            (send-fn cid event))
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


  ()
  )
