(ns soul-talk.test
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [secretary.core :as secretary :refer-macros [defroute]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   soul-talk.subs
   soul-talk.handlers
   [accountant.core :as accountant]
   [soul-talk.util.route-utils :refer [run-events
                                       run-events-admin
                                       logged-in?
                                       navigate!]]
   [soul-talk.sub.funcs.product-series :as product-series]
   [soul-talk.util.query-filter :as query-filter]))

;; (dispatch [:sell-order.edn<- {:query '[:find ?e
;;                                :where [?ee :cost/machine.start-time ?e]
;;                                ]}  ])


;; (dispatch [:metadata.post<- {}])

(dispatch [:datomic/schema.edn<-])

(dispatch [:sell-detail.edn<- {:query '[:find  (pull ?e [*])
                                        :in $ [?id]
                                        :where
                                        [?e :sell-detail/detail_id ?id]]
                               :input [2]}])

(dispatch [:sell-order.edn<-])

(dispatch [:product-task.edn<-])
(first @(subscribe [:product-task.data]))
;;(dispatch [:product-flow.edn<-])
(subscribe [:db/schema])

(first  @(subscribe [:sell-detail.data]))
(first  @(subscribe [:sell-order.list]))

(first @(subscribe [:sell-detail.view/data]))

(dispatch [:flow.cost.transact
           {:query '[:find  (pull ?order [* {:sell-order/ref.order_company [*]
                                             :sell-order/ref.customer [*]}])
                     :in $ ?id
                     :where
                     [?detail :sell-detail/detail_id]
                     [?detail :sell-detail/ref.sell-order ?order]
                     [?task :product-task/ref.sell-detail ?detail]
                     [?cost :cost/ref.product-task ?task]]
            :input 200}])


(first @(subscribe [:flow.cost.data]))
;;(dispatch [:item/server.pull :metadata {:limit 1000} ];; )
;; (dispatch [:item/server.pull :product-track {:limit 1000} ])

;; (last @(subscribe [:item/all :metadata]))
;; (count @(subscribe [:item/all :relations]))

;; (dispatch [:item/server.pull :craft-material-info {:ids [1 ]}])
;; (dispatch [:item/server.pull :human-info {:ids [1 ]}])
;; (dispatch [:item/server.pull :machine-info {:ids [1 ]}])

;; (dispatch [:item/server.pull :order_number {:order_number "SO201907250001"}] )


;; (subscribe [:order_number/get :human])
;; (-> @(subscribe [:order_number ])
;;     keys
;;     )



;; (dispatch [:http/post :metadata ])

;; (dispatch [:edn/update '{:find [(pull ?eee [*]) ]
;;                          :where
;;                          [[?e :product-flow/ref.workshop ?p]
;;                           [?e :product-flow/flow_id 100]
;;                           [?ee :sell-detail/ref.product-flows ?e]
;;                           [?eee :sell-order/ref.sell-details ?ee]]}    ])
;;  (subscribe [:get :tttt])

;; (subscribe [:product-task/order_id 1])

;; (->> @(subscribe [:full-order/all])
;;     ;; product-series/order-company->TaiAn
;;     ;;product-series/tax_money
;;      (product-series/product-company->TaiAn)
;;      count)

;; (->> @(subscribe [:full-order/all])
;;      product-series/count-order_id)

;; (->  @(subscribe [:full-order/all])
;;      ((juxt product-series/count-order_id
;;             product-series/count-order_detail_id
;;             product-series/count-job_order_id
;;             product-series/count-flow_id
;;             product-series/tax_money
;;             product-series/order_detail_weight
;;             product-series/flow_final_weight
;;             product-series/flow_plan_release
;;             )))


;; (-> @(subscribe [:item/all :relations])
;;     first
;;     )
;; (subscribe [:metadata/table :todos])

;; (subscribe [:metadata/table-columns :todos])

;; (dispatch [:item/server.get :test])

;; (dispatch [:item/server.get :relation])

;; (subscribe [:item/raw :relation])

;; (subscribe [:item/raw :test])

;; (subscribe [:page-state])

;; (subscribe [:item/all :product-track])

;; (first @(subscribe [:product-task/all]))
;; (first @(subscribe  [:sell-order/all]))
;; (subscribe [:sell-order/all.flow_plan_release])

;; (subscribe [:sell-order/xin_tai.order_detail_weight])

;; (subscribe [:sell-order/all.flow_plan_release])

;; (-> (last @(subscribe [:product-order/all])))

;; (subscribe [:sell-order/all.job_order_weight])


;; (dispatch [:set-active-page :test])
;; (dispatch [:page-state.set  :test :haha "dd"  ])

;; (subscribe [:page-state.all])
;; (subscribe [:get-view])

;; @(subscribe [:resource-api/raw :todos])

;; (dispatch [:resource-api/server.pull :todos {}])
;; (dispatch [:resource-api/server.add :todos {:title "hahaha"}])
;; (subscribe [:resource-api/all :todos])
;; (dispatch [:resource-api/server.del :todos 2])
;; (dispatch [:resource-api/server.update :todos {:id 4 :title "再次测试"}])


;;  (subscribe [:model/view-state :todos])




;; (dispatch [:resource-api/server.add :todos {:title "dasdf"}])

;; (dispatch [:resource-api/delete :todos 9])

;; (dispatch [:resource-api/update :todos 0 {:haha "dfasdf"} ])

;;(dispatch [:resource-api/server.update  :todos {:id 12 :title "测试"} ])


;; (subscribe [:page-state :index-detail])
;; (subscribe [:metadata/select-column])

;;  (subscribe [:product-task/by-order_detail_id 72])

;; (dispatch [:page-state :metadata-index :dasdf "dasdf"])
;; (dispatch [:page-state :sdasdf-index :dasdf "dasdf"])

;; (subscribe [:page-state])

;; (subscribe [:metadata/select-table_name])
;; (subscribe [:metadata/select-table])

;; (first @(subscribe [:metadata/all]))

;; (subscribe [:metadata/column.type "erp_goods" "goods_id"])

;; (first @(subscribe [:metadata/flatten]))

;; (subscribe [:metadata/column.unique :data_type])

;; (subscribe [:resource/columns :sys_org])

;; (dispatch [:metadata/new.table_name  "hahaha"])

;; (dispatch [:metadata/column.update  "erp_goods" "goods_hahah" {:column_name "daasdd"  }   ])
;; (subscribe [:metadata/all.view_type])

;; (subscribe [:metadata/relation.all])

;; (subscribe [:resource/find_by-order_detail_id :machine-resource 189])
;; (subscribe [:resource/all :machine-resource])


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


;;(subscribe [:metadata/column.unique :data_type])
;; => #<Reaction 191: ("bigint" "bit" "blob" "char" "date" "datetime" "decimal" "double" "float" "int" "longblob" "longtext" "smallint" "text" "timestamp" "tinyint" "varchar")>

