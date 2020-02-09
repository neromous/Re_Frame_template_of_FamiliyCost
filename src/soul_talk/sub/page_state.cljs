(ns soul-talk.sub.page-state
  (:require
   [re-frame.core :refer [inject-cofx
                          dispatch
                          dispatch-sync
                          reg-event-db
                          reg-event-fx
                          subscribe reg-sub]]
   [soul-talk.util.query-filter :as query-filter]))

(reg-sub
 :active-page
 (fn [db _]
   (get-in db [:views  :active-page])))

(reg-sub
 :views
 (fn [db [_]]
   (get db :views)))

(reg-sub
 :page-state
 (fn [db [_ & args]]
   (let []
     (get-in db  (concat  [:views :page-state]  args)))))

(reg-sub
 :current-page-state
 :<- [:active-page]
 :<- [:page-state]
 (fn [[current-page page-state] [_]]
   (get page-state current-page)))



