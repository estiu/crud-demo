(ns com.reducecombine.crud-demo.config.kws
  (:require
   [clojure.spec.alpha :as spec]
   [com.reducecombine.crud-demo.environment.kws :as environment]
   [nedap.speced.def :as speced]))

(def dependencies [::environment/component])

(speced/def-with-doc ::value
  "The value of the parsed configuration file"
  map?)

(speced/def-with-doc ::component
  "Provides access to the configuration file, via a strongly speced interface."
  (eval `(spec/keys :req ~dependencies
                    :opt [::value])))
