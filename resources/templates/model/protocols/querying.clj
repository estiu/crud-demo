(ns com.reducecombine.crud-demo.{{model_name_plural}}.protocols.querying
  "These are protocols for allowing a datomic<->psql swap"
  (:refer-clojure :exclude [find])
  (:require
   [clojure.spec.alpha :as spec]
   [nedap.speced.def :as speced]))

(require '[com.reducecombine.crud-demo.spec-helpers :refer [result-of]])

(spec/def ::{{model_name_singular}} map?)

(spec/def ::find ::{{model_name_singular}})

(speced/defprotocol Querying
  ""
  (^{::speced/spec (result-of ::find)} find [_ request-context id]
    ""))
