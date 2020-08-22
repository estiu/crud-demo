(ns com.reducecombine.crud-demo.widgets.handlers
  (:require
   [com.reducecombine.crud-demo.widgets.kws :as widgets]
   [com.reducecombine.crud-demo.widgets.protocols.querying :as widgets.protocols.querying]
   [ring.util.response :as response :refer [response]]))

(def home-page
  [(fn [request]
     (let [{:keys [result errors] :as x} (-> request
                                             ::widgets/component
                                             (widgets.protocols.querying/find request 1))]
       (if result
         (response/response (pr-str result))
         (response/not-found (pr-str errors)))))])
