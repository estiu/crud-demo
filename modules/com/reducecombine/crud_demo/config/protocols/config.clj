(ns com.reducecombine.crud-demo.config.protocols.config
  (:require
   [clojure.spec.alpha :as spec]
   [com.reducecombine.crud-demo.config.kws :as config]
   [nedap.speced.def :as speced]))

(spec/def ::string string?)

(speced/defprotocol Config
  "Getters for this app's most used paths."
  (^::config/value value [this] "The whole config map")
  (^pos-int? server-port [this] "port to bind webserver to")
  (^string? server-address [this] "address to bind webserver to"))
