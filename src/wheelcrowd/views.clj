(ns wheelcrowd.views
  (:use [wheelcrowd.helpers])
  (:use [hiccup core page]))

(defn layout [body]
  (html5
    [:head
      [:title "Wheelcrowd"]
        [:link {:href "http://fonts.googleapis.com/css?family=Seymour+One" :rel "stylesheet" :type "text/css"}]
        (include-css "/css/reset.css")
        (include-css "/css/style.css")
        (include-css "http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.css")
        (include-js "http://code.jquery.com/jquery-1.8.2.min.js")
        (include-js "http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.js")
        (include-js  "/js/geolocation.js")]
      [:body
        [:div {:data-role "page"}
          [:div {:data-role "header"}
            [:h1 "Wheelcrowd"]]
          [:div {:data-role "content"}
            body]
          [:div.footer "Data from Foursquare"]]]))

(defn index-page []
  (layout 
    [:form {:action "/venues" :method "post" :id "explore" :class "search-wrapper"}
      [:input {:type "text" :name "query" :placeholder "near me" :class "search-field"}]
      [:input {:type "submit" :value "explore" :class "search-button ocean"}]]))

(defn accessible-image[value]
   ({true "accessible.png", false "non-accessible.png", nil "unknown.png"} value))

(defn single-venue [location]
  [:li.location
    [:a {:href (str "/venue/" (location :id)) } [:span.name (location :name)]] [:span (str (location :distance) "m")]
    [:span.accessible [:img {:src (str "/images/" (accessible-image (location :accessible)))}]]])

(defn venues-page [locations]
  (layout [:ul {:data-role "listview" :data-inset "true" :data-filter "true"}
            (map single-venue locations)]))


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
