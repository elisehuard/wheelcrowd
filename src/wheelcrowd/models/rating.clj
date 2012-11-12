(ns wheelcrowd.models.rating
  (:require [wheelcrowd.data.rating :as data]))

(defn retrieve[id]
  (if (nil? id)
    nil
    (first (data/get-rating id))))

(defn make-accessible[id venue-name]
  "update venue to make it accessible"
  (data/find-or-update-rating id venue-name true))

(defn make-inaccessible[id venue-name]
  "update venue to make it inaccessible"
  (data/find-or-update-rating id venue-name false))
