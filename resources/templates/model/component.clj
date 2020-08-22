(ns com.reducecombine.crud-demo.{{model_name_plural}}.component
  (:require
   [com.reducecombine.crud-demo.{{model_name_plural}}.impl.queries.sql :as impl.queries.sql]
   [com.reducecombine.crud-demo.{{model_name_plural}}.protocols.querying :as {{model_name_plural}}.protocols.querying]
   [nedap.utils.modular.api :refer [implement]]))

(defn new []
  (implement {}
    {{model_name_plural}}.protocols.querying/--find impl.queries.sql/query-one))
