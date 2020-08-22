(ns com.reducecombine.crud-demo.widgets.impl.queries.sql
  "'Queries' perform validation/authentication/authorization
  before passing the query itself to `querying`, which will perform the main IO."
  (:require
   [com.reducecombine.crud-demo.persistence.kws :as persistence]
   [com.reducecombine.crud-demo.persistence.protocols.querying :as querying]))

(defn query-all [_ request-context]
  {:select [:*]
   :from   [:users]})

(defn query-count [_ request-context]
  {:select [:%count.*]
   :from   [:users]})

(defn query-one [_ {persistence ::persistence/component
                    :as         request-context} id]
  (if (-> request-context :user :id #{42})
    {:errors "Bad user"}
    (-> persistence (querying/query {:select [:*]
                                     :from   [:users]
                                     :where  [:= :id id]}))))
