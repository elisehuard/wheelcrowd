(defproject wheelcrowd "1.0.0-SNAPSHOT"
  :description "Wheelcrowd: how accessible are locations around us?"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/data.json "0.2.1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [ring "1.1.6"]
                 [compojure "1.1.3"]
                 [clj-http "0.5.7"]
                 [hiccup "1.0.1"]
                 [ragtime "0.3.2"]]
  :plugins [[lein-ring "0.7.5"]
            [ragtime/ragtime.lein "0.3.2"]]
  :ragtime {:migrations ragtime.sql.files/migrations
            :database (str "jdbc:" (or (let [database_host (System/getenv "DATABASE_HOST")
                                             database_user (System/getenv "DATABASE_USER")
                                             database_pass (System/getenv "DATABASE_PASS")]
                                           (str "postgresql://" database_host 
                                                (if (clojure.string/blank? database_user) "" (str "?user=" database_user))
                                                (if (clojure.string/blank? database_pass) "" (str "&password=" database_pass))))))}
  :ring {:handler wheelcrowd.routes/app})
