(ns com.reducecombine.crud-demo.persistence.components.sql
  (:require
   [clojure.java.jdbc :as jdbc]
   [com.reducecombine.crud-demo.persistence.kws :as persistence]
   [com.reducecombine.crud-demo.persistence.protocols.querying :as persistence.protocols.querying]
   [honeysql.core :as honeysql]
   [nedap.utils.modular.api :refer [implement]]))

(defn query [{db-component ::persistence/db-component} q]
  (try
    (let [db (->> db-component :pool (hash-map :datasource))]
      {:result (->> q
                    honeysql/format
                    (jdbc/query db))})
    (catch Exception e
      {:errors [e]})))

(defn new []
  (implement {}
    persistence.protocols.querying/--query query))
