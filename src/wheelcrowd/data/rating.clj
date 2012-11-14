(ns wheelcrowd.data.rating
  (:use [wheelcrowd.data.configuration])
  (require [clojure.java.jdbc :as sql]))

(defn get-rating [foursquare-id]
  (sql/with-connection postgres-db
    (sql/with-query-results results
      [(str "select * from rating where foursquare_id = '" foursquare-id "';")]
      (into [] results))))

(defn new-rating[data]
  (sql/with-connection postgres-db
    (sql/insert-values :rating [:foursquare_id :name :accessible]
                               [(data :foursquare-id) (data :name) (data :accessible)])))

(defn update-rating
  "This method updates a rating"
  [id attribute-map]
  (sql/with-connection postgres-db
    (sql/update-values
      :rating
        ["foursquare_id=?" id]
        attribute-map)))


(defn find-or-update-rating[id venue-name accessible]
  (let [rating (get-rating id)]
    (if (empty? rating)
      (new-rating {:foursquare-id id :name venue-name :accessible accessible})
      (update-rating id {:accessible accessible})))) 
