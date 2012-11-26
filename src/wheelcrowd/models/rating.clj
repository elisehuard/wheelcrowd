(ns wheelcrowd.models.rating
  (:require [wheelcrowd.data.rating :as data]))

(defn retrieve[id]
  (if (nil? id)
    nil
    (first (data/get-rating id))))

(defn update-accessible[id venue-name accessible foursquare-user]
  "update venue to make it accessible or inaccessible"
  (data/create-or-update-rating id venue-name accessible foursquare-user))
