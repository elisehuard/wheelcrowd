(ns wheelcrowd.views
  (:use [hiccup core page]))

(def about-button
    [:a {:href "/about" :data-role "button" :data-mini "true" :class "about"} "About"])
(def home-button
    [:a {:href "/" :data-role "button" :data-mini "true" :data-icon "home"} "Home"])

(defn layout [body menu-button]
  (html5
    [:head
      [:title "Wheelcrowd"]
        [:link {:href "http://fonts.googleapis.com/css?family=Montserrat+Alternates|Raleway" :rel "stylesheet" :type "text/css"}]
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
        [:link {:rel "icon" :href "/images/favicon.png" :type "image/png"}]
        [:link {:rel "apple-touch-icon" :href "/images/apple-touch-icon-iphone.png"}]
        [:link {:rel "apple-touch-icon" :sizes "72x72" :href "/images/apple-touch-icon-ipad.png"}]
        [:link {:rel "apple-touch-icon" :sizes "114x114" :href "/images/apple-touch-icon-iphone4.png"}]
        [:link {:rel "apple-touch-icon" :sizes "144x144" :href "/images/apple-touch-icon-ipad-retina.png"}]
        (include-css "http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.css")
        (include-css "/css/style.css")
        (include-js "http://code.jquery.com/jquery-1.8.2.min.js")
        (include-js "http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.js")
        (include-js "/js/jqm.autoComplete-1.5.0-min.js")
        (include-js "/js/controls.js")
        (include-js  "/js/geolocation.js")
        (include-js  "/js/google-analytics.js")]
      [:body
        [:div {:data-role "page"}
          [:div {:data-role "header"}
            [:h1 [:a {:href "/"}  "Wheelcrowd"]]
            menu-button]
          [:div {:data-role "content"}
            body]
          [:div.footer
             (include-js "/js/images.js")
             [:img {:src "https://playfoursquare.s3.amazonaws.com/press/logo/poweredByFoursquare_16x16.png" :title "foursquare"}]
             "Powered by Foursquare"]]]))

(defn index-page []
  (layout 
    [:form {:action "/venues" :id "explore"}
      [:input {:type "search" :name "query" :placeholder "near me" :id "search-basic"}]
      [:p.categories_label "search on categories:"]
      [:div {:id "suggestions" :data-role "listview" :data-inset "true"}]]
    about-button))

(defn accessible-image[value]
   ({"yes" "accessible.png", "no" "non-accessible.png", "limited" "limited-accessible.png", nil "unknown.png"} value))

(defn single-venue [location]
  [:li.location
    [:a {:href (str "/venue/" (location :id)) :class "venue"}
      [:img {:src "/images/missing.png" :data-id (location :id) :class "photo"}]
      [:div.details
        [:span.name (location :name)]
        [:span.distance (str "(" (location :distance) "m)")]
        [:span.accessible [:img {:src (str "/images/" (accessible-image (location :accessible)))}]]]]])

(defn venues-page [locations]
  (layout [:div
             [:ul {:data-role "listview" :data-inset "true"} (map single-venue locations)]
             [:a {:href "/" :data-role "button" :data-icon "home" :data-inline "true"} "Home"]]
          about-button))


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
      [:input (attr-checked {:type "radio" :name "accessible" :id "accessible-yes" :value "yes"} location "yes")]
      [:label {:for "accessible-yes"} "Access"]
      [:input (attr-checked {:type "radio" :name "accessible" :id "accessible-limited" :value "limited"} location "limited")]
      [:label {:for "accessible-limited"} "Limited Access"]
      [:input (attr-checked {:type "radio" :name "accessible" :id "accessible-no" :value "no"} location "no")]
      [:label {:for "accessible-no"} "No Access"]]])

(defn show-venue [location]
  [:div.show-location
    [:a {:href (str "/venue/" (location :id)) } [:span.name (location :name)]]
    [:span.accessible [:img {:src (str "/images/" (accessible-image (location :accessible)))}]]
    [:div
      (accessible-radio location)]
    [:a {:class "back" :data-role "button" :data-icon "arrow-l" :data-inline "true"} "Back"]
    [:a {:class "foursquare" :href (str "https://foursquare.com/v/" (location :id)) } [:img {:src "/images/foursquare-36x36.png"}]]])

(defn venue-page [location]
  (layout (show-venue location) about-button)) 

(defn about-page []
  (layout [:div.about
             [:h2 "About"]
             [:p "Wheelcrowd is an app to find all the wheelchair accessible venues around us. It is entirely based on Foursquare data."]
             [:h3 "2 ways to help:"]
             [:ul
               [:li "indicate in the app whether you found a venue to be accessible"]
               [:li "leave tips in Foursquare, containing the tags #accesspass or #ap for accessible venues, #accessfail or #af for inaccessible venues, and #accesslimited or #al for venues with limited access."]]
             [:p "You can also leave comments in case those indications are not enough, as in 'downstairs is accessible, but the higher floor isn't'."]
             [:h3 "What does wheelchair accessible mean?"]
             [:p "Mostly, wheelchair-accessible means step-free. If there's one step, its doable, especially if the step is low and there are doorframes to hold on to, but that depends on the style of wheelchair. More steps is evidently impossible."]]
          home-button))
             
                
