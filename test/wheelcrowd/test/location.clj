(ns wheelcrowd.test.location
  (:use [wheelcrowd.location])
  (:use [clojure.test])
  (:require [wheelcrowd.location.foursquare :as foursquare])
  (:require [wheelcrowd.data.rating :as data])
  (:require [wheelcrowd.models.rating :as rating]))

(deftest accessible-venue-test
  (with-redefs [rating/update-accessible (fn[a b c d] nil) ; no updates, we're testing the output value
                rating/retrieve (fn[v] nil)]             ; no tip in db to start with
    (testing "if no data is returned from db"
        (with-redefs [foursquare/tips-accessible? (fn[v c] nil)]
          (is (= (accessible-venue {:tip-count 0}) {:tip-count 0 :accessible nil})))
        (with-redefs [foursquare/tips-accessible? (fn[v c] {:foursquare-user "foo" :accessible true})]
          (is (= (accessible-venue {:tip-count 1}) {:tip-count 1 :accessible true})))))
  (with-redefs [rating/retrieve (fn[v] {:accessible false})]
    (testing "if data is returned from db"
        (with-redefs [foursquare/tips-accessible? (fn[v c] {:foursquare-user "moo" :accessible true})]
          (is (= (accessible-venue {:tip-count 1}) {:tip-count 1 :accessible false}))))))

(deftest accessible-venue-caching-test
  ; making sure we go through the update in some situations
  (with-redefs [rating/update-accessible (fn[a b c d] (throw Exception))
                rating/retrieve (fn[v] nil)]
    (testing "if no data returned from tip"
      (with-redefs [foursquare/tips-accessible? (fn[v c] nil)]
        (is (= (accessible-venue {:id "ABC" :tip-count 1}) {:id "ABC" :tip-count 1 :accessible nil})))
    (testing "if data returned from tip"
      (with-redefs [foursquare/tips-accessible? (fn[v c] {:foursquare-user "user" :accessible true})]
        (is (thrown? Exception (accessible-venue {:id "DEF" :tip-count 1}))))
      (with-redefs [foursquare/tips-accessible? (fn[v c] {:accessible false :foursquare-user "user2"})]
        (is (thrown? Exception (accessible-venue {:id "GHI" :tip-count 1}))))))))

(deftest cache-if-tip-present-test
  (with-redefs [data/create-or-update-rating (fn[a b c d]  d)]
    (is (= (cache-if-tip-present! nil {:id "x"}) nil))
    (is (= (cache-if-tip-present! {:foursquare-user "user" :accessible true} {:id "y"}) "user"))))

