(ns soul-talk.handler.org
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
  (concat [:data :org :dataset] args))

(defn  view-path
  [& args]
  (concat [:data :org :views] args))

(defn dataset-state-path
  [& args]
  (concat [:data :org :dataset-state] args))

(defn relation-path [& args]
  (concat [:data :org :relation]))

(reg-event-db
 :org/mdw.dto
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  (dataset-path) dataset)
         (assoc-in  (dataset-state-path)   state)))))

(reg-event-fx
 :org/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/api/v2/query/sys_org"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:org/mdw.dto]}}))

(reg-sub
 :org/data.all
 (fn [db [_]]
   (get-in db (dataset-path))))

(reg-sub
 :org/data.sample
 :<- [:order-track/data.all]
 (fn [all-data [_]]
   (first all-data)))

(reg-sub
 :org/factory
 :<- [:org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "factory")) all-data)))

(reg-sub
 :org/company
 :<- [:org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "company")) all-data)))

(reg-sub
 :org/department
 :<- [:org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "department")) all-data)))

(reg-sub
 :org/repository
 :<- [:org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "repository")) all-data)))

(reg-sub
 :org/workshop
 :<- [:org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "workShop")) all-data)))

(reg-sub
 :org/find-by-id
 :<- [:org/data.all]
 (fn [all-data [_ id]]
   (-> (filter #(-> % :org_id (= id))  all-data)
       first)))

(reg-sub
 :org/children-by-id
 :<- [:org/data.all]
 (fn [all-data [_ id]]
   (let [trees  (group-by :parent_id  all-data)]
     (get trees id))))

















