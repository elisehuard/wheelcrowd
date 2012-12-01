(ns wheelcrowd.models.rating
  (:require [wheelcrowd.data.rating :as rating-data])
  (:require [wheelcrowd.data.comment :as comment-data]))

(defn format-timestamp[comment]
  (assoc comment :created_at (->> (:created_at comment)
                             (.format (java.text.SimpleDateFormat. "dd MMM yyyy HH:mm")))))

(defn retrieve[id]
  (let [rating (first (rating-data/get-rating id))]
    (if (nil? rating)
      nil
      (assoc rating :comments (map format-timestamp (comment-data/get-comments id))))))

(defn update-accessible[id venue-name accessible foursquare-user]
  "update venue to make it accessible or inaccessible"
  (rating-data/create-or-update-rating id venue-name accessible foursquare-user))
