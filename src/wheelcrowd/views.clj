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
    [:form {:action "/venues" :method "post" :id "explore"}
      [:fieldset
        [:input {:type "text" :name "query"}]]
      [:fieldset
        [:input {:type "submit" :value "explore"}]]]))

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


(defn accessible-button [location]
  [:form {:action (str "/venue/" (location :id) "/accessible") :method "post"}
    [:input {:type "hidden" :name "venue-name" :value (location :name)}]
    [:input {:type "submit" :class "accessible" :value "accessible"}]])

(defn not-accessible-button [location]
  [:form {:action (str "/venue/" (location :id) "/not-accessible") :method "post"}
    [:input {:type "hidden" :name "venue-name" :value (location :name)}]
    [:input {:type "submit" :class "not-accessible" :value "not accessible"}]])

(defn show-venue [location]
  [:div.location
   [:div {:data-id (location :id) }
     [:a {:href (str "/venue/" (location :id)) } [:span.name (location :name)]]
     [:span (str "Accessible: " (accessibility (location :accessible)))]
     [:a {:href (str "https://foursquare.com/v/" (location :id)) } "Foursquare link"]
     [:div
       (accessible-button location)
       (not-accessible-button location)]]])

(defn venue-page [location]
  (layout (show-venue location))) 
