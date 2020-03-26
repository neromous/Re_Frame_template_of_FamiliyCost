(ns soul-talk.db
  (:require  [reagent.core :as r]
             [soul-talk.util.date-utils :as du]
             [soul-talk.model-schema :refer [schema]]
             [datascript.core :as d]
             [re-frame.core :as rf :refer [dispatch reg-fx reg-event-fx]]))
;; =================================
;; 初始化数据库
;; ==========================

(def datomic-db
  (let [init-schema schema]
    (d/empty-db init-schema)))

(def conn (d/conn-from-db datomic-db))

(reg-fx
 :datomic/transact
 (fn [tx-data]
   (d/transact! conn tx-data)))

(reg-event-fx
 :transact
 (fn [cofx [_ tx-data]]
   {:datomic/transact  tx-data}))

(reg-event-fx
 :add-field
 (fn [cofx [_ eid  attrib  value]]
   {:datomic/transact  [[:db/add eid  attrib value]]}))

(reg-event-fx
 :add-entity
 (fn [cofx [_ form]]
   {:datomic/transact  [form]}))

;; =================================
;; 初始化全局信息
;; ==========================


(def default-db
  {:active {}
   :breadcrumb ["Home"]
   :login-events []
   :conn conn})

(defonce unique-work (r/atom 0))

(defn unique-id []
  (swap! unique-work inc))

(goog-define api-uri "http://localhost:3000/api/v1")

