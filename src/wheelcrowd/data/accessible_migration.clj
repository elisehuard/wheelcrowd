(ns wheelcrowd.data.accessible-migration
  (:use [wheelcrowd.data.configuration])
  (require [clojure.java.jdbc :as sql]))


(defn transform-accessible[accessible]
  (cond (= accessible true) "yes"
        (= accessible false) "no"
        (nil? accessible) nil))

(defn change-rating-to-varchar[]
  "change rating from boolean to varchar value"
  []
  (let [rating-rows
          (sql/with-connection postgres-db
            (sql/with-query-results results
              [(str "select * from rating;")]
              (into [] results)))]
    (map (fn[row]
            (sql/with-connection postgres-db
              (sql/update-values
                :rating
                ["foursquare_id=?" (row :foursquare_id)]
                {:wheelchair_accessible (transform-accessible (row :accessible))})))
          rating-rows)))

(defn -main[]
  (change-rating-to-varchar))
