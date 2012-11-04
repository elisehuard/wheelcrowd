(ns wheelcrowd.views
  (:use [hiccup core page]))

(defn layout [body]
  (html5
    [:head
      [:title "Wheelcrowd"]
        (include-css "/css/style.css")
        (include-js  "/js/geolocation.js")]
      [:body
        [:h1 "Wheelcrowd"]
        body]))

(defn index-page []
  (layout [:a {:href "/venues"} "explore"]))

(defn single-venue [location]
  [:div#location 
   [:div {:data-id (location :id) } (str (location :name) " " (location :distance) "m")]])

(defn venues-page [locations]
  (layout (map single-venue locations)))
