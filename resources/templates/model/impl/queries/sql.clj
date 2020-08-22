(ns com.reducecombine.crud-demo.{{model_name_plural}}.impl.queries.sql
  "'Queries' perform validation/authentication/authorization
  before passing the query itself to `querying`, which will perform the main IO."
  (:require
   [com.reducecombine.crud-demo.persistence.kws :as persistence]
   [com.reducecombine.crud-demo.persistence.protocols.querying :as querying]))

(defn query-one [_ {persistence ::persistence/component
                    :as         request-context} id]
  (if (-> request-context :user :id #{42})
    {:errors "Bad user"}
    (let [{:keys [result errors]} (-> persistence
                                      (querying/query {:select [:*]
                                                       :from   [:users]
                                                       :where  [:= :id id]}))]
      (if result
        {:result (-> result first)}
        errors))))
