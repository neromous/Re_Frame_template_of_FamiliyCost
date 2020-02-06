(ns soul-talk.sub.page-state
  (:require [soul-talk.models :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.util.query-filter :as query-filter]))

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



