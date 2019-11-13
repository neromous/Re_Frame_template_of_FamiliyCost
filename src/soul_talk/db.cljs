(ns soul-talk.db
  (:require  [reagent.core :as r]

             ;;
             ))

(goog-define api-uri "http://localhost:8000/api/v1")

(def default-db
  (->
   {:active {}
    :breadcrumb ["Home"]
    :login-events []}
   ;;(assoc :md/record Record)
   ;;(assoc :md/account Account)
   ;;注册account
   ))

(defonce unique-work (r/atom 0))
(defn unique-id []
  (swap! unique-work inc))


