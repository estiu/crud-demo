(ns com.reducecombine.crud-demo.persistence.protocols.creation
  (:require
   [com.reducecombine.crud-demo.persistence.protocols.specs :as persistence.protocols.specs]
   [nedap.speced.def :as speced]))

(speced/defprotocol Creation
  ""
  ;; to be implemented by persistence adapters (datomic, honey)
  (create! [this model object]
    "Performs a creation to disk."))
