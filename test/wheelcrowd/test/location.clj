(ns wheelcrowd.test.location
  (:use [wheelcrowd.location])
  (:use [clojure.test])
  (:require [wheelcrowd.location.foursquare :as foursquare])
  (:require [wheelcrowd.models.rating :as rating]))

(deftest accessible-venue-test
  (testing "if no data is returned from db"
    (with-redefs [rating/retrieve (fn[v] nil)]
      (with-redefs [foursquare/tips-accessible? (fn[v c] nil)]
        (is (= (accessible-venue {:tip-count 0}) {:tip-count 0 :accessible nil})))
      (with-redefs [foursquare/tips-accessible? (fn[v c] true)]
        (is (= (accessible-venue {:tip-count 1}) {:tip-count 1 :accessible true})))))
  (testing "if data is returned from db"
    (with-redefs [rating/retrieve (fn[v] {:accessible false})]
      (with-redefs [foursquare/tips-accessible? (fn[v c] true)]
        (is (= (accessible-venue {:tip-count 1}) {:tip-count 1 :accessible false}))))))
