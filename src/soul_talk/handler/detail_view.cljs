(ns soul-talk.handler.detail-view
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [soul-talk.utils :as utils]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]

             ))


;; 定义全局state


(def state-sample {:order_id -1
                   :order_detail_id -1
                   :flow_id -1})


(reg-event-db
 :cost.detail/mdw-material_craft
 (fn [db [_  model-key response ]]
   (let [dataset (get-in response [:result])]
     (-> db
         (assoc-in [:views :cost.detail :data  model-key]   dataset)))))

(reg-event-fx
 :cost.detail/material_craft-pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/material-craft"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost.detail/mdw-material_craft  :material_craft ]}}))

(reg-event-fx
 :cost.detail/human_resource-pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/human"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost.detail/mdw-material_craft  :human_resource ]}}))

(reg-event-fx
 :cost.detail/machine-pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/machine"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost.detail/mdw-material_craft  :machine ]}}))







(reg-sub
 :cost.detail/material_craft
 (fn [db [_]]
   (get-in  db [:views :cost.detail :data :material_craft])))

(reg-sub
 :cost.detail/human_resource
 (fn [db [_]]
   (get-in  db [:views :cost.detail :data :human_resource])))

(reg-sub
 :cost.detail/machine
 (fn [db [_]]
   (get-in  db [:views :cost.detail :data :machine])))





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










