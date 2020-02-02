(ns dashboard-clj.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
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
             ;(prn "get services")
             (str (json/write-str {:services (h/get-services)})))})

;;;;; Layout Saving Routes

  ;TODO test removing the write-str here and put the de-string back in url handler
  (GET "/layout" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "get layout")
             (str (json/write-str {:layout (h/get-layout (get-in req [:params :username]))})))})

  (POST "/save-layout" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "save layout")
             {:widget-save (h/save-layout (get-in req [:params :widgets]))})})

  (POST "/update-widget" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "updating one widget" (get-in req [:params :widget]))
             {:widget-update (h/update-widget (get-in req [:params :widget]))})})

  (POST "/delete-widget" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "remove widget" (get-in req [:params :id]))
             {:delete-widget (h/delete-widget (get-in req [:params :id]))})})

;;;;;; User Login Routes

  (GET "/verify-user" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body  (do
             ;(prn "verify user " (:params req))
             ;(prn (str (h/verify-user-password (:params req))))
             {:verified-user (h/verify-user-password (:params req))})})


  (GET "/return-all-users" _
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
            ;(prn "get all users")
            ;(prn (h/get-all-users))
            (h/get-all-users))})

  (POST "/create-new-user" req
    {:status 200
     :header {"Content-Type" "text/json; charset=utf-8"}
     :body (do
            ;(prn "create new user " (:params req))         ;; This keeps req in clojure format (keywords values)
            ;(prn "create new user " (:json-params req))    ;; This turns req into json strings
            {:rows-updated (h/create-user (:params req))})})                            ;;Expects a struct of username password



  (resources "/"))




(defn ->http-handler [ring-ajax-post ring-ajax-get-or-ws-handshake]
  (-> routes
      (compojure.core/routes
       (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
       (POST "/chsk" req (ring-ajax-post req))

       (GET "/version" req (ring-ajax-get-or-ws-handshake req)))
      (wrap-defaults api-defaults)
      (wrap-json-response)
      (wrap-keyword-params)
      (wrap-json-params)
      ;wrap-with-logger
      wrap-gzip))
