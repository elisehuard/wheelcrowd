(ns wheelcrowd.views
    (:use [hiccup core page]))

(defn index-page []
    (html5
          [:head
                 [:title "Hello World"]
                 (include-css "/css/style.css")
                 (include-js  "/js/geolocation.js")]
          [:body
                 [:h1 "Hello World"]]))
