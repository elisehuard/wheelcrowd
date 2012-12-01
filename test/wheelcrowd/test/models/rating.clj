(ns wheelcrowd.test.models.rating
  (:require [wheelcrowd.models.rating :as rating])
  (:use [clojure.test]))

(deftest retrieve-test-if-nil
  (is (= (rating/retrieve nil) nil)))

(def first_of_december
  (java.sql.Timestamp. (.getTime (.parse (java.text.SimpleDateFormat. "dd/MM/yyyy") "01/12/2012"))))

(deftest format-timestamp-test
  (is (= (rating/format-timestamp {:created_at first_of_december}) {:created_at "01 Dec 2012 00:00"})))
