(ns wheelcrowd.test.location.foursquare
  (:use [wheelcrowd.location.foursquare])
  (:use [wheelcrowd.test.location.fixture])
  (:use [clojure.test]))

(deftest search-request-test
  (is (= (search-request 10.0 12.2 nil nil {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&client_id=ABC&client_secret=DEF"))
  (is (= (search-request 10.0 12.2 "" nil {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&client_id=ABC&client_secret=DEF"))
  (is (= (search-request 10.0 12.2 "food" nil {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&query=food&client_id=ABC&client_secret=DEF"))
  (is (= (search-request 10.0 12.2 nil "category" {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&categoryId=category&client_id=ABC&client_secret=DEF")))

(deftest search-test
  (with-redefs [search-call (fn[a b c d e] foursquare-response)]
    (is (= (count (search 51.533599 -0.0937594 "" "" config)) 30))))

(deftest relevant-details-test
  (let [details (venue-relevant-details single-location)]
    (is (= (details :id) "4e2442cdd4c0d325910996f3"))
    (is (= (details :name) "Anussage building"))
    (is (= (details :distance) 276)) 
    (is (= (details :categories) []))
    (is (= (details :tip-count) 0))))

(deftest tips-request-test
  (is (= (tips-request "ABCDEF" {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/ABCDEF/tips?client_id=ABC&client_secret=DEF")))

(deftest tip-accessible-test
  (is (= (tip-accessible "great place #accesspass") "yes"))
  (is (= (tip-accessible "boo hiss #accessfail") "no"))
  (is (= (tip-accessible "nice coffee, bad service") nil)))

(deftest tips-accessible-test
  (with-redefs [tips-call (fn[a b] tips-response)]
    (is (= (tips-accessible? "4ace6a86f964a52072d020e3" config) nil))))

(deftest tips-conclusion-test
  (is (= (tips-conclusion [{:user "hola" :created 123 :accessible "yes"}])  {:foursquare-user "hola" :accessible "yes"}))
  (is (= (tips-conclusion [{:created 123 :accessible "no" :user "boo"}]) {:foursquare-user "boo" :accessible "no"}))
  (is (= (tips-conclusion [{:created 123 :accessible "no" :user "best"},{:created 456 :accessible nil :user "no"}]) {:foursquare-user "best" :accessible "no"}))
  (is (= (tips-conclusion [{:created 123 :accessible "no" :user "bar"},{:created 456 :accessible "yes" :user "baz"}]) {:foursquare-user "baz" :accessible "yes"})))

(deftest tip-information-test
  (is (= (tip-information {"id" "4fa5a094e4b0fd4c3f2fc248", "createdAt" 1336254612, "text" "Just what a pub should be.", "likes" {"count" 0, "groups" []}, "todo" {"count" 0}, "done" {"count" 1}, "user" {"contact" {}, "lastName" "L.", "gender" "male", "bio" "", "homeCity" "London, UK", "photo" "https://is1.4sqi.net/userpix_thumbs/JJMRY5FBXIAE1ETE.jpg", "tips" {"count" 8}, "id" "3580406","firstName" "Nicolas"}}) 
         {:text "Just what a pub should be." :user "3580406" :accessible nil :created 1336254612})))

(deftest categories-request-test
  (is (= (categories-request {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/categories?client_id=ABC&client_secret=DEF")))

(deftest category-data-test
  (testing "retrieves all category data from the data tree"
   (is (= (category-data {"id" "ABC" "shortName" "Food" "categories" [{"id" "DEF" "shortName" "Coffee" "categories" []}]}) '({:value "ABC" :label "Food"}, {:value "DEF", :label "Coffee"}) ))
   (is (= (category-data {"id" "ABC" "shortName" "Food" "categories" [{"id" "DEF" "shortName" "Coffee" "categories" []} {"id" "GHI" "shortName" "Games" "categories" []}]}) 
          '({:value "ABC" :label "Food"}, {:value "DEF", :label "Coffee"}, {:value "GHI" :label "Games"}) ))))
   (is (= (category-data {"id" "ABC" "shortName" "Food" "categories" [{"id" "DEF" "shortName" "Coffee" "categories" [{"id" "GHI" "shortName" "Games" "categories" []}]}]}) 
          '({:value "ABC" :label "Food"}, {:value "DEF", :label "Coffee"}, {:value "GHI" :label "Games"}) ))
