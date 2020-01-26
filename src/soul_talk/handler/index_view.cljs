(ns soul-talk.handler.index-view
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
             [soul-talk.date-utils :as du]))

(reg-sub
 :cost.index/state
 (fn [db [_]]
   (get-in db [:view.state :cost.index])))

(reg-event-db
 :cost.index/state.init
 (fn [db [_ form]]
   (assoc-in db [:view.state :cost.index] {:factorys []
                                           :companys []
                                           :workshops []
                                           :order_ids []
                                           :order_detail_ids []
                                           :time_begin  (new js/moment  "2019-01-01")
                                           :time_end (new js/moment)
                                           :color_numbers []
                                           :customers []
                                           ;; 以下为染缸所在的
                                           :dyelot_numbers []
                                           :flow_ids []
                                           })))

(reg-event-db
 :time_begin/set
 (fn [db  [_ date-str]]
   (let [date-obj (cond
                    (string? date-str) (du/moment->type date-str)
                    :default date-str)]
     (assoc-in db [:view.state :cost.index :time_begin] date-obj)
     ;;
     )))

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
   (-> (get page-state :factorys)
       set)))

(reg-sub
 :cost.index/companys
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :companys)
       set)))

(reg-sub
 :cost.index/workshops
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :workshops)
       set)))

(reg-sub
 :cost.index/time_begin
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :time_begin)
       ;;
       )))

(reg-sub
 :cost.index/time_end
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :time_end)
       ;;
       )))

(reg-sub
 :cost.index/customers
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :customers)
       set)))

(reg-sub
 :cost.index/order_ids
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :order_ids)
       set)))

(reg-sub
 :cost.index/order_detail_ids
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :order_detail_ids)
       set)))

(reg-sub
 :cost.index/flow_ids
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :flow_ids)
       set)))

(reg-sub
 :cost.index/color_numbers
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :color_numbers)
       set)))

(reg-sub
 :cost.index/dyelot_numbers
 :<- [:cost.index/state]
 (fn [page-state [_]]
   (-> (get page-state :dyelot_numbers)
       set)))













