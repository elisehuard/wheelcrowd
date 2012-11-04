(ns wheelcrowd.test.views
  (:use [wheelcrowd.views])
  (:use [clojure.test]))

; more like smoke tests to check whether there's a big fat bug

(deftest index-page-test
  (is (not (= (index-page) nil))))

(deftest venues-page-test
  (is (not (= (venues-page [{:id "abc123" :name "home" :categories []}]) nil))))
