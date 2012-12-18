(ns wheelcrowd.core
    (:use [compojure.core :only [defroutes]])
    (:use [wheelcrowd.routes])
    (:require [compojure.route :as route]
              [compojure.handler :as handler]
              [ring.adapter.jetty :as ring]))

(defn start [port]
    (ring/run-jetty #'app {:port (or port 8080) :join? false}))

(defn -main []
    (let [port (Integer. (System/getenv "PORT"))]
          (start port)))
