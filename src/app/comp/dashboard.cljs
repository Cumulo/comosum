
(ns app.comp.dashboard
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core :refer [defcomp <> list-> >> div span button]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo.comp.space :refer [=<]]
            [app.config :refer [dev?]]
            [app.config :as config]
            ["dayjs" :as dayjs]
            [feather.core :refer [comp-icon]]
            [respo-alerts.core :refer [use-prompt]]
            [clojure.string :as string]))

(defcomp
 comp-dashboard
 (states questions-dict query)
 (let [edit-query (use-prompt
                   (>> states :query)
                   {:text "Query keyword", :initial-text query})]
   (div
    {:style {:padding "8px 16px"}}
    (div
     {}
     (<> "Topics:")
     (=< 8 nil)
     (if (string/blank? query)
       (<> "show all" {:font-family ui/font-fancy, :color (hsl 0 0 80)})
       (<> query))
     (=< 8 nil)
     (comp-icon
      :edit
      {:font-size 14, :color (hsl 200 80 70), :cursor :pointer}
      (fn [e d!]
        ((:show edit-query)
         d!
         (fn [text] (d! :router/change {:name :home, :data {:query text}}))))))
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
               (<> (:title question)))]))))
    (:ui edit-query))))
