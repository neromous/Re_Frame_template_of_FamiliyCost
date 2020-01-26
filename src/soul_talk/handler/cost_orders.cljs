(ns soul-talk.handler.cost-orders
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [soul-talk.utils :as utils]))

;; 取位置数据

(reg-sub
 :order-track/data.all
 (fn [db [_]]
   (get-in db  [:order-track :datasets])))





;; 未参与重染订单
(reg-sub
 :order-track/data.no_re_dye
 :<- [:order-track/data.all]
 (fn [all-data [_]]
   (filter  #(-> % :is_re_dye (= 1)) all-data)))

;; 参与重染的订单
(reg-sub
 :order-track/data.re_dye
 :<- [:order-track/data.all]
 (fn [all-data [_]]
   (filter  #(-> % :is_re_dye (= 2)) all-data)))

;; 取一个样本
(reg-sub
 :order-track/data.sample
 :<- [:order-track/data.all]
 (fn [all-data [_]]
   (first all-data)))

;; 取样本的columns用于渲染
(reg-sub
 :order-track/view.table-columns
 :<- [:order-track/data.sample]
 (fn [samples [_]]
   (let [sample-keys  (keys samples)]
     (map (fn [x]  {:title (name x)
                    :dataIndex (name x)
                    :key (name x)})    sample-keys))))

;; 取一个字段的所有特殊值
(reg-sub
 :order-track/columns.unique-key
 :<- [:order-track/data.all]
 (fn [all-data [_ columns-key]]
   (->> all-data
        (map #(get % columns-key))
        set
        sort)))

;; 聚合函数
(reg-sub
 :order-track/group-by
 :<- [:order-track/data.all]
 (fn [all-data [_ columns-key]]
   (group-by #(get % columns-key) all-data)))

;; 采用query筛选
(reg-sub
 :order-track/filter
 :<- [:order-track/data.all]
 (fn [all-data [_ query]]
   (let [query-keys  (keys query)]
     (filter #(= (select-keys % query-keys)   query) all-data))))

;; 过滤

(reg-sub
 :order-track/state.data
 :<- [:order-track/data.all]
 :<- [:cost.index/state]
 (fn [[all-data state] [_]]
   (let []
     (utils/filter->order-track all-data state))))

;; 全部公司相关的计算

(reg-sub
 :product.output/all
 (fn [db [_]]
   (get-in  db [:product.output :datasets])))



















