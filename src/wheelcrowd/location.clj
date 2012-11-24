(ns wheelcrowd.location
   (:require [wheelcrowd.location.foursquare :as foursquare])
   (:require [wheelcrowd.models.rating :as rating]))

(defn foursquare-tip[venue]
  (foursquare/tips-accessible? (venue :id) foursquare/config))

(def foursquare-tip-memo
  (memoize foursquare-tip))

; side-effecting!
(defn cache-if-tip-present! [accessible venue]
  (if (not (nil? accessible))
    (rating/update-accessible (venue :id) (venue :name) accessible)
    nil))

(defn accessible-venue[venue]
  (let [rating (rating/retrieve (venue :id))]
    (cond (not (nil? rating))      (assoc venue :accessible (rating :accessible))
          (> (venue :tip-count) 0) (do (cache-if-tip-present! (foursquare-tip-memo venue) venue)
                                       (assoc venue :accessible (foursquare-tip-memo venue)))
          :else                    (assoc venue :accessible nil))))

(defn photo-venue [venue]
  (let [photo (foursquare/photo (venue :id) foursquare/config)]
    (if (nil? photo)
       (assoc venue :photo "/images/missing.png")
       (assoc venue :photo photo))))

(defn augmented-venue[venue]
  (photo-venue (accessible-venue venue)))

; aggregate venue data and tip information
(defn venues[lat lon query categoryId config]
   (map augmented-venue (foursquare/search lat lon query categoryId config)))

(defn venue [id config]
   (accessible-venue (foursquare/venue id config)))

(defn update-accessible[id venue-name accessible]
  (rating/update-accessible id venue-name accessible))
