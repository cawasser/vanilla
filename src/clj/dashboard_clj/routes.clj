(ns dashboard-clj.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [clojure.data.json :as json]

            ; version number support
            [trptcolin.versioneer.core :as version]

            [vanilla.url-handlers :as h])
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

  (GET "/services" _
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (str (json/write-str {:services (h/get-services)}))})


  ;TODO test removing the write-str here and put the de-string back in url handler
  (GET "/layout" _
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (str (json/write-str {:layout (h/get-layout)}))})

  (POST "/save-layout" req
    {:status 200
     :parameters {:body {:request string?}}
     :responses {200 {:body {:result string?}}}
     :headers {"Content-Type" "text/json; charset=utf-8"}   ;maybe convert from text-json to straight edn
     ;pull apart parameters and strip the keys off the values for the DB call
     :body (str (json/write-str {:layout (h/save-layout parameters)}))})

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
