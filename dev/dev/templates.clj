(ns dev.templates
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [stencil.core :refer [render-string]])
  (:import
   (java.io File)
   (java.util Properties)))

(defn folderize [s]
  (-> s
      (string/replace "." "/")
      (string/replace "-" "_")))

(defn new-model [model-name-plural model-name-singular]
  (let [folder (-> (Properties.)
                   (doto (.load (io/input-stream "nedap.lein-template.properties")))
                   (.getProperty "nedap.lein-template.lib_prefix"))
        prefix (str "modules/" (folderize folder) "/" model-name-plural "/")]
    (->> "templates/model"
         io/resource
         io/as-file
         file-seq
         (filter (fn [^File f]
                   (-> f .isFile)))
         (map (fn [^File f]
                (let [suffix (-> f
                                 .getAbsolutePath
                                 (string/replace #".*templates/model/" ""))]
                  [f
                   (str prefix suffix)])))

         (run! (fn [[template-file, ^String s]]
                 (-> s File. .getParentFile .mkdirs)
                 (spit (-> s File.)
                       (render-string (slurp template-file)
                                      {:model_name_plural   model-name-plural
                                       :model_name_singular model-name-singular})))))))
