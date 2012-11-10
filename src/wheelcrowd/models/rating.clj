(ns wheelcrowd.models.rating
  (:require [wheelcrowd.data.rating :as data]))

(defn retrieve[venue]
  (if (nil? venue)
    nil
    (first (data/get-rating (venue :foursquare-id)))))
