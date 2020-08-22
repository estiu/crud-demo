(ns com.reducecombine.crud-demo.spec-helpers
  (:require
   [clojure.spec.alpha :as spec]))

(defn result-of [k]
  (let [rk (keyword (str (namespace k) "." (name k))
                    "result")]
    (eval `(spec/def ~rk ~k))
    (spec/and (eval `(spec/keys :opt-un [~rk
                                         ::errors]))
              (fn [m]
                (->> m keys (some #{:errors :result}))))))
