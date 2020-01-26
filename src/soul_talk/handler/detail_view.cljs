(ns soul-talk.handler.detail-view
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [soul-talk.utils :as utils]))


;; 定义全局state


(def state-sample {:order_id -1
                   :order_detail_id -1
                   :flow_id -1})

(reg-sub
 :cost.detail/material_craft
 (fn [db [_]]
   (get-in  db [:views :cost.detail :data :material_craft])))

(reg-sub
 :views/cost.detail
 (fn [db [_]]
   (get-in db [:views :cost.detail])))

(reg-event-db
 :views/cost.detail
 (fn [db [_  form]]
   (assoc-in db [:views :cost.detail] form)))

(reg-sub
 :cost.detail/order_detail-by_query
 :<- [:sell-order/all]
 (fn [all-order [_ views]]
   (let [order_id  (get  views :order_detail_id -1)]
     (filter #(-> % :order_detail_id  (= order_id))  all-order)
     ;;
     )))

(reg-sub
 :cost.detail/product_task-by_query
 :<- [:product-task/all]
 (fn [all-order  [_  views]]
   (let [order_id  (get  views :order_detail_id -1)]
     (filter #(-> % :order_detail_id  (= order_id))  all-order)
     ;;
     )))

(reg-sub
 :cost.detail/product_order-by_query
 :<- [:product-order/all]
 (fn [all-order  [_  views]]
   (let [order_id  (get  views :order_detail_id -1)]
     (filter #(-> % :order_detail_id  (= order_id))  all-order)
     ;;
     )))

(reg-sub
 :cost.detail/order_detail-selected
 :<- [:sell-order/all]
 :<- [:views/cost.detail]
 (fn [[all-order views] [_]]
   (let [order_id  (get  views :order_detail_id -1)]
     (filter #(-> % :order_detail_id  (= order_id))  all-order)
     ;;
     )))

(reg-sub
 :cost.detail/product_task-selected
 :<- [:product-task/all]
 :<- [:views/cost.detail]
 (fn [[all-order views] [_]]
   (let [order_id  (get  views :order_detail_id -1)]
     (filter #(-> % :order_detail_id  (= order_id))  all-order)
     ;;
     )))

(reg-sub
 :cost.detail/material_raw
 :<- [:cost.material-raw/all]
 :<- [:views/cost.detail]
 (fn [[all views] [_]]
   (let [order_id  (get  views :order_detail_id -1)]
     (->   (filter #(-> % :order_detail_id  (= order_id))  all)
           (->> (group-by :dyelot_number)))
     ;;
     )))










