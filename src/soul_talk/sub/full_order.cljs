(ns soul-talk.sub.full-order
  (:require 
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.util.query-filter :as query-filter]))

(reg-sub
 :full-order/all
 :<- [:resource/all :order-track]
 (fn [all-order [_]]
   all-order))

;; (reg-sub
;;  :full-order/view.index-page
;;  :<- [:full-order/all]
;;  :<- [:active-page]
;;  :<- [:current-page-state]
;;  (fn [[all-data active-page current-page-state] [_]]
;;    (let [order_detail_id (get current-page-state :order_detail_id)]
;;      (filter  #(query-filter/has-kv? % :order_detail_id order_detail_id ) all-data  )
;;      ;;
;;      )))


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

;; 过滤

;; (reg-sub
;;  :full-order/state.data
;;  :<- [:full-order/all]
;;  :<- [:cost.index/state]
;;  (fn [[all-data state] [_]]
;;    (let []
;;      (utils/filter->full-order all-data state))))

;; 全部公司相关的计算

;; (reg-sub
;;  :product.output/all
;;  (fn [db [_]]
;;    (get-in  db [:product.output :datasets])))



















