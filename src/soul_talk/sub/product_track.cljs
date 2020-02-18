(ns soul-talk.sub.product-track
  (:require  [re-frame.core :refer [inject-cofx
                                    dispatch
                                    dispatch-sync
                                    reg-event-db
                                    reg-event-fx
                                    subscribe reg-sub]]
             [soul-talk.utils :as utils]
             [soul-talk.util.query-filter :as query-filter]
             [soul-talk.sub.funcs.item-path :as item-path]))

;;========================================================
;; 全局数据初始化
;;========================================================

(reg-sub
 :full-order/all
 :<- [:item/all :product-track]
 (fn [all-order [_]]
   all-order))

;; 未参与重染订单
(reg-sub
 :full-order/no_re_dye
 :<- [:full-order/all]
 (fn [all-data [_]]
   (filter  #(-> % :is_re_dye (= 1)) all-data)))

;; 参与重染的订单
(reg-sub
 :full-order/re_dye
 :<- [:full-order/all]
 (fn [all-data [_]]
   (filter  #(-> % :is_re_dye (= 2)) all-data)))

;; 取一个样本
(reg-sub
 :full-order/sample
 :<- [:full-order/all]
 (fn [all-data [_]]
   (first all-data)))

;; 取样本的columns用于渲染
(reg-sub
 :full-order/table-columns
 :<- [:full-order/sample]
 (fn [samples [_]]
   (let [sample-keys  (keys samples)]
     (map (fn [x]  {:title (name x)
                    :dataIndex (name x)
                    :key (name x)})    sample-keys))))

;; 取一个字段的所有特殊值
(reg-sub
 :full-order/columns-unique-key
 :<- [:full-order/all]
 (fn [all-data [_ columns-key]]
   (->> all-data
        (map #(get % columns-key))
        set
        sort)))

;; 聚合函数
(reg-sub
 :full-order/group-by
 :<- [:full-order/all]
 (fn [all-data [_ columns-key]]
   (group-by #(get % columns-key) all-data)))

;; 采用query筛选
(reg-sub
 :full-order/filter
 :<- [:full-order/all]
 (fn [all-data [_ query]]
   (let [query-keys  (keys query)]
     (filter #(= (select-keys % query-keys)   query) all-data))))

;;===================================
;;    生产流转环节的资料汇总
;;=====================================

(reg-sub
 :product-task/all
 :<- [:item/all :product-track]
 (fn [all-detail [_]]
   (-> (map #(select-keys %  [:order_id
                              :order_detail_id
                              :job_order_id
                              :flow_id
                              :sheet_id
                              :order_number
                              :dyelot_number
                              :order_company_id
                              :order_saler_id
                              :goods_id
                              :goods_name
                              :color_number
                              :customer_color
                              :customer_name
                              :customer_id
                              :order_detail_weight
                              :job_order_weight
                              :flow_plan_release
                              :flow_final_weight
                              ;; 地点信息
                              :company_id
                              :factory_id
                              :workshop_id])  all-detail)
       set)))

;; 按照id查询
(reg-sub
 :product-task/by-order_detail_id
 :<- [:product-task/all]
 (fn [all-data [_ id]]
   (-> (filter #(= (:order_detail_id %)  id)  all-data))))

;;  泰安公司相关

(reg-sub
 :product-task/tai_an
 :<- [:product-task/all]
 (fn [all-order [_]]
   (let [org-id "3"]
     (filter #(-> % :order_company_id (= org-id)) all-order))))

;;  新泰公司相关
(reg-sub
 :product-task/xin_tai
 :<- [:product-task/all]
 (fn [all-order [_]]
   (let [org-id "35"]
     (filter #(-> % :order_company_id (= org-id)) all-order))))


;;=========================
;;  销售订单及销售订单分析
;;==================


(reg-sub
 :sell-order/all
 :<- [:full-order/all]
 (fn [all-detail [_]]
   (-> (map #(select-keys %  [:order_id
                              :contract_number
                              :order_number
                              :order_seller
                              :order_is_delete
                              :order_detail_id
                              :order_company_id
                              :order_saler_id
                              :goods_id
                              :goods_name
                              :color_number
                              :customer_color
                              :tax_price
                              :tax_money
                              :customer_name
                              :customer_id
                              :order_detail_weight
                              ;;:company_id
                              :order_time
                              :finish_time])  all-detail)
       set)))

(reg-sub
 :sell-order/by-order_detail_id
 :<- [:sell-order/all]
 (fn [all-data [_ id]]
   (-> (filter #(= (:order_detail_id %)  id)  all-data))))

(reg-sub
 :sell-order/all.count_orders
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (count all-order)))

(reg-sub
 :sell-order/all.sum_tax_money
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :tax_money))
            ;;(map utils/round-number)
            (apply +)
            utils/round-number))))

(reg-sub
 :sell-order/all.order_detail_weight
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :order_detail_weight))
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)))))
(reg-sub
 :sell-order/all.job_order_weight
 :<- [:product-order/all]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :job_order_weight))
            (filter #(-> % nil? not))
            (map js/parseInt)
            (apply +)
            ;;(map utils/round-number)
            ))))

(reg-sub
 :sell-order/all.flow_plan_release
 :<- [:product-task/all]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_plan_release))
            (filter #(-> % nil? not))
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)))))

(reg-sub
 :sell-order/all.flow_final_weight
 :<- [:product-task/all]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_final_weight))
            ;;(map utils/round-number)
            (filter #(-> % nil? not))
            (filter #(not= % ""))
            (map js/parseInt)
            (apply +)))))


;; 泰安公司相关


(reg-sub
 :sell-order/tai_an
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (let [org-id "3"]
     (filter #(-> % :order_company_id (= org-id)) all-order))))

(reg-sub
 :sell-order/tai_an.count_orders
 :<- [:sell-order/tai_an]
 (fn [all-order [_]]
   (count all-order)))

(reg-sub
 :sell-order/tai_an.sum_tax_money
 :<- [:sell-order/tai_an]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :tax_money))
            (apply +)
            utils/round-number))))

(reg-sub
 :sell-order/tai_an.order_detail_weight
 :<- [:sell-order/tai_an]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :order_detail_weight))
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)))))
(reg-sub
 :sell-order/tai_an.job_order_weight
 :<- [:product-order/tai_an]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :job_order_weight))
            (filter #(-> % nil? not))
            (map js/parseInt)
            (apply +)))))

(reg-sub
 :sell-order/tai_an.flow_plan_release
 :<- [:product-task/tai_an]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_plan_release))
            (filter #(-> % nil? not))
            (map js/parseInt)
            (apply +)))))

(reg-sub
 :sell-order/tai_an.flow_final_weight
 :<- [:product-task/tai_an]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_final_weight))
            ;;(map utils/round-number)
            (filter #(-> % nil? not))
            (filter #(not= % ""))
            (map js/parseInt)
            (apply +)))))

;; 新泰公司相关
(reg-sub
 :sell-order/xin_tai
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (let [org-id "35"]
     (filter #(-> % :order_company_id (= org-id)) all-order))))

(reg-sub
 :sell-order/xin_tai.count_orders
 :<- [:sell-order/xin_tai]
 (fn [all-order [_]]
   (count all-order)))

(reg-sub
 :sell-order/xin_tai.sum_tax_money
 :<- [:sell-order/xin_tai]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :tax_money))
            (apply +)
            utils/round-number))))

(reg-sub
 :sell-order/xin_tai.order_detail_weight
 :<- [:sell-order/xin_tai]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :order_detail_weight))
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)))))
(reg-sub
 :sell-order/xin_tai.job_order_weight
 :<- [:product-order/xin_tai]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :job_order_weight))
            (filter #(-> % nil? not))
            (map js/parseInt)
            (apply +)

            ;;(map utils/round-number)
            ))))

(reg-sub
 :sell-order/xin_tai.flow_plan_release
 :<- [:product-task/xin_tai]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_plan_release))
            (filter #(-> % nil? not))
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)))))

(reg-sub
 :sell-order/xin_tai.flow_final_weight
 :<- [:product-task/xin_tai]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_final_weight))
            ;;(map utils/round-number)
            (filter #(-> % nil? not))
            (filter #(not= % ""))
            (map js/parseInt)
            (apply +)))))



;;======================================================
;; 测 试
;;===============================================

;; (reg-sub
;;  :resource/all
;;  (fn [db [_ model-key]]
;;    (let [model (get model-register model-key)
;;          data-path (get model :data-path)]
;;      (get-in db data-path))))

;; (reg-sub
;;  :resource/columns
;;  :<- [:metadata/all]
;;  (fn [all-meta  [_  model-key]]
;;    (let [model (get model-register model-key)
;;          table_name (get model :table_name)]
;;      (->> (get-in all-meta [table_name])))))

;; (reg-sub
;;  :resource/view.table-columns
;;  :<- [:metadata/all]
;;  (fn [all-meta  [_  model-key]]
;;    (let [model (get model-register model-key)
;;          table_name (get model :table_name)]
;;      (-> (get-in all-meta [table_name])
;;          vals
;;          (->>
;;           (map (fn [x] {:key (:column_name x)
;;                         :dataIndex (:column_name x)
;;                         :title (or (:view_title x)
;;                                    (:column_comment x)
;;                                    (:column_name x))})))
;;          ;;
;;          ))))

;; (reg-sub
;;  :resource/view-state
;;  (fn [db [_ model-key]]
;;    (let [model (get model-register model-key)
;;          data-path (get model :data-path)
;;          view-path (get model :view-path)]
;;      (get-in db view-path)
;;      ;;
;;      )))

;; (reg-sub
;;  :resource/filter
;;  (fn [db [_ model-key filter-fns]]
;;    (let [model (get model-register model-key)
;;          data-path (get model :data-path)]
;;      (->>  (get-in db data-path)
;;            (filter (comp  filter-fns))))))

;; (reg-sub
;;  :resource/find-by
;;  (fn [db [_ model-key query]]
;;    (let [model (get model-register model-key)
;;          data-path (get model :data-path)]
;;      (->>  (get-in db data-path)
;;            (filter  #(query-filter/is-part-of-query? % query))))))

;; (reg-sub
;;  :resource/find_by-order_detail_id
;;  (fn [db [_ model-key id]]
;;    (let [model (get model-register model-key)
;;          data-path (get model :data-path)]
;;      (->>  (get-in db data-path)
;;            (filter  #(= (:order_detail_id  %)  id))))))

;; (reg-sub
;;  :resource/unique
;;  (fn [db [_ model-key field-key]]
;;    (let [model (get model-register model-key)
;;          data-path (get model :data-path)]
;;      (->>  (get-in db data-path)
;;            (map (fn [x] (get x field-key)))
;;            set
;;            ;;
;;            ))))



