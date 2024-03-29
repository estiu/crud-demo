(ns com.reducecombine.crud-demo.widgets.protocols.querying
  "These are protocols for allowing a datomic<->psql swap"
  (:refer-clojure :exclude [find])
  (:require
   [clojure.spec.alpha :as spec]
   [nedap.speced.def :as speced]))

(require '[com.reducecombine.crud-demo.spec-helpers :refer [result-of]])

(spec/def ::widget map?)

(spec/def ::all (spec/coll-of ::widget))

(spec/def ::count-all nat-int?)

(spec/def ::find ::widget)

(speced/defprotocol Querying
  ""
  (^{::speced/spec (result-of ::all)} all [_ request-context]
    "")

  (^{::speced/spec (result-of ::count-all)} count-all [_ request-context]
    "")

  (^{::speced/spec (result-of ::find)} find [_ request-context id]
    ""))
