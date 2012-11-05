(ns wheelcrowd.location.foursquare
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(def config
  {:client-id (get (System/getenv) "FOURSQUARE_CLIENT_ID")
   :client-secret (get (System/getenv) "FOURSQUARE_CLIENT_SECRET")})

(defn auth-params[config]
  (str "client_id="      (config :client-id) 
       "&client_secret=" (config :client-secret)))

(defn search-request[lat lon foursquare-config]
  (str "https://api.foursquare.com/v2/venues/search?"
       "ll=" lat "," lon "&"
       (auth-params foursquare-config)))

(defn search-call[lat lon foursquare-config]
  (let [response (client/get (search-request lat lon foursquare-config))]
    (json/read-str (response :body))))

(defn get-items[json]
   ((first ((json "response") "groups")) "items"))

(defn search-relevant-details[location-json]
  {:id   (location-json "id")
   :name (location-json "name")
   :distance ((location-json "location") "distance")
   :categories (map (fn[x] (x "name")) (location-json "categories"))
   :tip-count ((location-json "stats") "tipCount") })

(defn search[lat lon foursquare-config]
  (sort-by :distance <
    (map search-relevant-details (get-items (search-call lat lon foursquare-config)))))

(defn tips-request[id config]
  (str "https://api.foursquare.com/v2/venues/" id "/tips?"
       (auth-params config)))

(defn tips-call[id config]
  (let [response (client/get (tips-request id config))]
    (json/read-str (response :body))))

(defn tips-get-items[json]
  (((json "response") "tips") "items"))

(defn tip-accessible[text]
  (cond (re-find #"(#accessfail|#af)" text) false
        (re-find #"(#accesspass|#ap)" text) true
        :else nil))

(defn tip-information[tip]
  {:text (tip "text")
   :accessible (tip-accessible (tip "text"))
   :created (tip "createdAt")})


(defn tips-conclusion[tips]
  (first
     (sort-by :created > (filter (fn[x] (not (= (x :accessible) nil))) tips))))

(defn tips[id config]
   (tips-conclusion (tips-get-items (tips-call id config))))
