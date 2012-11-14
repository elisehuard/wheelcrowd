(ns wheelcrowd.location
   (:require [wheelcrowd.location.foursquare :as foursquare])
   (:require [wheelcrowd.models.rating :as rating]))

(defn accessible-venue[venue]
  (let [rating (rating/retrieve (venue :id))]
    (cond (not (nil? rating))      (assoc venue :accessible (rating :accessible))
          (> (venue :tip-count) 0) (assoc venue :accessible (foursquare/tips-accessible? (venue :id) foursquare/config))
          :else                    (assoc venue :accessible nil))))

; aggregate venue data and tip information
(defn venues[lat lon query config]
   (map accessible-venue (foursquare/search lat lon query config)))

(defn venue [id config]
   (accessible-venue (foursquare/venue id config)))

(defn make-accessible[id venue-name]
  (rating/make-accessible id venue-name))

(defn make-inaccessible[id venue-name]
  (rating/make-inaccessible id venue-name))
