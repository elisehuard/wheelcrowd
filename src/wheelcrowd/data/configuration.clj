(ns wheelcrowd.data.configuration
  (use wheelcrowd.configuration)
  (require [clojure.java.jdbc :as sql]))

(def postgres-db 
  (let [postgres-db (System/getenv "DATABASE_URL")]
    (or postgres-db "postgresql://postgres@localhost:5432/wheelcrowd")))
