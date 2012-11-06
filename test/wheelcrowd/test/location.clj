(ns wheelcrowd.test.location
  (:use [wheelcrowd.location])
  (:use [clojure.test])
  (:require [wheelcrowd.location.foursquare :as foursquare]))

(deftest single-venue-test
  (with-redefs [foursquare/tips-accessible? (fn[v c] nil)]
    (is (= (single-venue {:tip-count 0}) {:tip-count 0 :accessible nil})))
  (with-redefs [foursquare/tips-accessible? (fn[v c] true)]
    (is (= (single-venue {:tip-count 1}) {:tip-count 1 :accessible true}))))
