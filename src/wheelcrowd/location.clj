(ns wheelcrowd.location
   (:require [wheelcrowd.location.foursquare :as foursquare])
   (:require [wheelcrowd.models.rating :as rating]))

(defn foursquare-tip[venue]
  (foursquare/tips-accessible? (venue :id) foursquare/config))

(def foursquare-tip-memo
  (memoize foursquare-tip))

; side-effecting!
(defn cache-if-tip-present! [tip venue]
  (if (not (nil? tip))
    (rating/update-accessible (venue :id) (venue :name) (tip :accessible) (tip :foursquare-user))
    nil))

(defn add-tip-accessible [tip venue]
  (if (nil? tip)
    (assoc venue :accessible nil)
    (assoc venue :accessible (tip :accessible))))

(defn accessible-venue[venue]
  (let [rating (rating/retrieve (venue :id))]
    (cond (not (nil? rating))      (assoc venue :accessible (rating :wheelchair_accessible) :comments (rating :comments))
          (> (venue :tip-count) 0) (do (cache-if-tip-present! (foursquare-tip-memo venue) venue)
                                       (add-tip-accessible (foursquare-tip-memo venue) venue))
          :else                    (assoc venue :accessible nil))))

; aggregate venue data and tip information
(defn venues[lat lon query categoryId config]
   (map accessible-venue (foursquare/search lat lon query categoryId config)))

(defn venue [id config]
   (accessible-venue (foursquare/venue id config)))

(defn update-accessible[id venue-name accessible]
  (rating/update-accessible id venue-name accessible nil))
