(ns com.reducecombine.crud-demo.persistence.protocols.querying
  (:require
   [clojure.spec.alpha :as spec]
   [nedap.speced.def :as speced]))

(spec/def ::query coll?)

(speced/def-with-doc ::query-result
  "A query result, after IO."
  (spec/keys :opt-un [::result
                      ::errors]))

(speced/defprotocol Querying
  ""
  (^::query-result query [this ^::query query]
    ""))
