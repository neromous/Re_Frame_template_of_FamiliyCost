(ns soul-talk.modules.relations.sub
  (:require  [re-frame.core :refer [inject-cofx
                                    dispatch
                                    dispatch-sync
                                    reg-event-db
                                    reg-event-fx
                                    subscribe reg-sub]]
             [soul-talk.utils :as utils]
             [soul-talk.util.query-filter :as query-filter]
             [soul-talk.util.data-formatter :as formatter]))

(reg-sub
 :relations/all
 :<- [:item/all :relations]
 (fn [all-data [_]]
   all-data))

(reg-sub
 :relations/table.all
 :<- [:relations/all]
 (fn [all-data [_]]
   (->>  (map :table_name all-data)
         set
         sort)))

(reg-sub
 :relations/table.columns
 :<- [:relations/all]
 (fn [all-data [_ table_name]]
   (->> (filter #(= (get % :table_name) table_name)  all-data)
        (map :column_name all-data)
        set
        sort)))

