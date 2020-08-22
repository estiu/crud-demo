(ns com.reducecombine.crud-demo.config.getters
  (:require
   [clojure.spec.alpha :as spec]
   [com.reducecombine.crud-demo.config.kws :as config]
   [com.reducecombine.crud-demo.config.protocols.config :as protocols.config]
   [nedap.speced.def :as speced]
   [nedap.utils.modular.api :refer [implement]]))

(spec/def ::found (complement #{::not-found}))

(defn getter [& path-segments]
  (speced/fn ^::found [^map? config]
    (get-in config (cons ::config/value path-segments) ::not-found)))

(def value (getter))

(def server-port
  (getter :server :port))

(def server-address
  (getter :server :address))

(defn setup! [config]
  (implement config
    protocols.config/--value value
    protocols.config/--server-port server-port
    protocols.config/--server-address server-address))
