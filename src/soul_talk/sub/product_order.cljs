(ns soul-talk.sub.product-order
  (:require 
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.util.query-filter :as query-filter]))

;; 生产订单
;; 全部公司相关的计算
(reg-sub
 :product-order/all
 :<- [:full-order/all]
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
(reg-sub
 :product-order/by-order_detail_id
 :<- [:product-order/all]
 (fn [all-data [_ id]]
   (-> (filter #(= (:order_detail_id %)  id)  all-data))))

;; (reg-sub
;;  :product-order/view.index-page
;;  :<- [:product-order/all]
;;  :<- [:active-page]
;;  :<- [:current-page-state]
;;  (fn [[all-data active-page current-page-state] [_]]
;;    (let [order_detail_id (get current-page-state :order_detail_id)]
;;      (filter  #(query-filter/has-kv? % :order_detail_id order_detail_id)  all-data)
;;      ;;
;;      )))

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



