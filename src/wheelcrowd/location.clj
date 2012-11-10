(ns wheelcrowd.location
   (:require [wheelcrowd.location.foursquare :as foursquare])
   (:require [wheelcrowd.models.rating :as rating]))

(defn accessible-venue[venue]
  (let [rating (rating/get (venue :foursquare-id))]
    (cond (not (nil? rating))      (assoc venue :accessible (rating :accessible))
          (> (venue :tip-count) 0) (assoc venue :accessible (foursquare/tips-accessible? (venue :id) foursquare/config))
          :else                    (assoc venue :accessible nil))))

; aggregate venue data and tip information
(defn venues[lat lon config]
   (map accessible-venue (foursquare/search lat lon config)))
