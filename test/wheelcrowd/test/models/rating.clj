(ns wheelcrowd.test.models.rating
  (:require [wheelcrowd.models.rating :as rating])
  (:use [clojure.test]))

(deftest retrieve-test-if-nil
  (is (= (rating/retrieve nil) nil)))
