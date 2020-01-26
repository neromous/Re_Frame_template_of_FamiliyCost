(ns soul-talk.handler.cost-customer
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
                                json-response-format]]))


(defn dataset-path
  [& args]
  (concat [:data :base.customer :dataset] args))

(defn  view-path
  [& args]
  (concat [:data :base.customer :views] args))

(defn dataset-state-path
  [& args]
  (concat [:data :base.customer :dataset-state] args))

(defn relation-path [& args]
  (concat [:data :base.customer :relation]))

(reg-event-db
 :base.customer/mdw.dto
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  (dataset-path) dataset)
         (assoc-in  (dataset-state-path)   state)))))

(reg-event-fx
 :base.customer/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/api/v2/query/erp_customer"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:base.customer/mdw.dto]}}))


(reg-sub
 :base.customer/page-state
 (fn [db [_]]
   (get-in db (view-path  :page-state))))

(reg-event-db
 :base.customer/page-state.replace
 (fn [db [_ form]]
   (assoc-in db (view-path  :page-state) form)))

(reg-event-db
 :base.customer/page-state
 (fn [db [_ form]]
   (update-in db (view-path  :page-state) merge form)))

(reg-sub
 :base.customer/data.all
 (fn [db [_]]
   (get-in db (dataset-path))))

(reg-sub
 :base.customer/data.sample
 :<- [:base.customer/data.all]
 (fn [all-data [_]]
   (first all-data)))





















