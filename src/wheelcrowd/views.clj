(ns wheelcrowd.views
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
        (include-js "/js/controls.js")
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
    [:form {:action "/venues" :id "explore"}
      [:input {:type "search" :name "query" :placeholder "near me" :id "search-basic"}]]))

(defn accessible-image[value]
   ({true "accessible.png", false "non-accessible.png", nil "unknown.png"} value))

(defn single-venue [location]
  [:li.location
    [:a {:href (str "/venue/" (location :id)) } [:span.name (location :name)]] [:span (str (location :distance) "m")]
    [:span.accessible [:img {:src (str "/images/" (accessible-image (location :accessible)))}]]])

(defn venues-page [locations]
  (layout [:ul {:data-role "listview" :data-inset "true" :data-filter "true"}
            (map single-venue locations)]))


(defn attr-selected [location value]
  (if (= value (location :accessible))
    {:selected "selected" :value (pr-str value)}
    {:value (pr-str value)}))

(defn accessible-slider [location]
  [:form {:action (str "/venue/" (location :id)) :method "post"}
    [:input {:type "hidden" :name "venue-name" :value (location :name)}]
    [:div {:data-role "fieldcontain"}
      [:label {:for "accessible-flip"} "Accessible:"]
      [:select {:name "accessible-flip" :id "accessible-flip" :data-role "slider"}
        [:option (attr-selected location true) "Yes"]
        [:option (attr-selected location false) "No"]]]])

(defn show-venue [location]
  [:div.show-location
    [:a {:href (str "/venue/" (location :id)) } [:span.name (location :name)]]
    [:span.accessible [:img {:src (str "/images/" (accessible-image (location :accessible)))}]]
    [:div
      (accessible-slider location)]
    [:a {:class "foursquare" :href (str "https://foursquare.com/v/" (location :id)) } "Foursquare link"]])

(defn venue-page [location]
  (layout (show-venue location))) 
