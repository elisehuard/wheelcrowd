(ns wheelcrowd.data.configuration
  (use wheelcrowd.configuration)
  (require [clojure.java.jdbc :as sql]))

(def postgres-db {:subprotocol "postgresql"
                  :subname (System/getenv "DATABASE_SUB")})
