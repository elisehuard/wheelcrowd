(ns wheelcrowd.test.views
  (:use [wheelcrowd.views])
  (:use [clojure.test]))

; more like smoke tests to check whether there's a big fat bug

(deftest index-page-test
  (is (not (= (index-page) nil))))

(deftest venues-page-test
  (is (not (= (venues-page [{:id "abc123" :name "home" :categories []}]) nil))))

(deftest attr-checked-test
   (is (= (attr-checked {:type "radio" :name "accessible" :id "accessible-nil" :value "nil"} {:accessible nil} nil) 
          {:type "radio" :name "accessible" :id "accessible-nil" :value "nil" :checked "checked"})))

(deftest venue-page-test
  (is (not (nil? (re-find #"flat surface" (venue-page {:id "ABCD" :name "Da Place" :accessible true :comments [{:text "flat surface, all good"},{:text "couldn't be better"}]}))))))
