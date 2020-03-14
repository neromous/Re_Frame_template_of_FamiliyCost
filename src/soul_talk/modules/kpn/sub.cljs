(ns soul-talk.modules.kpn.sub
  (:require  [re-frame.core :refer [inject-cofx
                                    dispatch
                                    dispatch-sync
                                    reg-event-db
                                    reg-event-fx
                                    subscribe reg-sub]]
             [soul-talk.utils :as utils]
             [soul-talk.util.query-filter :as query-filter]
             [soul-talk.sub.funcs.item-path :as item-path]
             [soul-talk.util.data-formatter :as formatter]
             [soul-talk.sub.funcs.product-series :as product-series]))

;;========================================================
;; 全局数据初始化
;;========================================================
(reg-sub
 :full-order/all
 :<- [:sell-info.data]
 (fn [all-order [_]]
   all-order))

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

;;====================
;;====================
;;====================

(reg-sub
 :order_number
 :<- [:order_number.data]
 (fn [all-data [_]]
   all-data))

(reg-sub
 :order_number/get
 :<- [:order_number]
 (fn [all-data [_ coll]]
   (get all-data coll)))

(reg-sub
 :sell-detail.list
 :<- [:sell-detail.data]
 (fn [all-data [_]]
   (->> (apply  concat   all-data)
        (map #(dissoc % :db/id)))))

(reg-sub
 :sell-order.list
 :<- [:sell-order.data]
 (fn [all-data [_]]
   (->> (apply  concat   all-data)
        (map #(dissoc % :db/id)))))

(reg-sub
 :product-flow.list
 :<- [:product-flow.data]
 (fn [all-data [_]]
   (->> (apply  concat   all-data)
        (map #(dissoc % :db/id)))))



