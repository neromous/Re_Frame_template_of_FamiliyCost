(ns soul-talk.handler.product-task
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [soul-talk.utils :as utils]))

;;全公司生产任务
(reg-sub
 :product-task/all
 :<- [:order-track/data.all]
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
                              :workshop_id
                              ])  all-detail)
       set)))


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

