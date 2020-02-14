(ns soul-talk.sub.resource
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

(reg-sub :model/raw orm/raw>)

(reg-sub :model/all orm/all>)

(reg-sub :model/find-by orm/find-by>)

(reg-sub :model/find-id orm/find-id>)

(reg-sub :model/view-state orm/view-state>)

(reg-sub
 :metadata/all
 (fn [db [_]]
   (get db :metadata)))

(reg-sub
 :metadata/table
 (fn [db  [_  model-key]]
   (let [meta-path (path/->meta-path model-key)]
     (get-in db meta-path))))

(reg-sub
 :metadata/columns
 (fn [db  [_  model-key]]
   (let [meta-path (path/->meta-path model-key)
         table-columns (get-in db meta-path)]
     (-> table-columns
         vals
         ))))

(reg-sub
 :metadata/views.table-columns
 (fn [db  [_  model-key]]
   (let [meta-path (path/->meta-path model-key)
         table-columns (get-in db meta-path)]
     (-> table-columns
         vals
         (->>
          (map (fn [x] {:key (:column_name x)
                        :dataIndex (:column_name x)
                        :title (or (:view_title x)
                                   (:column_comment x)
                                   (:column_name x))})))))))



;; (reg-sub
;;  :model/filter
;;  (fn [db [_ model-key filter-fns]]
;;    (let [data-path (path/->data-path model-key)]
;;      (->>  (get-in db data-path)
;;            (filter (comp  filter-fns))))))

;; (reg-sub
;;  :model/unique
;;  (fn [db [_ model-key field-key]]
;;    (let [model (get model-register model-key)
;;          data-path (get model :data-path)]
;;      (->>  (get-in db data-path)
;;            (map (fn [x] (get x field-key)))
;;            set))))








