
(ns app.comp.dashboard
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core :refer [defcomp <> list-> >> div span button]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo.comp.space :refer [=<]]
            [app.config :refer [dev?]]
            [app.config :as config]
            ["dayjs" :as dayjs]))

(defcomp
 comp-dashboard
 (questions-dict)
 (div
  {:style {:padding "4px 8px"}}
  (list->
   {}
   (->> questions-dict
        (map
         (fn [[qid question]]
           [qid
            (div
             {}
             (<> (-> (dayjs (:time question)) (.format "MM-DD HH:mm")))
             (=< 8 nil)
             (<> (:title question)))]))))))
