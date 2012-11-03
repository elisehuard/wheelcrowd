(defproject wheelcrowd "1.0.0-SNAPSHOT"
  :description "Wheelcrowd: how accessible are locations around us?"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "1.1.3"]
                 [hiccup "1.0.1"]]
  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler wheelcrowd.routes/app})
