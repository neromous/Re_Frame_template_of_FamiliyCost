(ns soul-talk.db
  (:require  [reagent.core :as r]

             ;;
             ))

(goog-define api-uri "http://localhost:8000/api/v1")

(def default-db
  (->
   {:active {}
    :breadcrumb ["Home"]
    :login-events []}))

(def Env (r/atom  (sorted-map)))
