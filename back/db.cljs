(ns soul-talk.db
  (:require  [reagent.core :as r]))

(def default-db
  {:active-page :home
   :pagination {:page     1
                :pre-page 6}
   :breadcrumb ["Home"]
   :login-events []})

(defonce unique-work (r/atom 0))
(defn unique-id []
  (swap! unique-work inc))

(goog-define api-uri "http://localhost:8000/api/v1")
;;(goog-define api-uri "http://localhost:3000/api")

(defmulti pages (fn [page _] page))
