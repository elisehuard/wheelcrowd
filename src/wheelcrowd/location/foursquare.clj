(ns wheelcrowd.location.foursquare
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(def foursquare-config
  {'client-id "2R5BMXPAADPLBIXDVLTP5N0BFBA3JWR4RZUKSLG5XCHIU2CQ"
   'client-secret "XAUOLWJPRZNFR2OXYBGUI3RAKS3RIEH0HLAVEQ1WRD3MIDY4"})

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
   :categories (map (fn[x] (x "name")) (location-json "categories")) }
)

(defn search[lat lon foursquare-config]
   (map relevant-details (get-items (search-call lat lon foursquare-config))))
