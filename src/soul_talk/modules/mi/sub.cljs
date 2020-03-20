(ns soul-talk.modules.mi.sub
  (:require
   [reagent.core :as r]
   [re-posh.core :as rd]
   [re-frame.core :refer [subscribe reg-event-db
                          dispatch reg-sub reg-event-fx]]
   [soul-talk.util.reframe-helper :refer
    [sub> act> dsub dget dset to-columns fix-key fix-value]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]))

(rd/reg-query-sub
 :company.all.ids
 '[:find ?e
   :where
   [?e :org/eid]])

(rd/reg-sub
 :company.all
 :<- [:company.all.ids]
 (fn [ids _]
   {:type :pull-many
    :pattern '[*]
    :ids (reduce into []  ids)}))

(rd/reg-query-sub
 :order.all.ids
 '[:find ?e
   :where
   [?e :order/eid]])

(rd/reg-sub
 :order.all
 :<- [:order.all.ids]
 (fn [ids _]
   {:type :pull-many
    :pattern '[*]
    :ids (reduce into []  ids)}))

(rd/reg-query-sub
 :material.all.ids
 '[:find ?e
   :where
   [?e :material/eid]])

(rd/reg-sub
 :material.all
 :<- [:material.all.ids]
 (fn [ids _]
   {:type :pull-many
    :pattern '[*]
    :ids (reduce into []  ids)}))

(rd/reg-query-sub
 :task.all.ids
 '[:find ?e
   :where
   [?e :task/eid]])

(rd/reg-sub
 :task.all
 :<- [:task.all.ids]
 (fn [ids _]
   {:type :pull-many
    :pattern '[*]
    :ids (reduce into []  ids)}))
  
(rd/reg-query-sub
 :process.all.ids
 '[:find ?e
   :where
   [?e :process/eid]])

(rd/reg-sub
 :process.all
 :<- [:process.all.ids]
 (fn [ids _]
   {:type :pull-many
    :pattern '[*]
    :ids (reduce into []  ids)}))

(first @(sub> :process.all))


