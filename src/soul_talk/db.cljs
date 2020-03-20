(ns soul-talk.db
  (:require  [reagent.core :as r]
             [soul-talk.util.date-utils :as du]
             [soul-talk.model-schema :refer [schame]]
             [soul-talk.util.reframe-helper :refer
              [sub> act> dsub dget dset to-columns fix-key fix-value]]
             [datascript.core :as d]
             [re-posh.core :as rd]))

(defonce conn (d/create-conn schame))
(rd/connect! conn)

(def default-db
  (->   {:active {}
         :breadcrumb ["Home"]
         :login-events []}))

(defonce unique-work (r/atom 0))

(defn unique-id []
  (swap! unique-work inc))

(goog-define api-uri "http://localhost:3000/api/v1")

;; (d/q '[:find (pull ?e [*])
;;        :in $ ?id
;;        :where [?e :order/eid ?id]]
;;      @conn 1)


;; (d/q '[:find (pull ?ee [*])
;;        :in $ ?id
;;        :where
;;        [?e :order/eid ?id]
;;        [?e :order/ref.order_number ?num]
;;        [?num :order/ref.customer ?ee]]
;;      @conn 1)

