(ns soul-talk.handler.product-order
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [soul-talk.utils :as utils]))

;; 生产订单
;; 全部公司相关的计算
(reg-sub
 :product-order/all
 :<- [:order-track/data.all]
 (fn [all-detail [_]]
   (-> (map #(select-keys %  [:order_id
                              :order_detail_id
                              :job_order_id
                              :order_number
                              :goods_id
                              :goods_name
                              :order_company_id
                              :order_saler_id
                              :color_number
                              :customer_color
                              :tax_price
                              :tax_money
                              :customer_name
                              :customer_id
                              :order_detail_weight
                              :job_order_weight])  all-detail)
       set)))





;; 泰安公司相关
(reg-sub
 :product-order/tai_an
 :<- [:product-order/all]
 (fn [all-order [_]]
   (let [org-id "3"]
     (filter #(-> % :order_company_id (= org-id)) all-order))))




;; 新泰公司相关

(reg-sub
 :product-order/xin_tai
 :<- [:product-order/all]
 (fn [all-order [_]]
   (let [org-id "35"]
     (filter #(-> % :order_company_id (= org-id)) all-order))))



