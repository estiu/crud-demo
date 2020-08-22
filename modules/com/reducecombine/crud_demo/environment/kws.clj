(ns com.reducecombine.crud-demo.environment.kws
  (:require
   [nedap.speced.def :as speced]))

(speced/def-with-doc ::component "The application environment" #{:dev :production :test})
