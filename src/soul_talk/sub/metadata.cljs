(ns soul-talk.sub.metadata
  (:require [soul-talk.models :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.utils :as utils]
            [soul-talk.util.query-filter :as query-filter]))

(reg-sub
 :metadata/all
 (fn [db [_]]
   (get-in db  [:metadata])))

(reg-sub
 :metadata/relation.all
 (fn [db [_]]
   (let [all-data (get-in db  [:metadata.relation])
         is-sorted? (sorted? all-data)]
     all-data)))

(reg-sub
 :metadata/all.data_type
 (fn [db [_]]
   (get-in db  [:metadata.unique :data_type])))

(reg-sub
 :metadata/all.view_type
 (fn [db [_]]
   (get-in db  [:metadata.unique :view_type])))

(reg-sub
 :metadata/all.entity_type
 (fn [db [_]]
   (get-in db  [:metadata.unique :entity_type])))

(reg-sub
 :metadata/flatten
 :<- [:metadata/all]
 (fn [all-data [_]]
   (let [x (vals all-data)
         y (mapcat vals x)]
     y)))

(reg-sub
 :metadata/sample
 :<- [:metadata/all]
 (fn [all-data [_]]
   (first all-data)))

(reg-sub
 :metadata/all.table_names
 :<- [:metadata/all]
 (fn [all-data [_]]
   (->> all-data
        keys
        set
        sort)))

(reg-sub
 :metadata/table.columns
 :<- [:metadata/all]
 (fn [all-data [_  table-name]]
   (->>  (get all-data table-name))))

(reg-sub
 :metadata/table.column_name
 :<- [:metadata/all]
 (fn [all-data [_  table-name]]
   (->> (get all-data table-name)
        keys)))

(reg-sub
 :metadata/column
 :<- [:metadata/all]
 (fn [all-data [_  table-name column-name]]
   (-> all-data
       (get-in [table-name column-name]))
   ;;
   ))

(reg-sub
 :metadata/column.type
 :<- [:metadata/all]
 (fn [all-data [_  table-name column-name]]
   (-> all-data
       (get-in [table-name column-name])
       :data_type)))

(reg-sub
 :metadata/column.unique
 :<- [:metadata/flatten]
 (fn [all-data-list [_ column-type-name]]
   (->>  all-data-list
         (map #(get % column-type-name))
         set
         sort)))

(reg-sub
 :metadata/cascader.option
 :<- [:metadata/flatten]
 (fn [all-data [_]]
   (->  all-data
        (->>  (group-by #(get % :table_name))
              vals
              (map (fn [x] {:value (-> x first :table_name)
                            :label (-> x first :table_name)
                            :children (map (fn [y] {:value (get y :column_name)
                                                    :label (get y :column_name)}) x)}))
              (sort-by :value <)
              ;;
              ))))

(reg-sub
 :metadata/select-table_name
 (fn [db [_]]
   (get-in  db [:page-state  :metadata-index :select-table])))

(reg-sub
 :metadata/select-column_name
 (fn [db [_]]
   (get-in  db [:page-state :metadata-index :select-column])))

(reg-sub
 :metadata/select-table
 :<- [:metadata/all]
 :<- [:page-state :metadata-index]
 (fn [[all-data page-state] [_]]
   (let [table-name  (:select-table page-state)]
     (->> (get-in all-data  [table-name])))))

(reg-sub
 :metadata/select-column
 :<- [:metadata/all]
 :<- [:page-state :metadata-index]
 (fn [[all-data page-state] [_]]
   (let [table-name  (:select-table page-state)
         column-name  (:select-column page-state)]
     (-> all-data
         (get-in [table-name column-name])))))



;; (reg-sub
;;  :metadata/select-column
;;  ;;:<- [:metadata/all]
;;  ;;:<- [:page-state :metadata-index]

;;  :<- [:metadata/all]
;;  (fn [[all-data page-state] [_]]
;;    (let [table-name  (:select-table page-state)
;;          column-name  (:select-column page-state)]
;;      (->> all-data
;;           (filter #(= (get % :table_name)  table-name))
;;           (filter #(= (get % :column_name)  column-name))
;;           first))))








