(ns wheelcrowd.views
  (:use [hiccup core page]))

(defn layout [body]
  (html5
    [:head
      [:title "Wheelcrowd"]
        [:link {:href "http://fonts.googleapis.com/css?family=Montserrat+Alternates|Raleway" :rel "stylesheet" :type "text/css"}]
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
        (include-css "http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.css")
        (include-css "/css/style.css")
        (include-js "http://code.jquery.com/jquery-1.8.2.min.js")
        (include-js "http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.js")
        (include-js "/js/jqm.autoComplete-1.5.0-min.js")
        (include-js "/js/controls.js")
        (include-js  "/js/geolocation.js")]
      [:body
        [:div {:data-role "page"}
          [:div {:data-role "header"}
            [:h1 [:a {:href "/"}  "Wheelcrowd"]]]
          [:div {:data-role "content"}
            body]
          [:div.footer
             [:img {:src "https://playfoursquare.s3.amazonaws.com/press/logo/poweredByFoursquare_16x16.png" :title "foursquare"}]
             "Powered by Foursquare"]]]))

(defn index-page []
  (layout 
    [:form {:action "/venues" :id "explore"}
      [:input {:type "search" :name "query" :placeholder "near me" :id "search-basic"}]
      [:p.categories_label "search on categories:"]
      [:div {:id "suggestions" :data-role "listview" :data-inset "true"}]]))

(defn accessible-image[value]
   ({true "accessible.png", false "non-accessible.png", nil "unknown.png"} value))

(defn single-venue [location]
  [:li.location
    [:a {:href (str "/venue/" (location :id)) }
      [:span.name (location :name)]
      [:div.details
        [:span (str (location :distance) "m")]
        [:span.accessible [:img {:src (str "/images/" (accessible-image (location :accessible)))}]]]]])

(defn venues-page [locations]
  (layout [:div
             [:ul {:data-role "listview" :data-inset "true"} (map single-venue locations)]
             [:a {:href "/" :data-role "button" :data-icon "home" :data-inline "true"} "Home"]]))


(defn attr-checked [attrs location value]
  (if (= (location :accessible) value)
    (assoc attrs :checked "checked")
    attrs))

(defn accessible-radio [location]
  [:form {:action (str "/venue/" (location :id)) :method "post"}
    [:input {:type "hidden" :name "venue-name" :value (location :name)}]
    [:fieldset {:data-role "controlgroup" :id "accessible-radio"}
      [:input (attr-checked {:type "radio" :name "accessible" :id "accessible-nil" :value "nil"} location nil)]
      [:label {:for "accessible-nil"} "Don't know"]
      [:input (attr-checked {:type "radio" :name "accessible" :id "accessible-true" :value "true"} location true)]
      [:label {:for "accessible-true"} "Access"]
      [:input (attr-checked {:type "radio" :name "accessible" :id "accessible-false" :value "false"} location false)]
      [:label {:for "accessible-false"} "No Access"]]])

(defn show-venue [location]
  [:div.show-location
    [:a {:href (str "/venue/" (location :id)) } [:span.name (location :name)]]
    [:span.accessible [:img {:src (str "/images/" (accessible-image (location :accessible)))}]]
    [:div
      (accessible-radio location)]
    [:a {:class "back" :data-role "button" :data-icon "arrow-l" :data-inline "true"} "Back"]
    [:a {:class "foursquare" :href (str "https://foursquare.com/v/" (location :id)) } [:img {:src "/images/foursquare-36x36.png"}]]])

(defn venue-page [location]
  (layout (show-venue location))) 
