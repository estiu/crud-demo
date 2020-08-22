(ns com.reducecombine.crud-demo.persistence.components.dummy
  "A mock DB"
  (:require
   [com.reducecombine.crud-demo.persistence.protocols.querying :as persistence.protocols.querying]
   [nedap.utils.modular.api :refer [implement]]))

(defn query [this q]
  (case q
    {:select [:*]
     :from   [:users]
     :where  [:= :id 1]} {:result {:id 1}}
    {:errors ["Unknown query"]}))

(defn new []
  (implement {}
    persistence.protocols.querying/--query query))
