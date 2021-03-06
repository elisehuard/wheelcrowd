(ns wheelcrowd.location.foursquare
  (:use [ring.util.codec])
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json])
  (:require [clojure.string :as string]))

(def config
  {:client-id (get (System/getenv) "FOURSQUARE_CLIENT_ID")
   :client-secret (get (System/getenv) "FOURSQUARE_CLIENT_SECRET")})

(defn auth-params[config]
  (str "client_id="      (config :client-id) 
       "&client_secret=" (config :client-secret)))

(defn api-call[request]
  (let [response (client/get request)]
    (json/read-str (response :body))))

; search venues

(defn search-request[lat lon query categoryId foursquare-config]
  (str "https://api.foursquare.com/v2/venues/search?"
       "ll=" lat "," lon "&"
       (if (string/blank? query) "" (str "query=" (url-encode query) "&"))
       (if (string/blank? categoryId) "" (str "categoryId=" categoryId "&"))
       (auth-params foursquare-config)))

(defn search-call[lat lon query categoryId foursquare-config]
  (api-call (search-request lat lon query categoryId foursquare-config)))

(defn get-items[json]
   ((first ((json "response") "groups")) "items"))

(defn venue-relevant-details[location-json]
  {:id   (location-json "id")
   :name (location-json "name")
   :distance (if (nil? (location-json "location")) nil ((location-json "location") "distance"))
   :categories (map (fn[x] (x "name")) (location-json "categories"))
   :tip-count ((location-json "stats") "tipCount") })

(defn search[lat lon query categoryId foursquare-config]
  (sort-by :distance <
    (map venue-relevant-details (get-items (search-call lat lon query categoryId foursquare-config)))))

; tips

(defn tips-request[id config]
  (str "https://api.foursquare.com/v2/venues/" id "/tips?"
       (auth-params config)))

(defn tips-call[id config]
  (api-call (tips-request id config)))

(defn tip-accessible[text]
  (cond (re-find #"(#accessfail|#af)" text) "no"
        (re-find #"(#accesspass|#ap)" text) "yes"
        (re-find #"(#accesslimited|#al)" text) "limited"
        :else nil))

(defn tip-information[tip]
  {:text (tip "text")
   :user ((tip "user") "id")
   :accessible (tip-accessible (tip "text"))
   :created (tip "createdAt")})

(defn tips-get-items[json]
  (map tip-information (((json "response") "tips") "items")))

(defn tips-conclusion[tips]
  (let [relevant-tip  (first (sort-by :created > (filter (fn[x] (not (= (x :accessible) nil))) tips)))]
    (if (nil? relevant-tip)
      nil
      {:accessible (relevant-tip :accessible) :foursquare-user (relevant-tip :user)})))

(defn tips-accessible?[id config]
   (tips-conclusion (tips-get-items (tips-call id config))))

; single venue

(defn venue-request[id foursquare-config]
  (str "https://api.foursquare.com/v2/venues/"
       id "?"
       (auth-params foursquare-config)))

(defn venue-call [id config]
  (api-call (venue-request id config)))

(defn venue-response [json]
  ((json "response") "venue"))

(defn venue [id config]
  (venue-relevant-details 
    (venue-response (venue-call id config))))

; categories

(defn categories-request[foursquare-config]
  (str "https://api.foursquare.com/v2/venues/categories?"
       (auth-params foursquare-config)))

(defn categories-call[foursquare-config]
  (api-call (categories-request foursquare-config)))

(defn categories-response [json]
  ((json "response") "categories"))

(defn unspool-category [category]
  (tree-seq (fn[c] (not (empty? (c "categories"))))
            (fn[c] (c "categories"))
            category))

(defn category-data [category]
  (map (fn[c]
         {:value (c "id")
          :label (c "shortName")})
       (unspool-category category)))

(defn categories [config]
  (pr "searching categories")
  (mapcat category-data (categories-response (categories-call config))))

; memoization: should be across all web threads really
(def categories-memo
   (memoize categories))

; photo

(defn photos-request[id config]
  (str "https://api.foursquare.com/v2/venues/" id "/photos?group=venue&"
       (auth-params config)))

(defn photos-call[id config]
  (api-call (photos-request id config)))

(defn photos-response[json]
  (((json "response") "photos") "items"))

(defn photo[id config]
  (let [photo-json (first (photos-response (photos-call id config)))]
    (if (nil? photo-json) 
      nil
      (photo-json "url"))))
