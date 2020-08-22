(ns com.reducecombine.crud-demo.system
  (:require
   [com.grzm.component.pedestal :refer [using-component]]
   [com.reducecombine.crud-demo.config.component :as config.component]
   [com.reducecombine.crud-demo.config.kws :as config]
   [com.reducecombine.crud-demo.config.pedestal-adapter :as config.pedestal-adapter]
   [com.reducecombine.crud-demo.environment.kws :as environment]
   [com.reducecombine.crud-demo.persistence.components.sql :as persistence.components.sql]
   [com.reducecombine.crud-demo.persistence.kws :as persistence]
   [com.reducecombine.crud-demo.router.component :as router.component]
   [com.reducecombine.crud-demo.router.kws :as com.reducecombine.crud-demo.router]
   [com.reducecombine.crud-demo.widgets.component :as widgets.component]
   [com.reducecombine.crud-demo.widgets.kws :as widgets]
   [com.stuartsierra.component :as component]
   [modular.postgres]
   [nedap.components.pedestal.interceptors.component-injector :refer [component-injector]]
   [nedap.components.pedestal.router.kws :as components.pedestal.router]
   [nedap.components.pedestal.server.component :as server.component]
   [nedap.components.pedestal.server.kws :as server]
   [nedap.components.pedestal.service.component :as service.component]
   [nedap.components.pedestal.service.kws :as service]
   [nedap.speced.def :as speced])
  (:gen-class))

(def common-dependencies
  "The dependencies all domain-specific modules can be presumed to depend on."
  [::environment/component
   ::persistence/component
   ::config/component])

(def domain-specific-dependencies
  [::widgets/component])

(def components-to-inject
  (into common-dependencies domain-specific-dependencies))

(defn adapters []
  {::pedestal-adapter (-> (config.pedestal-adapter/new)
                          (component/using [::config/component ::environment/component]))})

(speced/defn create [{:keys [additional-components, ^::environment/component environment]}]
  (let [component-interceptors (conj (mapv using-component components-to-inject)
                                     (component-injector components-to-inject))
        defaults-kind (if (#{:dev :test} environment)
                        :dev
                        :production)
        server-dependencies (into server/dependencies components-to-inject)]
    (-> (component/system-map
         ::environment/component                environment
         ::config/component                     (-> (config.component/new)
                                                    (component/using config/dependencies))
         ::persistence/component                (-> (persistence.components.sql/new)
                                                    (component/using persistence/dependencies))
         ::persistence/db-component             (modular.postgres/map->Postgres {:url      "jdbc:postgresql:ebdb"
                                                                                 :user     "root"
                                                                                 :password ""})
         ::components.pedestal.router/component (-> (router.component/new component-interceptors)
                                                    (component/using com.reducecombine.crud-demo.router/dependencies))

         ::service/component                    (-> (service.component/new {::service/defaults-kind  defaults-kind
                                                                            ::service/expand-routes? false})
                                                    (component/using {::components.pedestal.router/component
                                                                      ::components.pedestal.router/component
                                                                      ::service/pedestal-options
                                                                      ::pedestal-adapter}))
         ::server/component                     (-> (server.component/new)
                                                    (component/using server-dependencies))
         ::widgets/component                    (-> (widgets.component/new)
                                                    (component/using widgets/dependencies)))
        (merge (adapters) additional-components))))

(defn -main [& args]
  (-> (create {:environment :production})
      (component/start)))
