(ns wheelcrowd.data.configuration
  (use wheelcrowd.configuration)
  (require [clojure.java.jdbc :as sql]))

(def postgres-db {:subprotocol "postgres"
                  :subname (System/getenv "DATABASE_SUB")})
