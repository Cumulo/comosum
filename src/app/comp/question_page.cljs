
(ns app.comp.question-page
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core :refer [defcomp <> list-> >> div span button]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo.comp.space :refer [=<]]
            [app.config :refer [dev?]]
            [app.config :as config]
            ["dayjs" :as dayjs]
            [feather.core :refer [comp-icon]]
            [respo-alerts.core :refer [use-confirm use-prompt]]
            [clojure.string :as string]))

(defcomp
 comp-question-page
 (states question)
 (let [edit-title (use-prompt
                   (>> states :title)
                   {:initial (:title question), :text "Change title"})
       confirm-remove (use-confirm
                       (>> states :remove)
                       {:text "Sure to remove this question?"})]
   (div
    {:style {:padding "8px 16px"}}
    (div
     {:style ui/row-parted}
     (div
      {:style ui/row-middle}
      (<> (:title question))
      (=< 8 nil)
      (comp-icon
       :edit
       {:font-size 16, :color (hsl 200 80 68), :cursor :pointer}
       (fn [e d!]
         ((:show edit-title)
          d!
          (fn [text]
            (when-not (string/blank? text)
              (d! :question/update {:id (:id question), :title text})))))))
     (comp-icon
      :x
      {:font-size 16, :color (hsl 0 80 68), :cursor :pointer}
      (fn [e d!]
        ((:show confirm-remove)
         d!
         (fn [] (d! :question/rm (:id question)) (d! :router/change {:name :home}))))))
    (div
     {}
     (<>
      (-> (dayjs (:time question)) (.format "YYYY-MM-DD HH:mm"))
      {:color (hsl 0 0 80), :font-family ui/font-fancy}))
    (if (empty? (:replies question))
      (div {:style (merge ui/center {:color (hsl 0 0 80)})} (<> "No replies"))
      (list->
       {}
       (->> (:replies question) (map (fn [[rid reply]] (rid (div {} (<> "TODO reply"))))))))
    (:ui confirm-remove)
    (:ui edit-title))))
