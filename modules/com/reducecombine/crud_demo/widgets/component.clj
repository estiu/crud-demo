(ns com.reducecombine.crud-demo.widgets.component
  (:require
   [com.reducecombine.crud-demo.widgets.impl.queries.sql :as impl.queries.sql]
   [com.reducecombine.crud-demo.widgets.protocols.querying :as widgets.protocols.querying]
   [nedap.utils.modular.api :refer [implement]]))

(defn new []
  (implement {}
    widgets.protocols.querying/--find impl.queries.sql/query-one))
