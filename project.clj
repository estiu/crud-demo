;; Please don't bump the library version by hand - use ci.release-workflow instead.
(defproject com.reducecombine/crud-demo "unreleased"
  ;; Please keep the dependencies sorted a-z.
  :dependencies [[aero "1.1.6"]
                 [cheshire "5.10.0"]
                 [com.fasterxml.jackson.core/jackson-databind "2.10.2"]
                 [com.nedap.staffing-solutions/components.pedestal "1.1.0" :exclusions [io.pedestal/pedestal.jetty]]
                 [com.nedap.staffing-solutions/speced.def "2.0.0"]
                 [com.nedap.staffing-solutions/utils.modular "2.2.0-alpha3"]
                 [com.stuartsierra/component "1.0.0"]
                 [honeysql "0.9.4"]
                 [io.pedestal/pedestal.jetty "0.5.5" #_"Higher versions trigger GeneralSSL error"]
                 [juxt.modular/postgres "0.0.1-SNAPSHOT"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [prismatic/schema "1.1.9" #_"For juxt.modular/postgres"]
                 [ring/ring-core "1.8.0"]
                 [stencil "0.5.0"]]

  :description "crud-demo"

  :url "https://github.com/reducecombine/crud-demo"

  :min-lein-version "2.0.0"

  :signing {:gpg-key "_@_.com"}

  :target-path "target/%s"

  :source-paths ["src" "modules" "libs"]

  :test-paths ["src" "test" "modules" "libs"]

  ;; Note that the following options aren't passed to production servers (unless using Leiningen there):
  :jvm-opts ["-XX:-OmitStackTraceInFastThrow" ;; increase stacktrace usefulness
             "-server" ;; JCIP recommends exercising server optimizations in all envs.
             ;; The following flags setup GC with short STW pauses, which tend to be apt for webserver workloads.
             ;; Taken from https://docs.oracle.com/cd/E40972_01/doc.70/e40973/cnf_jvmgc.htm#autoId2
             ;; Note that -Xmx and -Xms are unset, since developers can have different needs around that.
             "-XX:+UseG1GC"
             "-XX:MaxGCPauseMillis=200"
             "-XX:ParallelGCThreads=20"
             "-XX:ConcGCThreads=5"
             "-XX:InitiatingHeapOccupancyPercent=70"]

  :monkeypatch-clojure-test false

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-pprint "1.1.2"]]

  ;; Please don't add `:hooks [leiningen.cljsbuild]`. It can silently skip running the JS suite on `lein test`.
  ;; It also interferes with Cloverage.
  :cljsbuild {:builds {"test" {:source-paths ["src" "test" "modules" "libs"]
                               :compiler     {:main          com.reducecombine.crud-demo.test-runner
                                              :output-to     "target/out/tests.js"
                                              :output-dir    "target/out"
                                              :target        :nodejs
                                              :optimizations :none}}}}

  ;; A variety of common dependencies are bundled with `nedap/lein-template`.
  ;; They are divided into two categories:
  ;; * Dependencies that are possible or likely to be needed in all kind of production projects
  ;;   * The point is that when you realise you needed them, they are already in your classpath, avoiding interrupting your flow
  ;;   * After realising this, please move the dependency up to the top level.
  ;; * Genuinely dev-only dependencies allowing 'basic science'
  ;;   * e.g. criterium, deep-diff, clj-java-decompiler

  ;; NOTE: deps marked with #_"transitive" are there to satisfy the `:pedantic?` option.
  :profiles {:dev        {:dependencies [[cider/cider-nrepl "0.16.0" #_"formatting-stack needs it"]
                                         [cloverage "1.2.0" #_"same version as lein-cloverage"]
                                         [com.clojure-goes-fast/clj-java-decompiler "0.3.0"]
                                         [com.nedap.staffing-solutions/utils.spec.predicates "1.1.0"]
                                         [com.stuartsierra/component.repl "0.2.0"]
                                         [com.taoensso/timbre "4.10.0"]
                                         [criterium "0.4.5"]
                                         [formatting-stack "4.2.1"]
                                         [lambdaisland/deep-diff "0.0-47"]
                                         [medley "1.3.0"]
                                         [org.clojure/core.async "1.0.567"]
                                         [org.clojure/math.combinatorics "0.1.6"]
                                         [org.clojure/test.check "1.0.0"]
                                         [org.clojure/tools.namespace "1.0.0"]
                                         [refactor-nrepl "2.4.0" #_"formatting-stack needs it"]]
                          :jvm-opts     ["-Dclojure.compiler.disable-locals-clearing=true"]
                          :plugins      [[lein-cloverage "1.2.0" #_"same version as cloverage"]]
                          :source-paths ["dev"]
                          :repl-options {:init-ns dev}}

             :provided   {:dependencies [[org.clojure/clojurescript "1.10.597"
                                          :exclusions [com.cognitect/transit-clj
                                                       com.google.code.findbugs/jsr305
                                                       com.google.errorprone/error_prone_annotations]]
                                         [com.fasterxml.jackson.core/jackson-core "2.10.2" #_"transitive"]
                                         [com.google.guava/guava "25.1-jre" #_"transitive"]
                                         [com.google.protobuf/protobuf-java "3.4.0" #_"transitive"]
                                         [com.cognitect/transit-clj "0.8.313" #_"transitive"]
                                         [com.google.errorprone/error_prone_annotations "2.1.3" #_"transitive"]
                                         [com.google.code.findbugs/jsr305 "3.0.2" #_"transitive"]]}

             :check      {:global-vars {*unchecked-math* :warn-on-boxed
                                        ;; avoid warnings that cannot affect production:
                                        *assert*         false}}

             ;; some settings recommended for production applications.
             ;; You may also add :test, but beware of doing that if using this profile while running tests in CI.
             ;; (since that would disable tests altogether)
             :production {:jvm-opts    ["-Dclojure.compiler.elide-meta=[:doc :file :author :line :column :added :deprecated :nedap.speced.def/spec :nedap.speced.def/nilable]"
                                        "-Dclojure.compiler.direct-linking=true"]
                          :global-vars {*assert* false}}

             ;; this profile is necessary since JDK >= 11 removes XML Bind, used by Jackson, which is a very common dep.
             :jdk11      {:dependencies [[javax.xml.bind/jaxb-api "2.3.1"]
                                         [org.glassfish.jaxb/jaxb-runtime "2.3.1"]]}

             :test       {:dependencies [[com.nedap.staffing-solutions/utils.test "1.6.2"]]
                          :jvm-opts     ["-Dclojure.core.async.go-checking=true"
                                         "-Duser.language=en-US"]}

             :nvd        {:plugins      [[lein-nvd "1.3.1"]]
                          :nvd          {:suppression-file "nvd_suppressions.xml"}
                          ;; These are lein-nvd transitive dependencies, copied verbatim, which Lein could otherwise alter.
                          :dependencies [[com.esotericsoftware/minlog "1.3"]
                                         [com.github.spullara.mustache.java/compiler "0.8.17"]
                                         [com.google.code.gson/gson "2.8.5"]
                                         [com.h2database/h2 "1.4.196"]
                                         [com.h3xstream.retirejs/retirejs-core "3.0.1"]
                                         [joda-time "2.10" #_"For clj-time"]
                                         [org.apache.commons/commons-compress "1.19"]
                                         [org.json/json "20140107"]
                                         [org.owasp/dependency-check-core "5.2.2"]]}

             :ncrw       {:global-vars  {*assert* true} ;; `ci.release-workflow` relies on runtime assertions
                          :dependencies [[com.nedap.staffing-solutions/ci.release-workflow "1.10.1"]]}

             :ci         {:pedantic? :abort
                          :jvm-opts  ["-Dclojure.main.report=stderr"]}})
