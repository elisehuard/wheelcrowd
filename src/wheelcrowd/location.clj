(ns wheelcrowd.location
   (:require [wheelcrowd.location.foursquare :as foursquare]))

(defn accessible-venue[venue]
  (if (> (venue :tip-count) 0)
    (assoc venue :accessible (foursquare/tips-accessible? (venue :id) foursquare/config))
    (assoc venue :accessible nil)))

; aggregate venue data and tip information
(defn venues[lat lon config]
   (map accessible-venue (foursquare/search lat lon config)))
