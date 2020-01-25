(defproject vanilla "0.3.3-SNAPSHOT"
  :description "Vanilla Dashboard - a simple dashboard built on dashboard-clj"
  :url ""

  :dependencies [[ch.qos.logback/logback-classic "1.2.3"]
                 [cheshire "5.9.0"]
                 [cljs-ajax "0.8.0"]
                 [clojure.java-time "0.3.2"]
                 [com.cognitect/transit-clj "0.8.319"]
                 [conman "0.8.4"]

                 [day8.re-frame/http-fx "0.1.6"]
                 [cprop "0.1.15"]
                 [expound "0.8.2"]

                 [funcool/struct "1.4.0"]
                 [luminus-jetty "0.1.7"]
                 [luminus-migrations "0.6.6"]
                 [luminus-transit "0.1.2"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [markdown-clj "1.10.0"]
                 [metosin/muuntaja "0.6.6"]
                 [metosin/reitit "0.3.10"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [nrepl "0.6.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597" :scope "provided"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/tools.logging "0.5.0"]
                 [org.webjars.npm/bulma "0.8.0"]
                 [org.webjars.npm/material-icons "0.3.1"]
                 [org.webjars/webjars-locator "0.38"]
                 [org.xerial/sqlite-jdbc "3.28.0"]
                 [re-frame "0.10.9"]
                 [reagent "0.9.0-rc3"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.8.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [selmer "1.12.18"]

                 [cljsjs/react-grid-layout "0.17.1-0"]

                 [trptcolin/versioneer "0.2.0"]

                 ; for vanilla (matching versions)
                 [environ "1.1.0"]
                 [ring "1.8.0"]
                 [bk/ring-gzip "0.3.0"]
                 [radicalzephyr/ring.middleware.logger "0.6.0"]
                 [org.immutant/scheduling "2.1.10"]
                 [com.taoensso/sente "1.8.1"]
                 [com.stuartsierra/component "0.4.0"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]
                 [org.clojure/core.match "0.3.0"]
                 [com.layerware/hugsql "0.5.1"]
                 [org.xerial/sqlite-jdbc "3.30.1"]
                 [seancorfield/next.jdbc "1.0.12"]
                 [org.clojure/tools.nrepl "0.2.13"]

                 [cljsjs/react-color "2.13.8-0"]]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild" "resources/public/libs"]
  :target-path "target/%s/"

  :main ^:skip-aot vanilla.server

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-marginalia "0.9.1"]]
  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js"]

  :figwheel {:http-server-root "public"
             :server-logfile "log/figwheel-logfile.log"
             :server-port 3469
             :nrepl-port 7002
             :css-dirs ["resources/public/css"]
             :nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :profiles {:uberjar {:omit-source true
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]
                       :cljsbuild {:builds
                                   {:min
                                    {:source-paths ["src/cljc" "src/cljs" "env/prod/cljs"]
                                     :compiler
                                                   {:output-dir "target/cljsbuild/public/js/compiled"
                                                    :output-to "target/cljsbuild/public/js/compiled/app.js"
                                                    :source-map "target/cljsbuild/public/js/compiled/app.js.map"
                                                    :asset-path "js/compiled"
                                                    :main "vanilla.app"
                                                    :optimizations :advanced
                                                    :pretty-print false
                                                    :infer-externs true
                                                    :closure-warnings {:externs-validation :off
                                                                       :non-standard-jsdoc :off}
                                                    :externs ["react/externs/react.js"
                                                              "externs.js"]}}}}
                       :aot :all
                       :uberjar-name "vanilla.jar"
                       :source-paths ["env/prod/clj"]
                       :resource-paths ["env/prod/resources"]}

             :dev           [:project/dev :profiles/dev]
             :test          [:project/dev :project/test :profiles/test]

             :project/dev  {:jvm-opts ["-Dconf=dev-config.edn"]
                            :dependencies [[binaryage/devtools "0.9.11"]
                                           [cider/piggieback "0.4.2"]
                                           [doo "0.1.11"]
                                           [figwheel-sidecar "0.5.19"]
                                           [pjstadig/humane-test-output "0.10.0"]
                                           [prone "2019-07-08"]
                                           [day8.re-frame/re-frame-10x "0.4.5"]
                                           [day8.re-frame/tracing "0.5.3"]
                                           [ring/ring-devel "1.8.0"]
                                           [ring/ring-mock "0.4.0"]

                                           ;from original
                                           [figwheel "0.5.2"]
                                           [pjstadig/humane-test-output "0.10.0"]
                                           [com.cemerick/piggieback "0.2.2"]
                                           [org.clojure/tools.nrepl "0.2.13"]
                                           [org.clojure/tools.namespace "0.3.1"]
                                           [org.clojure/java.classpath "0.3.0"]]

                            :plugins      [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                           [jonase/eastwood "0.3.5"]
                                           [lein-doo "0.1.11"]
                                           [lein-figwheel "0.5.19"]
                                           [lein-marginalia "0.9.1"]]
                            :cljsbuild {:builds
                                        {:app
                                         {:source-paths ["src/cljs" "env/dev/cljs"]
                                          :figwheel {:on-jsload "vanilla.core/start-dashboard"}
                                          :compiler {:output-dir "target/cljsbuild/public/js/compiled"
                                                     :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true
                                                                       "day8.re_frame.tracing.trace_enabled_QMARK_"  true}
                                                     :optimizations :none
                                                     :preloads [day8.re-frame-10x.preload]
                                                     :output-to "target/cljsbuild/public/js/compiled/app.js"
                                                     :asset-path "js/compiled";"resources/public/js/compiled/out"
                                                     :source-map true
                                                     :main "vanilla.app"
                                                     :pretty-print true
                                                     :infer-externs true
                                                     :closure-warnings {:externs-validation :off
                                                                        :non-standard-jsdoc :off}
                                                     :externs ["react/externs/react.js"
                                                               "externs.js"]}}}}


                            :doo {:build "test"}
                            :source-paths ["env/dev/clj"]
                            :resource-paths ["env/dev/resources"]
                            :repl-options {:init-ns user}
                            :injections [(require 'pjstadig.humane-test-output)
                                         (pjstadig.humane-test-output/activate!)]}

             :project/test {:jvm-opts ["-Dconf=test-config.edn"]
                            :resource-paths ["env/test/resources"]
                            :cljsbuild
                            {:builds
                             {:test
                              {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                               :compiler {:output-to "target/test.js"
                                          :main "grid-play.doo-runner"
                                          :optimizations :whitespace
                                          :pretty-print true}}}}}

             :profiles/dev {}
             :profiles/test {}})
