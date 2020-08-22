(ns com.reducecombine.crud-demo.persistence.protocols.specs
  (:require
   [clojure.spec.alpha :as spec]))

(spec/def ::request-context map?)
