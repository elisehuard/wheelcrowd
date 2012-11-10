(ns wheelcrowd.location.foursquare
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(def config
  {:client-id (get (System/getenv) "FOURSQUARE_CLIENT_ID")
   :client-secret (get (System/getenv) "FOURSQUARE_CLIENT_SECRET")})

(defn auth-params[config]
  (str "client_id="      (config :client-id) 
       "&client_secret=" (config :client-secret)))

(defn api-call[request]
  (let [response (client/get request)]
    (json/read-str (response :body))))

(defn search-request[lat lon foursquare-config]
  (str "https://api.foursquare.com/v2/venues/search?"
       "ll=" lat "," lon "&"
       (auth-params foursquare-config)))

(defn search-call[lat lon foursquare-config]
  (api-call (search-request lat lon foursquare-config)))

(defn get-items[json]
   ((first ((json "response") "groups")) "items"))

(defn venue-relevant-details[location-json]
  {:id   (location-json "id")
   :name (location-json "name")
   :distance (when (location-json "location") ((location-json "location") "distance"))
   :categories (map (fn[x] (x "name")) (location-json "categories"))
   :tip-count ((location-json "stats") "tipCount") })

(defn search[lat lon foursquare-config]
  (sort-by :distance <
    (map venue-relevant-details (get-items (search-call lat lon foursquare-config)))))

(defn tips-request[id config]
  (str "https://api.foursquare.com/v2/venues/" id "/tips?"
       (auth-params config)))

(defn tips-call[id config]
  (api-call (tips-request id config)))

(defn tip-accessible[text]
  (cond (re-find #"(#accessfail|#af)" text) false
        (re-find #"(#accesspass|#ap)" text) true
        :else nil))

(defn tip-information[tip]
  {:text (tip "text")
   :accessible (tip-accessible (tip "text"))
   :created (tip "createdAt")})

(defn tips-get-items[json]
  (map tip-information (((json "response") "tips") "items")))

(defn tips-conclusion[tips]
  (let [relevant-tip  (first (sort-by :created > (filter (fn[x] (not (= (x :accessible) nil))) tips)))]
    (if (nil? relevant-tip)
      nil
      (relevant-tip :accessible))))

(defn tips-accessible?[id config]
   (tips-conclusion (tips-get-items (tips-call id config))))
