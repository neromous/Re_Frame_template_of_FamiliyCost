(ns soul-talk.handler.resource-api
  (:require [soul-talk.db :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe
                                   reg-sub]]
            [soul-talk.util.query-filter :as query-filter]))

(reg-event-db
 :resource-api/replace
 (fn [db [_ model-key data-vec]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (assoc-in db data-path  data-vec))))

(reg-event-db
 :resource-api/new
 (fn [db [_ model-key form]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)
         id (:id form)]
     (assoc-in db (conj data-path id) form))))

(reg-event-db
 :resource-api/delete
 (fn [db [_ model-key id]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (update-in db data-path dissoc id))))

(reg-event-db
 :resource-api/update
 (fn [db [_ model-key id update-form]]
   (let [model (get model-register model-key)
         all-data (get-in db data-path)]
     (update-in db (conj data-path id)   merge update-form))))
