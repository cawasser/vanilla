(ns dashboard-clj.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
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
     :body (do
             (prn "get version")
             (str (json/write-str {:version (version/get-version
                                              "vanilla"
                                              "vanilla"
                                              "version number not found")})))})

  (GET "/services" _
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             (prn "get services")
             (str (json/write-str {:services (h/get-services)})))})


  ;TODO test removing the write-str here and put the de-string back in url handler
  (GET "/layout" _
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             (prn "get layout")
             (str (json/write-str {:layout (h/get-layout)})))})


  (GET "/verify-user" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body  (do
              (prn "verify user " (:params req))
              {:verified-user (h/verify-user-password (:params req))})})


  (GET "/return-all-users" _
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             (prn "get all users")
             (prn (h/get-all-users))
             (h/get-all-users))})

  (POST "/create-new-user" req
    {:status 200
     ;:headers {"Content-Type" "text/json; charset=utf-8"}    ;; Not sure if this is better than line below it
     :header { "Accept" "application/json"
              :content-type "application/json;charset=utf-8"} ;;Not sure if this does anything
     :body (do
             (prn "create new user " (:params req))         ;; This keeps req in clojure format (keywords values)
             (prn "create new user " (:json-params req))    ;; This turns req into json strings
             {:rows-updated (h/create-user (:params req))})})                            ;;Expects a struct of username password

  (POST "/save-layout" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}   ;maybe convert from text-json to straight edn
     ;pull apart parameters and strip the keys off the values for the DB call
     :body (do
             (prn "save layout " req)
             (h/save-layout (:widgets (:params req))))})  ; this is way off, what is this body even needed for?





  (resources "/"))




(defn ->http-handler [ring-ajax-post ring-ajax-get-or-ws-handshake]
  (-> routes
      (compojure.core/routes
       (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
       (POST "/chsk" req (ring-ajax-post req))

       (GET "/version" req (ring-ajax-get-or-ws-handshake req)))
      (wrap-defaults api-defaults)

      wrap-json-response                                  ;;Adding this did nothing
      wrap-json-params                                    ;;Adding this did nothing

      ;wrap-with-logger
      wrap-gzip))
