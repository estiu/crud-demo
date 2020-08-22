(ns com.reducecombine.crud-demo.{{model_name_plural}}.handlers
  (:require
   [com.reducecombine.crud-demo.{{model_name_plural}}.kws :as {{model_name_plural}}]
   [com.reducecombine.crud-demo.{{model_name_plural}}.protocols.querying :as {{model_name_plural}}.protocols.querying]
   [ring.util.response :as response :refer [response]]))

(def home-page
  [(fn [request]
     (let [{:keys [result errors] :as x} (-> request
                                             ::{{model_name_plural}}/component
                                             ({{model_name_plural}}.protocols.querying/find request 1))]
       (if result
         (response/response (pr-str result))
         (response/not-found (pr-str errors)))))])
