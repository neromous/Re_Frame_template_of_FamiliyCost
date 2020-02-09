(ns soul-talk.handler.page-state
  (:require
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe
                                   reg-sub]]
            [reagent.core :as r]
            [soul-talk.util.query-filter :as query-filter]))


;; 设置当前页
(reg-event-db
 :set-active-page
 (fn [db [_  x]]
   (assoc-in db [:views  :active-page] x)))

(reg-event-db
 :set-views
 (fn [db [_  view-key view-value  ]]
   (let [
         ;;view-path (drop-last  kvs)
         ;;value  (last kvs)
         ]
     (assoc-in db  [:views view-key  ]  view-value))))

(reg-event-db
 :update-views
 (fn [db [_ view-path x]]
   (update-in db (concat [:views] view-path)  merge x)))



(reg-event-db
 :page-state
 (fn [db [_ page-key k v]]
   (assoc-in db  [:views :page-state page-key  k] v)))

(defn set-current-page-state [k v]
  (r/with-let [current-page (subscribe)]
    (dispatch [:page-state  @current-page k v])))


