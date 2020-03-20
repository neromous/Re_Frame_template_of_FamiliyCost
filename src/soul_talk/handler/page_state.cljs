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
   [soul-talk.util.page-path :as path]
   [soul-talk.util.query-filter :as query-filter]))

;; 设置当前页
(reg-event-db
 :set-active-page
 (fn [db [_  x]]
   (assoc db  path/active-page x)))

(reg-event-db
 :page-state.reset
 (fn [db [_  page-key  page-state]]
   (assoc-in db (path/page->path page-key)  page-state)))

(reg-event-db
 :page-state.set
 (fn [db [_  page-key  k v]]
   (assoc-in db (path/page->state page-key k)  v)))



