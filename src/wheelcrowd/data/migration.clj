(ns wheelcrowd.data.migration
    (:use [wheelcrowd.data.configuration])
    (:require [clojure.java.jdbc :as sql]))

(defn create-accessibility []
  (try
    (sql/with-connection (postgres-config)
      (sql/create-table :rating
        [:id :serial "PRIMARY KEY"]
        [:foursquare_id :varchar "UNIQUE"]
        [:name :varchar "NOT NULL"]
        [:accessible :boolean]
        [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]))
     (catch Exception e (.printStackTrace (.getNextException e)))))

(defn -main []
    (print "Migrating database...") (flush)
    (create-accessibility)
    (println " done"))
