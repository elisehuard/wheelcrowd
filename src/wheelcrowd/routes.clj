(ns wheelcrowd.routes
    (:use compojure.core
          wheelcrowd.views
         [hiccup.middleware :only (wrap-base-url)]
         [ring.util.response :only (redirect)])
    (:require [compojure.route :as route]
              [compojure.handler :as handler]
              [compojure.response :as response]
              [wheelcrowd.location.foursquare :as foursquare]
              [wheelcrowd.location :as location]))

(defroutes main-routes
             (GET "/" [] (index-page))
             (GET "/venues" [lat lon]
                (venues-page (location/venues lat lon foursquare/config)))
             (GET "/venue/:id" [id]
                (venue-page (location/venue id foursquare/config)))
             (POST "/venue/:id/accessible" [id venue-name]
                (location/make-accessible id venue-name)
                (redirect (str "/venue/" id)))
             (POST "/venue/:id/not-accessible" [id venue-name]
                (location/make-inaccessible id venue-name)
                (redirect (str "/venue/" id)))
             (route/resources "/")
             (route/not-found "Page not found"))

(def app
    (-> (handler/site main-routes)
            (wrap-base-url)))
