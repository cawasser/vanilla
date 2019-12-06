(defproject vanilla "0.2.2-SNAPSHOT"
  :description "Vanilla Dashboard - a simple dashboard built on dashboard-clj"
  :url ""
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.40" :scope "provided"]
                 [environ "1.0.2"]

                 ;for dashboard-clj
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [bk/ring-gzip "0.1.1"]
                 [ring.middleware.logger "0.5.0"]
                 [org.immutant/scheduling "2.1.3"]
                 [com.taoensso/sente "1.8.1"]
                 [com.stuartsierra/component "0.3.1"]
                 [reagent "0.6.0-alpha"]
                 [re-frame "0.7.0"]
                 [cljsjs/react-grid-layout "0.12.4-0"]
                 [http-kit "2.1.19"]
                 [compojure "1.5.0"]

                 ; get version data
                 [trptcolin/versioneer "0.2.0"]
                 [cljs-ajax "0.7.2"]

                 [org.clojure/core.match "0.3.0"]]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild" "resources/public/libs"]
  :target-path "target/%s/"

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js"]

  :uberjar-name "vanilla.jar"

  ;; Use `lein run` if you just want to start a HTTP server, without figwheel
  :main vanilla.server

  ;; nREPL by default starts in the :main namespace, we want to start in `user`
  ;; because that's where our development helper functions like (run) and
  ;; (browser-repl) live.
  :repl-options {:init-ns user}

  :plugins [[lein-cljsbuild "1.1.7"]]

  :cljsbuild {:builds
              {:app
               {:source-paths ["src/cljs"]

                :figwheel     true
                ;; Alternatively, you can configure a function to run every time figwheel reloads.
                ;; :figwheel {:on-jsload "vanilla.core/on-figwheel-reload"}

                :compiler     {:main                 vanilla.core
                               :externs              ["externs.js"]
                               :asset-path           "js/compiled/out"
                               :output-to            "resources/public/js/compiled/app.js"
                               :output-dir           "resources/public/js/compiled/out"
                               :source-map-timestamp true}}}}

  ;; When running figwheel from nREPL, figwheel will read this configuration
  ;; stanza, but it will read it without passing through leiningen's profile
  ;; merging. So don't put a :figwheel section under the :dev profile, it will
  ;; not be picked up, instead configure figwheel here on the top level.

  :figwheel {;; :http-server-root "public"       ;; serve static assets from resources/public/
             :server-port    3469                           ;; default
             ;; :server-ip "127.0.0.1"           ;; default
             :css-dirs       ["resources/public/css"]       ;; watch and update CSS

             ;; Instead of booting a separate server on its own port, we embed
             ;; the server ring handler inside figwheel's http-kit server, so
             ;; assets and API endpoints can all be accessed on the same host
             ;; and port. If you prefer a separate server process then take this
             ;; out and start the server with `lein run`.

             ;; Start an nREPL server into the running figwheel process. We
             ;; don't do this, instead we do the opposite, running figwheel from
             ;; an nREPL process, see
             ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
             ;; :nrepl-port 7888

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             :server-logfile "log/figwheel.log"}

  :doo {:build "test"}

  :profiles {:dev     {:source-paths ["dev"]
                       :dependencies [[binaryage/devtools "0.9.10"]
                                      [figwheel "0.5.2"]
                                      [figwheel-sidecar "0.5.2"]
                                      [pjstadig/humane-test-output "0.10.0"]
                                      [prone "2019-07-08"]
                                      [com.cemerick/piggieback "0.2.2"]
                                      [org.clojure/tools.nrepl "0.2.13"]
                                      [org.clojure/tools.namespace "0.3.1"]
                                      [org.clojure/java.classpath "0.3.0"]]

                       :plugins      [[lein-figwheel "0.5.2"]
                                      [lein-doo "0.1.6"]]

                       :cljsbuild    {:builds
                                      {:test
                                       {:source-paths ["src/cljs" "test/cljs"]
                                        :compiler     {:output-to     "resources/public/js/compiled/testable.js"
                                                       :main          vanilla.test-runner
                                                       :optimizations :none}}}}}

             :uberjar {:omit-source  true
                       :prep-tasks   ["compile" ["cljsbuild" "once" "min"]]
                       :cljsbuild    {:builds
                                      {:min
                                       {:source-paths ["src/cljc" "src/cljs"]
                                        :compiler     {:output-dir       "target/cljsbuild/public/js/compiled"
                                                       :output-to        "target/cljsbuild/public/js/compiled/app.js"
                                                       :source-map       "target/cljsbuild/public/js/compiled/app.js.map"
                                                       :optimizations    :advanced
                                                       :pretty-print     false
                                                       :externs          ["externs.js"]
                                                       :infer-externs    true
                                                       :closure-warnings {:externs-validation :off
                                                                          :non-standard-jsdoc :off}}}}}

                       :aot          :all
                       :uberjar-name "vanilla.jar"}})
