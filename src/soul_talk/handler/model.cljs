(ns soul-talk.handler.model
  (:require [soul-talk.db :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe
                                   reg-sub]]
            [soul-talk.sub.funcs.path :as path]
            [soul-talk.util.query-filter :as query-filter]))

(reg-event-db
 :model/replace
 (fn [db [_ model-key data-vec]]
   (let [data-path (path/->data-path model-key)]
     (assoc-in db data-path  data-vec))))

(reg-event-db
 :model/new
 (fn [db [_ model-key form]]
   (let [data-path (path/->data-path model-key)
         id (:id form)]
     (assoc-in db (conj data-path id) form))))

(reg-event-db
 :model/delete
 (fn [db [_ model-key id]]
   (let [data-path (path/->data-path model-key)]
     (update-in db data-path dissoc id))))

(reg-event-db
 :model/update
 (fn [db [_ model-key id update-form]]
   (let [data-path (path/->data-path model-key)
         all-data (get-in db data-path)]
     (update-in db (conj data-path id)   merge update-form))))
