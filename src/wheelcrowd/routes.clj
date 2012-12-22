(ns wheelcrowd.routes
    (:use compojure.core
          wheelcrowd.views
          wheelcrowd.configuration
          wheelcrowd.middleware
         [hiccup.middleware :only (wrap-base-url)]
         [ring.util.response :only (redirect)]
         clj-airbrake.ring)
    (:require [compojure.route :as route]
              [compojure.handler :as handler]
              [compojure.response :as response]
              [clojure.data.json :as json]
              [wheelcrowd.location.foursquare :as foursquare]
              [wheelcrowd.location :as location]
              [wheelcrowd.categories :as categories]
              [wheelcrowd.models.comment :as comments]
              [clj-airbrake.core :as airbrake]))

(airbrake/set-host! "swerve-errbit.herokuapp.com")

(defn wrap-if [handler pred wrapper & args]
  (if pred
    (apply wrapper handler args)
           handler))

(defn- emit-json
  [x & [status]]
  {:headers {"Content-Type"  "application/json" 
             "Cache-Control" "public, max-age=86400"
             "Vary"          "Accept-Encoding"}
   :status (or status 200)
   :body (json/json-str x)})

(defroutes main-routes
             (GET "/" [] (index-page))
             (GET "/venues" [lat lon query categoryId]
                (venues-page (location/venues lat lon query categoryId foursquare/config)))
             (GET "/venue/:id" [id]
                (venue-page (location/venue id foursquare/config)))
             (POST "/venue/:id" [id venue-name accessible]
                (location/update-accessible id venue-name accessible)
                (emit-json {:accessible accessible}))
             (GET "/venue/:id/photo" [id]
                (emit-json {:photo (foursquare/photo id foursquare/config)}))
             (POST "/venue/:id/comment" [id text]
                (comments/new-comment id text)
                (venue-page (location/venue id foursquare/config)))
             (GET "/categories" [term]
                (emit-json (categories/find-category term foursquare/config)))
             (GET "/about" []
                (about-page))
             (route/resources "/")
             (route/not-found "Page not found"))

(def app
    (-> (handler/site main-routes)
            (wrap-base-url)
            (wrap-if production?
               wrap-airbrake (System/getenv "ERRBIT_API_KEY") "production")
            (wrap-request-logging)
            (wrap-exception-logging)))
