
(ns app.updater.question
  (:require [app.schema :as schema] [medley.core :refer [dissoc-in]]))

(defn add-question [db op-data sid op-id op-time session user]
  (assoc-in
   db
   [:questions op-id]
   (merge schema/question {:id op-id, :author-id (:id user), :title op-data, :time op-time})))

(defn add-reply [db op-data sid op-id op-time session user]
  (let [qid (:question-id op-data), content (:content op-data)]
    (assoc-in
     db
     [:questions qid :replies op-id]
     (merge
      schema/reply
      {:id op-id, :content content, :time op-time, :author-id (:id user)}))))

(defn rm-question [db op-data sid op-id op-time session user]
  (dissoc-in db [:questions op-data]))

(defn rm-reply [db op-data sid op-id op-time session user]
  (dissoc-in db [:questions (:question-id op-data) :replies (:id op-data)]))

(defn update-question [db op-data sid op-id op-time session user]
  (update-in db [:questions (:id op-data)] (fn [q] (merge q op-data))))

(defn update-reply [db op-data sid op-id op-time session user]
  (update-in
   db
   [:questions (:question-id op-data) :replies (:id op-data)]
   (fn [r] (assoc r :content (:content op-data)))))
