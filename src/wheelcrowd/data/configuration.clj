(ns wheelcrowd.data.configuration
  (use wheelcrowd.configuration)
  (require [clojure.java.jdbc :as sql]))

(def postgres-db (System/getenv "DATABASE_URL"))
