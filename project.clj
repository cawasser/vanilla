(defproject vanilla "lein-git-inject/version"

  :description "Vanilla Dashboard - a simple dashboard built on dashboard-clj"
  :url ""

  :dependencies [[buddy/buddy-auth "2.2.0"]
                 [buddy/buddy-core "1.6.0"]
                 [buddy/buddy-hashers "1.4.0"]
                 [buddy/buddy-sign "3.1.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [cheshire "5.9.0"]
                 [cljs-ajax "0.8.0"]
                 [clojure.java-time "0.3.2"]
                 [com.cognitect/transit-clj "0.8.319"]
                 [com.google.javascript/closure-compiler-unshaded "v20191027" :scope "provided"]
                 [conman "0.8.4"]
                 [cprop "0.1.15"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [expound "0.8.3"]
                 [funcool/struct "1.4.0"]
                 [luminus-jetty "0.1.7"]
                 [luminus-migrations "0.6.6"]
                 [luminus-transit "0.1.2"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [markdown-clj "1.10.1"]
                 [metosin/muuntaja "0.6.6"]
                 [metosin/reitit "0.3.10"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [nrepl "0.6.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597" :scope "provided"]
                 [org.clojure/core.async "0.4.500"]
                 [org.clojure/google-closure-library "0.0-20191016-6ae1f72f" :scope "provided"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/tools.logging "0.5.0"]
                 [org.webjars.npm/bulma "0.8.0"]
                 [org.webjars.npm/material-icons "0.3.1"]
                 [org.webjars/webjars-locator "0.38"]
                 [org.xerial/sqlite-jdbc "3.28.0"]


                 [org.apache.qpid/qpid-jms-client "0.51.0"]

                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [re-frame "0.10.9"]
                 [reagent "0.9.0-rc3"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.8.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [selmer "1.12.18"]
                 [thheller/shadow-cljs "2.8.94" :scope "provided"]
                 [dk.ative/docjure "1.12.0"]
                 [datascript "0.18.10"]
                 [trptcolin/versioneer "0.2.0"]
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
                 [day8.re-frame/re-frame-10x "0.4.3"]
                 [day8.re-frame/tracing "0.5.3"]
                 [clj-time "0.15.2"]
                 [monoid/rough-cljs "1.0.0"]]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild" "resources/public/libs"]
  :target-path "target/%s/"

  :main ^:skip-aot vanilla.server

  :plugins [[lein-marginalia "0.9.1"]
            [day8/lein-git-inject "0.0.11"]
            [lein-pprint "1.3.2"]]

  :middleware [leiningen.git-inject/middleware]

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js"]

  :profiles {:uberjar {:omit-source true
                       :prep-tasks     ["compile" ["run" "-m" "shadow.cljs.devtools.cli" "release" "app"]]
                       :dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]
                       :aot :all
                       :uberjar-name "vanilla.jar"
                       :source-paths ["env/prod/clj" "env/prod/cljs"]
                       :resource-paths ["env/prod/resources"]}

             :dev           [:project/dev :profiles/dev]

             :test          [:project/dev :project/test :profiles/test]

             :project/dev  {:jvm-opts ["-Dconf=dev-config.edn"]
                            :dependencies [[binaryage/devtools "0.9.11"]
                                           [cider/piggieback "0.4.2"]
                                           [doo "0.1.11"]
                                           [pjstadig/humane-test-output "0.10.0"]
                                           [prone "2019-07-08"]
                                           [ring/ring-devel "1.8.0"]
                                           [ring/ring-mock "0.4.0"]
                                           [ring/ring-json "0.5.0"]

                                           [pjstadig/humane-test-output "0.10.0"]
                                           [com.cemerick/piggieback "0.2.2"]
                                           [org.clojure/tools.nrepl "0.2.13"]
                                           [org.clojure/tools.namespace "0.3.1"]
                                           [org.clojure/java.classpath "0.3.0"]]

                            :plugins      [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                           [jonase/eastwood "0.3.5"]
                                           [lein-doo "0.1.11"]
                                           [lein-marginalia "0.9.1"]]

                            :doo {:build "test"}
                            :source-paths ["env/dev/clj" "env/dev/cljs" "test/cljs"]
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
