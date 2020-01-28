(ns soul-talk.sub.sell-order
  (:require [soul-talk.models :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.utils :as utils]
            [soul-talk.util.query-filter :as query-filter]))

;;  全部公司销售订单
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
                              :finish_time
                              ])  all-detail)
       set)))



(reg-sub
 :sell-order/view.index-page
 :<- [:sell-order/all]
 :<- [:active-page]
 :<- [:current-page-state]
 (fn [[all-data active-page current-page-state] [_]]
   (let [order_detail_id (get current-page-state :order_detail_id)]
     (filter  #(query-filter/has-kv? % :order_detail_id order_detail_id )  all-data )
     ;;
     )))


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
            utils/round-number
            ))))

(reg-sub
 :sell-order/all.order_detail_weight
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :order_detail_weight))
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)
            ))))
(reg-sub
 :sell-order/all.job_order_weight
 :<- [:product-order/all]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :job_order_weight))
            (filter #(-> % nil? not)  )
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
            (filter #(-> % nil? not)  )
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)

            ))))

(reg-sub
 :sell-order/all.flow_final_weight
 :<- [:product-task/all]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_final_weight))
            ;;(map utils/round-number)
            (filter #(-> % nil? not)  )
            (filter #(not= % "")  )
            (map js/parseInt)
            (apply +)
            ))))


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
            (apply +)
            ))))
(reg-sub
 :sell-order/tai_an.job_order_weight
 :<- [:product-order/tai_an]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :job_order_weight))
            (filter #(-> % nil? not)  )
            (map js/parseInt)
            (apply +)

            ;;(map utils/round-number)
            ))))

(reg-sub
 :sell-order/tai_an.flow_plan_release
 :<- [:product-task/tai_an]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_plan_release))
            (filter #(-> % nil? not)  )
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)

            ))))


(reg-sub
 :sell-order/tai_an.flow_final_weight
 :<- [:product-task/tai_an]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_final_weight))
            ;;(map utils/round-number)
            (filter #(-> % nil? not)  )
            (filter #(not= % "")  )
            (map js/parseInt)
            (apply +)
            ))))

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
            (apply +)
            ))))
(reg-sub
 :sell-order/xin_tai.job_order_weight
 :<- [:product-order/xin_tai]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :job_order_weight))
            (filter #(-> % nil? not)  )
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
            (filter #(-> % nil? not)  )
            ;;(map utils/round-number)
            (map js/parseInt)
            (apply +)

            ))))


(reg-sub
 :sell-order/xin_tai.flow_final_weight
 :<- [:product-task/xin_tai]
 (fn [all-order [_]]
   (-> all-order
       (->> (map #(get %  :flow_final_weight))
            ;;(map utils/round-number)
            (filter #(-> % nil? not)  )
            (filter #(not= % "")  )
            (map js/parseInt)
            (apply +)
            ))))












