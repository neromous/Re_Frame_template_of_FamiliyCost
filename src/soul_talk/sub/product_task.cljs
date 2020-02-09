(ns soul-talk.sub.product-task
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [soul-talk.utils :as utils]
             [soul-talk.util.query-filter :as query-filter]))

;;全公司生产任务


(reg-sub
 :product-task/all
 :<- [:full-order/all]
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

(reg-sub
 :product-task/by-order_detail_id
 :<- [:product-task/all]
 (fn [all-data [_ id]]
   (-> (filter #(= (:order_detail_id %)  id)  all-data))))



;;


;; (reg-sub
;;  :product-task/view.index-page
;;  :<- [:product-task/all]
;;  :<- [:active-page]
;;  :<- [:current-page-state]
;;  (fn [[all-data active-page current-page-state] [_]]
;;    (let [order_detail_id (get current-page-state :order_detail_id)]
;;      (filter  #(query-filter/has-kv? % :order_detail_id order_detail_id) all-data)
;;      ;;
;;      )))

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

