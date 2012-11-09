(defproject wheelcrowd "1.0.0-SNAPSHOT"
  :description "Wheelcrowd: how accessible are locations around us?"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/data.json "0.2.1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [compojure "1.1.3"]
                 [clj-http "0.5.7"]
                 [hiccup "1.0.1"]]
  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler wheelcrowd.routes/app})
