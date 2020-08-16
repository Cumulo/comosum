
(ns app.updater.question
  (:require [app.schema :as schema] [medley.core :refer [dissoc-in]]))

(defn add-question [db op-data sid op-id op-time session user]
  (assoc-in
   db
   [:questions op-id]
   (merge schema/question {:id op-id, :author-id (:id user), :title op-data, :time op-time})))

(defn rm-question [db op-data sid op-id op-time session user]
  (dissoc-in db [:questions op-data]))

(defn update-question [db op-data sid op-id op-time session user]
  (update-in db [:questions (:id op-data)] (fn [q] (merge q op-data))))
