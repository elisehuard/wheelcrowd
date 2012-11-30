(ns wheelcrowd.models.comment
  (:require [wheelcrowd.data.comment :as comment-data]))

(defn new-comment [id text]
  (comment-data/new-comment {:id id :text text}))
