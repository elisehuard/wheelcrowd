(ns wheelcrowd.routes
    (:use compojure.core
          wheelcrowd.views
         [hiccup.middleware :only (wrap-base-url)]
         [ring.util.response :only (redirect)])
    (:require [compojure.route :as route]
              [compojure.handler :as handler]
              [compojure.response :as response]
              [clojure.data.json :as json]
              [wheelcrowd.location.foursquare :as foursquare]
              [wheelcrowd.location :as location]))

(defn- emit-json
  [type x & [status]]
  {:headers {"Content-Type"  "application/json" 
             "Cache-Control" "public, max-age=86400"
             "Vary"          "Accept-Encoding"}
   :status (or status 200)
   :body (json/json-str {type x})})

(defroutes main-routes
             (GET "/" [] (index-page))
             (GET "/venues" [lat lon query]
                  (pr lat lon query)
                (venues-page (location/venues lat lon query foursquare/config)))
             (GET "/venue/:id" [id]
                (venue-page (location/venue id foursquare/config)))
             (POST "/venue/:id" [id venue-name accessible-flip]
                (location/update-accessible id venue-name accessible-flip)
                (emit-json :accessible accessible-flip))
             (route/resources "/")
             (route/not-found "Page not found"))

(def app
    (-> (handler/site main-routes)
            (wrap-base-url)))
