(ns wheelcrowd.data.comment
  (:use [wheelcrowd.data.configuration])
  (require [clojure.java.jdbc :as sql]))

(defn get-comments [foursquare-id]
  (sql/with-connection postgres-db
    (sql/with-query-results results
      [(str "select * from comments where rating_id = '" foursquare-id "' order by created_at desc;")]
      (into [] results))))
  
(defn new-comment [data]
  (sql/with-connection postgres-db
    (sql/insert-values :comments [:rating_id :text]
                                 [(:id data) (:text data)])))

