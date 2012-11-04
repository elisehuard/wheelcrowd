(ns wheelcrowd.test.location.foursquare
  (:use [wheelcrowd.location.foursquare])
  (:use [wheelcrowd.test.location.fixture])
  (:use [clojure.test]))

(deftest search-request-test
  (is (= (search-request 10.0 12.2 {'client-id "ABC" 'client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&client_id=ABC&client_secret=DEF")))

(deftest search-test
  (with-redefs [search-call (fn[a b c] foursquare-response)]
    (is (= (count (search 51.533599 -0.0937594 config)) 30))))

(deftest relevant-details-test
  (let [details (relevant-details single-location)]
    (is (= (details :id) "4e2442cdd4c0d325910996f3"))
    (is (= (details :name) "Anussage building"))
    (is (= (details :categories) []))))
