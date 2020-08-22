(ns com.reducecombine.crud-demo.config.pedestal-adapter
  (:require
   [com.reducecombine.crud-demo.config.kws :as config]
   [com.reducecombine.crud-demo.config.protocols.config :as protocols.config]
   [com.reducecombine.crud-demo.environment.kws :as environment]
   [com.stuartsierra.component :as component]
   [io.pedestal.http :as pedestal.http]
   [nedap.speced.def :as speced]
   [nedap.utils.modular.api :refer [implement]]))

(speced/defn ^map? start [{^::config/component config ::config/component
                           ^::environment/component env ::environment/component}]
  {::pedestal.http/port   (protocols.config/server-port config)
   ::pedestal.http/host   (protocols.config/server-address config)
   ::pedestal.http/join?  false
   :env                   env})

(defn stop [this]
  {})

(defn new []
  (implement {}
    component/start start
    component/stop  stop))
