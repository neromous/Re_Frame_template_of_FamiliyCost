(ns soul-talk.handler.cost
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
             [soul-talk.utils :as utils]

             ))

(reg-event-db
 :cost/mdw.dto
 (fn [db [_ d-name  response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in [d-name :datasets]   dataset)
         (assoc-in  [d-name :state]   state)))))

(reg-event-fx
 :cost.human/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/human"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :cost.human]}}))

(reg-event-fx
 :energy.oa_report/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/energy/oa_report"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :energy.oa_report]}}))

(reg-event-fx
 :cost.material-craft/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/material-craft"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :cost.material-craft]}}))

(reg-event-fx
 :cost.material-craft/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/material-craft"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :cost.material-craft]}}))

(reg-event-fx
 :cost.material-raw/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/material-raw"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :cost.material-raw]}}))

(reg-event-fx
 :product.output/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/product-output"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :product.output]}}))

(reg-event-fx
 :sell-order/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/sell-order"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :sell-order]}}))

(reg-event-fx
 :product-order/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/product-order"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :product-order]}}))

(reg-event-fx
 :product-task/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/product-task"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :product-task]}}))

(reg-event-fx
 :order-distribution/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/product-distribution"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :order-distribution]}}))

(reg-sub
 :product-order/all
 (fn [db [_]]
   (get-in  db [:product-order :datasets])))

;;  全部公司销售订单
(reg-sub
 :sell-order/all
 (fn [db [_]]
   (get-in  db [:sell-order :datasets])))

(reg-sub
 :order-distribution/all
 (fn [db [_]]
   (get-in  db [:order-distribution :datasets])))

(reg-sub
 :product-task/all
 (fn [db [_]]
   (get-in  db [:product-task :datasets])))

(reg-sub
 :cost.human/all
 (fn [db [_]]
   (get-in  db [:cost.human :datasets])))

(reg-sub
 :energy.oa_report/all
 (fn [db [_]]
   (get-in db  [:energy.oa_report :datasets])))

(reg-sub
 :cost.material-craft/all
 (fn [db [_]]
   (get-in  db [:cost.material-craft :datasets])))

(reg-sub
 :cost.material-raw/all
 (fn [db [_]]
   (get-in  db [:cost.material-raw :datasets])))

(reg-sub
 :product.output/all
 (fn [db [_]]
   (get-in  db [:product.output :datasets])))

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
            (apply +)
            utils/round-number
            ))))

;;  用于进行集合运算的类别


(reg-sub
 :bull/sell-order
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (-> (map #(select-keys % [:order_id :order_detail_id])  all-order)
       set)))

(reg-sub
 :bull/product-order
 :<- [:product-order/all]
 (fn [all-order [_]]
   (-> (map #(select-keys % [:order_id :order_detail_id])  all-order)
       set)))

(reg-sub
 :bull/order-distribution
 :<- [:order-distribution/all]
 (fn [all-order [_]]
   (-> (map #(select-keys % [:order_id :order_detail_id])  all-order)
       set)))

(reg-sub
 :bull/product-order
 :<- [:product-order/all]
 (fn [all-order [_]]
   (-> (map #(select-keys % [:order_id :order_detail_id])  all-order)
       set)))

(reg-sub
 :bull/product-task
 :<- [:product-task/all]
 (fn [all-order [_]]
   (-> (map #(select-keys % [:order_id :order_detail_id])  all-order)
       set)))

;;  泰安公司相关


(reg-sub
 :sell-order/tai_an
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (let [org-id "3"]
     (filter #(-> % :company_id (= org-id)) all-order))))

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
            utils/round-number
            ))))
;;  新泰公司相关

(reg-sub
 :sell-order/xin_tai
 :<- [:sell-order/all]
 (fn [all-order [_]]
   (let [org-id "35"]
     (filter #(-> % :company_id (= org-id)) all-order))))

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
            utils/round-number
            ))))

(reg-sub
 :cost/order-track
 :<- [:sell-order/all]
 :<- [:product-order/all]
 :<- [:product-task/all]
 :<- [:order-distribution/all]
 ;;:<- [:cost.human/all]
 ;;:<- [:cost.material-craft/all]
 :<- [:cost.material-raw/all]
 :<- [:product.output/all]
 (fn [[sell-order
       product-order
       product-task
       order-distribution
       ;;cost-human
       ;;cost-material-craft
       cost-material-raw
       product-output] [_ order-id]]
   (let [search  :order_id]
     {:sell-order (filter  #(-> % search (= order-id))   sell-order)
      :product-order (filter  #(-> % search (= order-id))   product-order)
      :product-task (filter  #(-> % search (= order-id))   product-task)
      :order-distribution (filter  #(-> % search (= order-id))  order-distribution)
    ;;:cost.human (filter #(-> % search (= order-id)) cost-human)
      :cost.material-raw     (filter #(-> % search (= order-id)) cost-material-raw)
      :product.output     (filter #(-> % search (= order-id)) product-output)
    ;;:cost.material-craft     (filter #(-> % search (= order-id)) cost-materal-craft)
      })))



























