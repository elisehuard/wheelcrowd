(ns wheelcrowd.test.location.foursquare
  (:use [wheelcrowd.location.foursquare])
  (:use [wheelcrowd.test.location.fixture])
  (:use [clojure.test]))

(deftest search-request-test
  (is (= (search-request 10.0 12.2 {:client-id "ABC" :client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&client_id=ABC&client_secret=DEF")))

(deftest search-test
  (with-redefs [search-call (fn[a b c] foursquare-response)]
    (is (= (count (search 51.533599 -0.0937594 config)) 30))))

(deftest relevant-details-test
  (let [details (search-relevant-details single-location)]
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

(deftest tips-test
  (with-redefs [search-call (fn[a b] tips-response)]
    (is (= (tips "4ace6a86f964a52072d020e3" config) nil))))

(deftest tips-conclusion-test
  (is (= (tips-conclusion [{:created 123 :accessible true}]) {:created 123 :accessible true}))
  (is (= (tips-conclusion [{:created 123 :accessible false}]) {:created 123 :accessible false}))
  (is (= (tips-conclusion [{:created 123 :accessible false},{:created 456 :accessible nil}]) {:created 123 :accessible false}))
  (is (= (tips-conclusion [{:created 123 :accessible false},{:created 456 :accessible true}]) {:created 456 :accessible true})))

