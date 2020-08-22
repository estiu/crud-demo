(ns com.reducecombine.crud-demo.widgets.impl.creation)

(defn creation [this model object request-context]
  (if-not (-> request-context :user-id #{42})
    {:errors [:unauthorized]}
    {:result (-> object
                 (assoc :extra 3000))}))
