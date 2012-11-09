(ns wheelcrowd.data.rating
  (require [clojure.java.jdbc :as sql]))

(defn get-rating [foursquare-id]
  (sql/with-connection (System/getenv "DATABASE_URL")
    (sql/with-query-results results
      [(str "select * from rating where foursquare_id = '" foursquare-id "';")]
      (into [] results))))

(defn new-rating[data]
  (sql/with-connection (System/getenv "DATABASE_URL")
    (sql/insert-values :rating [:foursquare_id :name :accessible]
                               [(data :foursquare-id) (data :name) (data :accessible)])))
