(ns dev
  (:require
   [clj-java-decompiler.core :refer [decompile]]
   [clojure.java.javadoc :refer [javadoc]]
   [clojure.pprint :refer [pprint]]
   [clojure.repl :refer [apropos dir doc find-doc pst source]]
   [clojure.test :refer [run-all-tests run-tests]]
   [clojure.tools.namespace.repl :refer [clear refresh refresh-dirs set-refresh-dirs]]
   [com.reducecombine.crud-demo.system]
   [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
   [criterium.core :refer [quick-bench]]
   [dev.templates :refer [new-model]]
   [formatting-stack.branch-formatter :refer [format-and-lint-branch! lint-branch!]]
   [formatting-stack.component]
   [formatting-stack.processors.test-runner :refer [test!]]
   [formatting-stack.project-formatter :refer [format-and-lint-project! lint-project!]]
   [lambdaisland.deep-diff]))

(set-refresh-dirs "src" "dev" "test" "modules" "libs")

(defn dev-system []
  (com.reducecombine.crud-demo.system/create
   {:additional-components {:formatting-stack (formatting-stack.component/new {})}
    :environment           :dev}))

(set-init (fn [_]
            (dev-system)))

(defn suite []
  (refresh)
  (run-all-tests #".*\.com\.reducecombine\.crud-demo.*"))

(defn unit []
  (refresh)
  (run-all-tests #"unit\.com\.reducecombine\.crud-demo.*"))

(defn slow []
  (refresh)
  (run-all-tests #"integration\.com\.reducecombine\.crud-demo.*"))

(defn diff [x y]
  (-> x
      (lambdaisland.deep-diff/diff y)
      (lambdaisland.deep-diff/pretty-print)))

(defn gt
  "gt stands for git tests"
  []
  (refresh)
  (test!))
