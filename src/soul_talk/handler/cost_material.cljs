(ns soul-talk.handler.cost-material
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]
             [soul-talk.utils :as utils]))

(reg-sub
 :product-task/material.output.sum
 :<- [:order-track/data.no_re_dye]
 (fn [no-re-dye  [_]]
   (->>  (map :inward_quantity no-re-dye)
         (map int)
         (apply +))))

(reg-sub
 :product-task/material.re_dye.sum
 :<- [:order-track/data.re_dye]
 (fn [re-dye  [_]]
   (->>  (map :inward_quantity re-dye)
         (map int)
         (apply +))))

(reg-sub
 :cost.material-craft/all
 (fn [db [_]]
   (get-in  db [:cost.material-craft :datasets])))

(reg-sub
 :cost.material-raw/all
 (fn [db [_]]
   (get-in  db [:cost.material-raw :datasets])))


(reg-sub
 :cost.material-raw/data.product_flow
 :<- [:cost.material-raw/all]
 :<- [:cost.index/state]
 (fn [[all-material state] [_]]
   (let []
     (utils/filter->maerial-raw all-material state )
     )))



;; 全公司相关

;; 前络投料重量, 含重染
(reg-sub
 :cost.material-raw/all.weight.sum
 :<- [:cost.material-raw/all]
 (fn [all-data [_]]
   (->>  (map :ql_weight all-data)
         (map utils/round-number)
         (apply +))))

;; 泰安前络投料领料单 含重染
(reg-sub
 :cost.material-raw/tai_an
 :<- [:sell-order/tai_an]
 :<- [:cost.material-raw/all]
 (fn [[orders materials] [_]]
   (let [order-ids  (map :order_detail_id  orders)
         order-ids  (set order-ids)
         filter-fn (fn [x] (->> x :order_detail_id  (contains? order-ids)))]
     (filter filter-fn materials)
     ;;
     )))

;; 前络投料重量, 含重染
(reg-sub
 :cost.material-raw/tai_an.weight.sum
 :<- [:cost.material-raw/tai_an]
 (fn [tai_an [_]]
   (->>  (map :ql_weight tai_an)
         (map int)
         (apply +))))









;; 新泰公司相关

(reg-sub
 :cost.material-raw/xin_tai
 :<- [:sell-order/xin_tai]
 :<- [:cost.material-raw/all]
 (fn [[orders materials] [_]]
   (let [order-ids  (map :order_detail_id  orders)
         order-ids  (set order-ids)
         filter-fn (fn [x] (->> x :order_detail_id  (contains? order-ids)))]
     (filter filter-fn materials)
     ;;
     )))

(reg-sub
 ;; 前络投料重量, 含重染
 :cost.material-raw/xin_tai.weight.sum
 :<- [:cost.material-raw/xin_tai]
 (fn [xin_tai [_]]
   (->>  (map :ql_weight xin_tai)
         (map int)
         (apply +))))















