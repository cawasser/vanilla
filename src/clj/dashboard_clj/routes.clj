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
             {:version (version/get-version
                         "vanilla"
                         "vanilla"
                         "version number not found")
              :status 200})})

  (GET "/services" _
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "get services")
             {:services (h/get-services) :status 200})})

;;;;; Layout Saving Routes

  (GET "/layout" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "get layout")
             {:layout (h/get-layout (get-in req [:params :username])) :status 200})})

  (POST "/save-layout" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "save layout")
             {:widget-save (h/save-layout (get-in req [:params :widgets])) :status 200})})

  (POST "/update-widget" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "updating one widget" (get-in req [:params :widget]))
             {:widget-update (h/update-widget (get-in req [:params :widget])) :status 200})})

  (POST "/delete-widget" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body (do
             ;(prn "remove widget" (get-in req [:params :id]))
             {:delete-widget (h/delete-widget (get-in req [:params :id])) :status 200})})

;;;;;; User Login Routes

  (GET "/verify-user" req
    {:status 200
     :headers {"Content-Type" "text/json; charset=utf-8"}
     :body  (do
             ;(prn "verify user " (:params req))
             ;(prn (str (h/verify-user-password (:params req))))
             {:verified-user (h/verify-user-password (:params req)) :status 200})})


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
            {:rows-updated (h/create-user (:params req)) :status 200})})                            ;;Expects a struct of username password



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
