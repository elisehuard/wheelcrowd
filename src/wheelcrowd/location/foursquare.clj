(ns wheelcrowd.location.foursquare
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(def config
  {'client-id (get (System/getenv) "FOURSQUARE_CLIENT_ID")
   'client-secret (get (System/getenv) "FOURSQUARE_CLIENT_SECRET")})

(defn search-request[lat lon foursquare-config]
  (str "https://api.foursquare.com/v2/venues/search?"
       "ll=" lat "," lon 
       "&client_id="     (foursquare-config 'client-id) 
       "&client_secret=" (foursquare-config 'client-secret)))

(defn search-call[lat lon foursquare-config]
  (let [response (client/get (search-request lat lon foursquare-config))]
    (json/read-str (response :body))))

(defn get-items[json]
   ((first ((json "response") "groups")) "items"))

(defn relevant-details[location-json]
  {:id   (location-json "id")
   :name (location-json "name")
   :distance ((location-json "location") "distance")
   :categories (map (fn[x] (x "name")) (location-json "categories")) })

(defn search[lat lon foursquare-config]
  (sort-by :distance <
    (map relevant-details (get-items (search-call lat lon foursquare-config)))))
