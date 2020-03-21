(ns soul-talk.modules.mi.models.task
  (:require
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [re-posh.core :as rd]
   [soul-talk.util.reframe-helper :refer
    [sub> act>  to-columns fix-key fix-value]]))

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



