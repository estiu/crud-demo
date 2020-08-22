(ns com.reducecombine.crud-demo.router.component
  (:require
   [com.reducecombine.crud-demo.environment.kws :as environment]
   [com.reducecombine.crud-demo.router.kws :as router]
   [com.reducecombine.crud-demo.widgets.handlers :as widgets.handlers]
   [com.reducecombine.crud-demo.widgets.kws :as widgets]
   [com.stuartsierra.component :as component]
   [io.pedestal.http :as http]
   [io.pedestal.http.body-params :as body-params]
   [io.pedestal.http.route :as route]
   [nedap.components.pedestal.router.kws :as components.pedestal.router]
   [nedap.speced.def :as speced]
   [nedap.utils.modular.api :refer [implement]]
   [ring.util.response :refer [response]]))

(def home-page
  [(fn [request]
     (response "Hello World!"))])

(defn common-interceptors [component-interceptors]
  (into component-interceptors [(body-params/body-params) http/html-body]))

(defn routes [component-interceptors environment]
  (let [base                (common-interceptors component-interceptors)]
    #{["/"         :get (into base home-page)                  :route-name ::router/home-page]
      ["/widgets/" :get (into base widgets.handlers/home-page) :route-name ::widgets/home-page]}))

(speced/defn ^::components.pedestal.router/component start
  [{^::environment/component env ::environment/component
    ::keys                       [^vector? component-interceptors]
    :as                          ^::components.pedestal.router/component this}]
  (assoc this ::components.pedestal.router/routes (routes component-interceptors env)))

(defn stop [this]
  {})

(defn new [component-interceptors]
  (implement {::component-interceptors component-interceptors}
    component/start start
    component/stop  stop))
