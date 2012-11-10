(ns wheelcrowd.views
  (:use [hiccup core page]))

(defn layout [body]
  (html5
    [:head
      [:title "Wheelcrowd"]
        [:link {:href "http://fonts.googleapis.com/css?family=Seymour+One" :rel "stylesheet" :type "text/css"}]
        (include-css "/css/reset.css")
        (include-css "/css/style.css")
        (include-js  "/js/geolocation.js")]
      [:body
        [:div.header
          [:h1 "Wheelcrowd"]]
        body
        [:div.footer "Data from Foursquare"]]))

(defn index-page []
  (layout 
    [:fieldset
      [:ol
        [:a {:class "button" :id "explore"}
            [:button.native {:name "sync" :type "input"} "Explore"]]]]))

(defn accessibility[value]
   ({true "yes", false "no", nil "unknown"} value))

(defn single-venue [location]
  [:div.location
   [:div {:data-id (location :id) }
     [:a {:href (str "/venue/" (location :id)) } [:span.name (location :name)]]
     [:span (str (location :distance) "m")]
     [:span (str "Accessible: " (accessibility (location :accessible)))]]])

(defn venues-page [locations]
  (layout (map single-venue locations)))

(defn venue-page [location]
  (layout (single-venue location))) 
