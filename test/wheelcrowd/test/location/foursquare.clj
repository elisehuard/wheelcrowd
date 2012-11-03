(ns wheelcrowd.test.location.foursquare
  (:use [wheelcrowd.location.foursquare])
  (:use [clojure.test]))

(deftest search-request-test
  (is (= (search-request 10.0 12.2 {'client-id "ABC" 'client-secret "DEF"})
         "https://api.foursquare.com/v2/venues/search?ll=10.0,12.2&client_id=ABC&client_secret=DEF")))
