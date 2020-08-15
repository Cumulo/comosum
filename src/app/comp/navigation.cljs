
(ns app.comp.navigation
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.comp.space :refer [=<]]
            [respo.core :refer [defcomp >> <> span div button]]
            [clojure.string :as string]
            [app.config :as config]
            [respo-alerts.core :refer [use-prompt]]))

(defcomp
 comp-navigation
 (states logged-in? count-members)
 (let [add-question (use-prompt (>> states :new) {:title "New question"})]
   (div
    {:style (merge
             ui/row-center
             {:height 48,
              :justify-content :space-between,
              :padding "0 16px",
              :font-size 16,
              :border-bottom (str "1px solid " (hsl 0 0 0 0.1)),
              :font-family ui/font-fancy})}
    (div
     {:on-click (fn [e d!] (d! :router/change {:name :home})), :style {:cursor :pointer}}
     (<> (:title config/site) nil)
     (=< 8 nil)
     (button
      {:style ui/button,
       :inner-text "Question",
       :on-click (fn [e d!]
         ((:show add-question)
          d!
          (fn [text] (when-not (string/blank? text) (d! :question/add text)))))}))
    (div
     {:style {:cursor "pointer"},
      :on-click (fn [e d!] (d! :router/change {:name :profile}))}
     (<> (if logged-in? "Me" "Guest"))
     (=< 8 nil)
     (<> count-members))
    (:ui add-question))))
