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
 :metadata/indexed
 :<- [:metadata/all]
 (fn [all-data [_]]
   (group-by (fn [x] [(get x :table_name) (get x :column_name)]) all-data)))

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
        (map #(get % :table_name))
        set
        sort)))

(reg-sub
 :metadata/table.columns
 :<- [:metadata/all]
 (fn [all-data [_  table-name]]
   (->>  (filter #(= (get % :table_name)  table-name)  all-data)
         (sort-by :column_name <))))

(reg-sub
 :metadata/table.column_name
 :<- [:metadata/all]
 (fn [all-data [_  table-name]]
   (->> all-data
        (filter #(= (get % :table_name)  table-name))
        (map :column_name))))

(reg-sub
 :metadata/column
 :<- [:metadata/indexed]
 (fn [all-data [_  table-name column-name]]
   (-> all-data
       (get [table-name column-name])
       first)))

(reg-sub
 :metadata/column.type
 :<- [:metadata/indexed]
 (fn [all-data [_  table-name column-name]]
   (->  all-data
        (get [table-name column-name])
        first
        :data_type)))

(reg-sub
 :metadata/column.comment
 :<- [:metadata/indexed]
 (fn [all-data [_  table-name column-name]]
   (->  all-data
        (get [table-name column-name])
        first
        :column_comment)))

(reg-sub
 :metadata/column.privileges
 :<- [:metadata/indexed]
 (fn [all-data [_  table-name column-name]]
   (->  all-data
        (get [table-name column-name])
        first
        :privileges)))

(reg-sub
 :metadata/column.unique
 :<- [:metadata/all]
 (fn [all-data [_ column-name]]
   (->>  all-data
         (map #(get % column-name))
         set
         sort)))

(reg-sub
 :metadata/cascader.option
 :<- [:metadata/all]
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
     (->> all-data
          (filter #(= (get % :table_name)  table-name))))))

(reg-sub
 :metadata/select-column
 :<- [:metadata/indexed]
 :<- [:page-state :metadata-index]
 (fn [[all-data page-state] [_]]
   (let [table-name  (:select-table page-state)
         column-name  (:select-column page-state)]
     (-> all-data
         (get [table-name column-name])
         first))))


;; (reg-sub
;;  :metadata/select-column
;;  ;;:<- [:metadata/all]
;;  ;;:<- [:page-state :metadata-index]

;;  :<- [:metadata/indexed]
;;  (fn [[all-data page-state] [_]]
;;    (let [table-name  (:select-table page-state)
;;          column-name  (:select-column page-state)]
;;      (->> all-data
;;           (filter #(= (get % :table_name)  table-name))
;;           (filter #(= (get % :column_name)  column-name))
;;           first))))








