(ns soul-talk.test
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [secretary.core :as secretary :refer-macros [defroute]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   soul-talk.resources
   soul-talk.subs
   soul-talk.handlers
   [accountant.core :as accountant]
   [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
   [soul-talk.util.query-filter :as query-filter]))

(subscribe [:page-state :index-detail])
(subscribe [:metadata/select-column])

 (subscribe [:product-task/by-order_detail_id 72])

(dispatch [:page-state :metadata-index :dasdf "dasdf"])
(dispatch [:page-state :sdasdf-index :dasdf "dasdf"])

(subscribe [:page-state])

(subscribe [:metadata/select-table_name])
(subscribe [:metadata/select-table])

(first @(subscribe [:metadata/all]))

(subscribe [:metadata/column.type "erp_goods" "goods_id"])

(first @(subscribe [:metadata/flatten]))

(subscribe [:metadata/column.unique :data_type])

(subscribe [:resource/columns :sys_org])

(dispatch [:metadata/new.table_name  "hahaha"])

(dispatch [:metadata/column.update  "erp_goods" "goods_hahah" {:column_name "daasdd"  }   ])
(subscribe [:metadata/all.view_type])

(subscribe [:metadata/relation.all])

(subscribe [:resource/find_by-order_detail_id :machine-resource 189])
(subscribe [:resource/all :machine-resource])

;; (first @(subscribe [:metadata/all]))
;; (subscribe [:metadata/column.comment  "ACT_GE_BYTEARRAY" "NAME_" ])
;; (subscribe [:metadata/table.column_name  "ACT_GE_BYTEARRAY"  ])

;; (subscribe [:metadata/column "erp_goods"  "create_name" ])


;; (dispatch [:metadata/update {:table_name "erp_goods"
;;                              :column_name "goods_name"
;;                              }    {:ttt "dasdfasd"}  ])

;; (-> @(subscribe [:full-order/view.index-page])
;;     first
;;     :flow_id
;;     )

;;(dispatch [:resource/server.query  :order-track {} ])
;; (-> @(subscribe [:product-order/tai_an ])
;;     count
;;     )


;; (run-events [[:resource/server.query :human-resource {"filters" [["=" "order_detail_id" 72]]}]
;;              [:resource/server.query :order-track {"limit" 15600}]

;;              [:resource/server.query :energy-oa {}]
;;              [:resource/server.query :machine-resource {"limit" 10000}]
;;              [:resource/server.query :material-craft {"filters" [["=" "order_detail_id" 72]]}]
;;              [:resource/server.query :material-raw {"filters" [["=" "order_detail_id" 72]]}]])


;;(dispatch  [:set-page-state :index-detail :order_detail_id 72])

;; (subscribe [:product-task/view.index-page])

;; (def sample @(subscribe [:full-order/all] ))

;; (count sample)


;; (->
;;  (filter #(query-filter/is-part-of-query? % {:order_id 1}  )    sample  )
;;  first
;;  )

;; (dispatch [:resource/update :order-track {:order_id 1 }  {:ttt "dadsd"}  ])

;;(dispatch [:resource/delete :order-track  {:order_id 1}])

;; (count @(subscribe [:resource/all :order-track]))


;; 初始化所有数据


;; (run-events
;;  [[:set-views :admin-active-model :material-raw]])

;; (subscribe [:views])


(subscribe [:metadata/column.unique :data_type])
;; => #<Reaction 191: ("bigint" "bit" "blob" "char" "date" "datetime" "decimal" "double" "float" "int" "longblob" "longtext" "smallint" "text" "timestamp" "tinyint" "varchar")>

