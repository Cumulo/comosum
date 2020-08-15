
(ns app.schema )

(def reply {:id nil, :time nil, :content nil, :related-ids {}, :author-id nil})

(def question
  {:id nil,
   :title nil,
   :time nil,
   :author-id nil,
   :description nil,
   :inspiration-id nil,
   :related-ids #{},
   :replies (do reply {})})

(def router {:name nil, :title nil, :data {}, :router nil})

(def session
  {:user-id nil,
   :id nil,
   :nickname nil,
   :router (do router {:name :home, :data nil, :router nil}),
   :messages {}})

(def user {:name nil, :id nil, :nickname nil, :avatar nil, :password nil})

(def database {:sessions (do session {}), :users (do user {}), :question (do question {})})
