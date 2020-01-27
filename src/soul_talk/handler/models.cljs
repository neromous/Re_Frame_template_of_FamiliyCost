(ns soul-talk.handler.models
  (:require [soul-talk.models :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe
                                   reg-sub]]
            [soul-talk.util.query-filter :as query-filter]))

(reg-event-db
 :resource/replace
 (fn [db [_ model-key data-vec]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (assoc-in db data-path  data-vec))))

(reg-event-db
 :resource/new
 (fn [db [_ model-key form]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (update-in db data-path  concat form))))

(reg-event-db
 :resource/delete
 (fn [db [_ model-key query]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (->> (get-in db data-path)
          (filter  #(query-filter/not-part-of-query? % query))))))

(reg-event-db
 :resource/update
 (fn [db [_ model-key query update-form]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (->> (get-in db data-path)
          (map (fn [x]
                 (if (query-filter/is-part-of-query? x query)
                   (merge x update-form)
                   query)))))))










