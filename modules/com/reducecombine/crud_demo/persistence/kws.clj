(ns com.reducecombine.crud-demo.persistence.kws
  (:require
   [clojure.spec.alpha :as spec]))

(spec/def ::db-component some?)

(spec/def ::component some?)

(def dependencies [::db-component])
