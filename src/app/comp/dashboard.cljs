
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
  {:style {:padding "8px 16px"}}
  (list->
   {}
   (->> questions-dict
        (sort-by (fn [[qid question]] (unchecked-negate (:time question))))
        (map
         (fn [[qid question]]
           [qid
            (div
             {:style {:font-size 16, :cursor :pointer},
              :on-click (fn [e d!]
                (d! :router/change {:name :question, :data (:id question)}))}
             (<>
              (-> (dayjs (:time question)) (.format "MM-DD HH:mm"))
              {:color (hsl 0 0 80), :font-family ui/font-fancy})
             (=< 8 nil)
             (<> (:title question)))]))))))
