(ns wheelcrowd.data.configuration
  (use wheelcrowd.configuration)
  (require [clojure.java.jdbc :as sql]))

(def postgres-db {:subprotocol "postgresql"
                  :subname (System/getenv "DATABASE_URL")
                  :user (System/getenv "DATABASE_USER")
                  :password (System/getenv "DATABASE_PASS")})

(defn postgres-config[]
  (if production?
    (assoc postgres-db :ssl true)
    postgres-db))
