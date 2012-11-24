(ns wheelcrowd.test.data.configuration
  (:use [wheelcrowd.data.configuration]
        [clojure.test])
  (require [clojure.java.jdbc :as sql]))

(deftest database-smoketest
  (testing "checking if database connection works with generic query"
    (is (not (empty?
      (sql/with-connection postgres-db
        (into []
          (resultset-seq
            (-> (sql/connection)
              (.getMetaData)
              (.getTables nil nil nil (into-array ["TABLE" "VIEW"])))))))))))
