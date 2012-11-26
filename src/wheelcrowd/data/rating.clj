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


(defn boolerize [string]
  (boolean (Boolean/valueOf string)))

(defn create-or-update-rating[id venue-name accessible foursquare-user]
  (let [rating (get-rating id)
        accessible-bool (boolerize accessible)]
    (if (empty? rating)
      (new-rating {:foursquare-id id :name venue-name :accessible accessible-bool :foursquare_user foursquare-user})
      (update-rating id {:accessible accessible-bool :foursquare_user foursquare-user})))) 
