(ns com.reducecombine.crud-demo.config.component
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [com.reducecombine.crud-demo.config.getters]
   [com.reducecombine.crud-demo.config.kws :as config]
   [com.reducecombine.crud-demo.environment.kws :as environment]
   [com.stuartsierra.component :as component]
   [nedap.speced.def :as speced]
   [nedap.utils.modular.api :refer [implement]]))

(speced/defn ^map? start [{^::environment/component env ::environment/component
                           :as                          ^::config/component this}]
  (let [config-value (-> (io/resource "config.edn")
                         (aero/read-config {:profile env}))]
    (-> this
        (assoc ::config/value config-value)
        (com.reducecombine.crud-demo.config.getters/setup!))))

(defn stop [this]
  {})

(defn new []
  (implement {}
    component/start start
    component/stop  stop))
