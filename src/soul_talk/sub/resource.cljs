(ns soul-talk.sub.resource
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
 :resource/columns
 :<- [:metadata/all]
 (fn [all-meta  [_  model-key]]
   (let [model (get model-register model-key)
         table_name (get model :table_name)]
     (->> (get-in all-meta [table_name])))))

(reg-sub
 :resource/view.table-columns
 :<- [:metadata/all]
 (fn [all-meta  [_  model-key]]
   (let [model (get model-register model-key)
         table_name (get model :table_name)]
     (-> (get-in all-meta [table_name])
         vals
         (->>
          (map (fn [x] {:key (:column_name x)
                        :dataIndex (:column_name x)
                        :title (or (:view_title x)
                                   (:column_comment x)
                                   (:column_name x))})))
         ;;
         ))))

(reg-sub
 :resource/view-state
 (fn [db [_ model-key]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)
         view-path (get model :view-path)]
     (get-in db view-path)
     ;;
     )))

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

(reg-sub
 :resource/find_by-order_detail_id
 (fn [db [_ model-key id]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (->>  (get-in db data-path)
           (filter  #(= (:order_detail_id  %)  id))))))

(reg-sub
 :resource/unique
 (fn [db [_ model-key field-key]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (->>  (get-in db data-path)
           (map (fn [x] (get x field-key)))
           set
           ;;
           ))))







