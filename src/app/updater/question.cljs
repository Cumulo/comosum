
(ns app.updater.question (:require [app.schema :as schema]))

(defn add-question [db op-data sid op-id op-time session user]
  (assoc-in
   db
   [:questions op-id]
   (merge schema/question {:id op-id, :author-id (:id user), :title op-data, :time op-time})))
