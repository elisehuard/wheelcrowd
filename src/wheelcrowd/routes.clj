(ns wheelcrowd.routes
    (:use compojure.core
          wheelcrowd.views
         [hiccup.middleware :only (wrap-base-url)])
    (:require [compojure.route :as route]
              [compojure.handler :as handler]
              [compojure.response :as response]
              [wheelcrowd.location.foursquare :as foursquare]
              [wheelcrowd.location :as location]))

(defroutes main-routes
             (GET "/" [] (index-page))
             (GET "/venues" [lat lon]
                (venues-page (location/venues lat lon foursquare/config)))
             (route/resources "/")
             (route/not-found "Page not found"))

(def app
    (-> (handler/site main-routes)
            (wrap-base-url)))
