(ns wheelcrowd.models.rating
  (:require [wheelcrowd.data.rating :as data]))

(defn get[venue]
  (first (data/get-rating (venue :foursquare-id))))
