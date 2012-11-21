(ns wheelcrowd.test.location.foursquare
  (:use [wheelcrowd.location.foursquare])
  (:use [wheelcrowd.test.location.fixture])
  (:use [clojure.test]))

(deftest search-request-test
  (is (= (search-request 10.0 12.2 nil {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&client_id=ABC&client_secret=DEF"))
  (is (= (search-request 10.0 12.2 "" {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&client_id=ABC&client_secret=DEF"))
  (is (= (search-request 10.0 12.2 "food" {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&query=food&client_id=ABC&client_secret=DEF")))

(deftest search-test
  (with-redefs [search-call (fn[a b c d] foursquare-response)]
    (is (= (count (search 51.533599 -0.0937594 "" config)) 30))))

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
  (is (= (tip-accessible "great place #accesspass") true))
  (is (= (tip-accessible "boo hiss #accessfail") false))
  (is (= (tip-accessible "nice coffee, bad service") nil)))

(deftest tips-accessible-test
  (with-redefs [tips-call (fn[a b] tips-response)]
    (is (= (tips-accessible? "4ace6a86f964a52072d020e3" config) nil))))

(deftest tips-conclusion-test
  (is (= (tips-conclusion [{:created 123 :accessible true}])  true))
  (is (= (tips-conclusion [{:created 123 :accessible false}]) false))
  (is (= (tips-conclusion [{:created 123 :accessible false},{:created 456 :accessible nil}]) false))
  (is (= (tips-conclusion [{:created 123 :accessible false},{:created 456 :accessible true}]) true)))

(deftest categories-request-test
  (is (= (categories-request {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/categories?client_id=ABC&client_secret=DEF")))

(deftest category-data-test
  (testing "retrieves all category data from the data tree"
   (is (= (category-data {"id" "ABC" "name" "Food" "categories" [{"id" "DEF" "name" "Coffee" "categories" []}]}) [{:id "ABC" :name "Food"}, {:id "DEF", :name "Coffee"}] ))
   (is (= (category-data {"id" "ABC" "name" "Food" "categories" [{"id" "DEF" "name" "Coffee" "categories" []} {"id" "GHI" "name" "Games" "categories" []}]}) 
          [{:id "ABC" :name "Food"}, {:id "DEF", :name "Coffee"}, {:id "GHI" :name "Games"}] ))))
   (is (= (category-data {"id" "ABC" "name" "Food" "categories" [{"id" "DEF" "name" "Coffee" "categories" [{"id" "GHI" "name" "Games" "categories" []}]}]}) 
          [{:id "ABC" :name "Food"}, {:id "DEF", :name "Coffee"}, {:id "GHI" :name "Games"}] ))
