(ns soul-talk.sub.model
  (:require [soul-talk.models :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.util.query-filter :as query-filter]))

(reg-sub
 :resource/all
 (fn [db [_ model-key]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (get-in db data-path))))

(reg-sub
 :resource/filter
 (fn [db [_ model-key filter-fns]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (->>  (get-in db data-path)
           (filter (comp  filter-fns))))))

(reg-sub
 :resource/find-by
 (fn [db [_ model-key query]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (->>  (get-in db data-path)
           (filter  #(query-filter/is-part-of-query? % query))))))





