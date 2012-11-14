(ns wheelcrowd.test.data.rating
  (:use [wheelcrowd.data.rating])
  (:use [clojure.test]))

(deftest data-smoketest
  "checking if data connection works"
  (is (= (get-rating "boo") [])))
