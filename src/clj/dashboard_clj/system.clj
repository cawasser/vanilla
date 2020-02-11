(ns dashboard-clj.system
  (:require [dashboard-clj.routes :as routes]
            [dashboard-clj.components.webserver :as webserver]
            [dashboard-clj.components.scheduler :as scheduler]
            [dashboard-clj.components.websocket :as websocket]
            [taoensso.sente.server-adapters.http-kit      :refer [sente-web-server-adapter]]
            [com.stuartsierra.component :as component]
            [clojure.tools.nrepl.server :as nrepl]
            [vanilla.db.core :as db]))


(defn ->system [http-port nrepl nrepl-port data-sources dev-mode?]
  (if nrepl
    (do
      (prn "starting with nrepl")
      (component/system-map
       :websocket (websocket/new-websocket-server data-sources sente-web-server-adapter {})
       :server (component/using (webserver/new-webserver routes/->http-handler http-port) [:websocket])
       :scheduler (scheduler/new-scheduler data-sources)
       :nrepl (nrepl/start-server :port nrepl-port)
       :database (db/setup-database dev-mode?)))

    ; don't start an nrepl
    (component/system-map
      :websocket (websocket/new-websocket-server data-sources sente-web-server-adapter {})
      :server (component/using (webserver/new-webserver routes/->http-handler http-port) [:websocket])
      :scheduler (scheduler/new-scheduler data-sources)
      :database (db/setup-database dev-mode?))))



(defn start [http-port nrepl nrepl-port data-sources dev-mode?]
  (component/start (->system http-port nrepl nrepl-port data-sources dev-mode?)))
