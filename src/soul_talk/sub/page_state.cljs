(ns soul-talk.sub.page-state
  (:require
   [re-frame.core :refer [inject-cofx
                          dispatch
                          dispatch-sync
                          reg-event-db
                          reg-event-fx
                          subscribe reg-sub]]
   [soul-talk.util.page-path :as path]
   [soul-talk.util.query-filter :as query-filter]))

(reg-sub
 :active-page
 (fn [db _]
   (get db  path/active-page)))

(reg-sub
 :page-state.all
 (fn [db [_]]
   (get-in db path/page-prefix)))

(reg-sub
 :page-state
 (fn [db [_ page-key]]
   (get-in db (path/page->path page-key))))

(reg-sub
 :page-state.get
 (fn [db [_ page-key k]]
   (get-in db (path/page->state page-key k))))

(reg-sub
 :get-view
 :<- [:page-state.all]
 :<- [:active-page]
 (fn [[all-state active-page] _]
   {:active-page  active-page
    :page-state   (get all-state active-page)}))



