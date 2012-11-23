(ns wheelcrowd.test.categories
  (:use [wheelcrowd.categories]
        [clojure.test]))

(deftest filter-on-term-test
  (is (= (filter-on-term "Coff" '({:label "Coffee" :value "123"},{:label "Wine" :value "456"},{:label "Coffee and Cake" :value "789"}))
                                '({:label "Coffee" :value "123"},{:label "Coffee and Cake" :value "789"})))
  (is (= (filter-on-term "coff" '({:label "Coffee" :value "123"},{:label "Wine" :value "456"},{:label "Coffee and Cake" :value "789"}))
                                '({:label "Coffee" :value "123"},{:label "Coffee and Cake" :value "789"}))))
