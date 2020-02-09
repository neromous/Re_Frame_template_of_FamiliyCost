(ns soul-talk.sub.resource-api
  (:require [soul-talk.db :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.util.query-filter :as query-filter]
            [soul-talk.sub.funcs.orm :as orm]
            [soul-talk.sub.funcs.path :as path]))


(reg-sub :resource-api/raw orm/raw>)

(reg-sub :resource-api/all orm/all>)

(reg-sub :resource-api/find-by orm/find-by>)

(reg-sub :resource-api/find-id orm/find-id>)

(reg-sub :resource-api/view-state orm/view-state>)

(reg-sub
 :resource-api/columns
 :<- [:metadata/all]
 (fn [all-meta  [_  model-key]]
   (let [model (get model-register model-key)
         table_name (get model :table_name)]
     (->> (get-in all-meta [table_name])))))

(reg-sub
 :resource-api/view.table-columns
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
 :resource-api/filter
 (fn [db [_ model-key filter-fns]]
   (let [data-path (path/->data-path model-key)]
     (->>  (get-in db data-path)
           (filter (comp  filter-fns))))))

(reg-sub
 :resource-api/unique
 (fn [db [_ model-key field-key]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)]
     (->>  (get-in db data-path)
           (map (fn [x] (get x field-key)))
           set))))








