(ns com.reducecombine.crud-demo.{{model_name_plural}}.kws
  (:require
   [com.reducecombine.crud-demo.config.kws :as config]
   [com.reducecombine.crud-demo.environment.kws :as environment]))

(def dependencies [::environment/component
                   ::config/component])
