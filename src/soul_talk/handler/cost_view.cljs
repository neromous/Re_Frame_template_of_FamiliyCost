(ns soul-talk.handler.cost-view
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

(reg-sub
 :cost.index/state
 (fn [db [_]]
   (get-in db [:view.state :cost.index]
           {:factorys []
            :companys []
            :workshops []
            :order-ids []
            :detail-ids []
            :time-begin "2019-01-01"
            :time-end nil
            :color-numbers []
            :customers []
            ;; 以下为染缸所在的
            :flow_dyelot_numbers []
            :flow_ids []
            })))

(reg-event-db
 :cost.index/state.replace
 (fn [db [_ form]]
   (assoc-in db [:view.state :cost.index] form)))

(reg-event-db
 :cost.index/state
 (fn [db [_ form]]
   (update-in db [:view.state :cost.index] merge form)))

(reg-sub
 :cost.index/factorys
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :factorys)))

(reg-sub
 :cost.index/companys
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :companys)))

(reg-sub
 :cost.index/workshops
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :workshops)))

(reg-sub
 :cost.index/time-begin
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :time-begin)))

(reg-sub
 :cost.index/time-end
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :time-end)))

(reg-sub
 :cost.index/select-customer
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :select-customer)))

(reg-sub
 :cost.index/order-ids
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :order-ids)))

(reg-sub
 :cost.index/detail-ids
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :detail-ids)))

(reg-sub
 :cost.index/color-numbers
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :color-numbers)))

(reg-sub
 :cost.index/customers
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (get page-state :customers)))

















