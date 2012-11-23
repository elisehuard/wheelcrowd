(ns wheelcrowd.categories
    (:require [wheelcrowd.location.foursquare :as foursquare]))

(defn filter-on-term [term collection]
  (filter (fn[x] (not (nil? (re-find (java.util.regex.Pattern/compile (str "(?i)" term ".*")) (x :label))))) collection))

(defn find-category [term config]
  (filter-on-term term (foursquare/categories foursquare/config)))

