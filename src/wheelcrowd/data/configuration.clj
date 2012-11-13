(ns wheelcrowd.data.configuration)

(def postgres-db {:subprotocol "postgresql"
                  :subname (System/getenv "DATABASE_URL")
                  :user (System/getenv "DATABASE_USER")
                  :password (System/getenv "DATABASE_PASS")})
