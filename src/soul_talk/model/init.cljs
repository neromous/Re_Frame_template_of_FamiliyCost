(ns soul-talk.model.init
  (:require [re-frame.core :refer [subscribe
                                   reg-event-db
                                   dispatch
                                   reg-sub
                                   reg-event-fx]]))

(reg-sub
 :model.all
 (fn [db [_ model-name]]
   


   ))

(reg-sub
 :model.find_by
 (fn [db [_ model-name]]))

(reg-event-db
 :model.new
 (fn [db [_ model-name form]]
   db))

(reg-event-db
 :model.delete
 (fn [db [_ model-name ids]]
   db))

(reg-event-db
 :model.update
 (fn [db [_ model-name ids form]]
   db))








