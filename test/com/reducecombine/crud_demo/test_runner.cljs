(ns com.reducecombine.crud-demo.test-runner
  (:require
   [cljs.nodejs :as nodejs]
   [nedap.utils.test.api :refer-macros [run-tests]]))

(nodejs/enable-util-print!)

(defn -main []
  (run-tests))

(set! *main-cli-fn* -main)
