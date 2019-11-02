(ns dashboard-clj.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [clojure.data.json :as json]

            ; version number support
            [trptcolin.versioneer.core :as version])
  (:gen-class))


(defroutes routes
  (GET "/" _
       {:status 200
        :headers {"Content-Type" "text/html; charset=utf-8"}
        :body (io/input-stream (io/resource "public/index.html"))})

  (GET "/version" _
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (str (json/write-str {:version (version/get-version
                                            "vanilla"
                                            "vanilla"
                                            "version number not found")}))})

  (resources "/"))




(defn ->http-handler [ring-ajax-post ring-ajax-get-or-ws-handshake]
  (-> routes
      (compojure.core/routes
       (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
       (POST "/chsk" req (ring-ajax-post req))

       (GET "/version" req (ring-ajax-get-or-ws-handshake req)))
      (wrap-defaults api-defaults)
      wrap-with-logger
      wrap-gzip))
