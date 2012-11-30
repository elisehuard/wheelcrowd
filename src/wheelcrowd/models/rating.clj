(ns wheelcrowd.models.rating
  (:require [wheelcrowd.data.rating :as rating-data])
  (:require [wheelcrowd.data.comment :as comment-data]))

(defn retrieve[id]
  (let [rating (first (rating-data/get-rating id))]
    (if (nil? rating)
      nil
      (assoc rating :comments (comment-data/get-comments id)))))

(defn update-accessible[id venue-name accessible foursquare-user]
  "update venue to make it accessible or inaccessible"
  (rating-data/create-or-update-rating id venue-name accessible foursquare-user))
