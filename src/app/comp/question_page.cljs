
(ns app.comp.question-page
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core :refer [defcomp <> list-> >> div span button a]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo.comp.space :refer [=<]]
            [app.config :refer [dev?]]
            [app.config :as config]
            ["dayjs" :as dayjs]
            [feather.core :refer [comp-icon]]
            [respo-alerts.core :refer [use-confirm use-prompt]]
            [clojure.string :as string]))

(defcomp
 comp-reply
 (states question-id reply)
 (let [edit-reply (use-prompt
                   (>> states :edit)
                   {:text "Edit reply", :initial (:content reply), :multiline? true})
       confirm-remove (use-confirm (>> states :remove) {:text "Sure to remove reply?"})]
   (div
    {:style (merge ui/row-parted {:margin "16px 0px"})}
    (div
     {}
     (<> (:content reply))
     (=< 8 nil)
     (comp-icon
      :edit
      {:font-size 14, :color (hsl 200 80 70), :cursor :pointer}
      (fn [e d!]
        ((:show edit-reply)
         d!
         (fn [text]
           (when-not (string/blank? text)
             (d!
              :question/update-reply
              {:question-id question-id, :id (:id reply), :content text}))))))
     (=< 16 nil)
     (<>
      (-> reply :time dayjs (.format "MM-DD HH:mm"))
      {:color (hsl 0 0 80), :font-family ui/font-fancy}))
    (comp-icon
     :x
     {:font-size 14, :color (hsl 0 80 70), :cursor :pointer}
     (fn [e d!]
       ((:show confirm-remove)
        d!
        (fn [] (d! :question/rm-reply {:question-id question-id, :id (:id reply)})))))
    (:ui edit-reply)
    (:ui confirm-remove))))

(defcomp
 comp-question-page
 (states question)
 (let [edit-title (use-prompt
                   (>> states :title)
                   {:initial (:title question), :text "Change title"})
       edit-desc (use-prompt
                  (>> states :desc)
                  {:initial (:description question),
                   :text "Change description",
                   :multiline? true})
       confirm-remove (use-confirm
                       (>> states :remove)
                       {:text "Sure to remove this question?"})
       add-reply (use-prompt
                  (>> states :reply)
                  {:initial "", :multiline? true, :text "Add reply"})]
   (div
    {:style {:padding "8px 16px"}}
    (div
     {:style ui/row-parted}
     (div
      {:style ui/row-middle}
      (<> (:title question) {:font-size 18})
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
     (let [desc (:description question)]
       (if (some? desc)
         (<> desc {:color (hsl 0 0 50)})
         (<> "No description" {:font-family ui/font-fancy, :color (hsl 0 0 80)})))
     (=< 8 nil)
     (comp-icon
      :edit
      {:font-size 16, :color (hsl 200 80 68), :cursor :pointer}
      (fn [e d!]
        ((:show edit-desc)
         d!
         (fn [text]
           (when-not (string/blank? text)
             (d! :question/update {:id (:id question), :description text})))))))
    (div
     {}
     (<>
      (-> (dayjs (:time question)) (.format "YYYY-MM-DD HH:mm"))
      {:color (hsl 0 0 80), :font-family ui/font-fancy}))
    (=< nil 16)
    (div
     {}
     (a
      {:style ui/link,
       :inner-text "Add reply",
       :on-click (fn [e d!]
         ((:show add-reply)
          d!
          (fn [text]
            (when-not (string/blank? text)
              (d! :question/add-reply {:question-id (:id question), :content text})))))}))
    (if (empty? (:replies question))
      (div {:style (merge ui/center {:color (hsl 0 0 80)})} (<> "No replies"))
      (list->
       {}
       (->> (:replies question)
            (map (fn [[rid reply]] [rid (comp-reply (>> states rid) (:id question) reply)])))))
    (:ui confirm-remove)
    (:ui edit-title)
    (:ui add-reply)
    (:ui edit-desc))))
