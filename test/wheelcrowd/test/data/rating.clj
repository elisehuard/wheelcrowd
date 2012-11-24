(ns wheelcrowd.test.data.rating
  (:use [wheelcrowd.data.rating])
  (:use [clojure.test]))

(deftest boolerize-test
  (is (= (boolerize "false") false))
  (is (= (boolerize "true") true)))
