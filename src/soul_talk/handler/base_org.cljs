(ns soul-talk.handler.base-org
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
  (concat [:data :base.org :dataset] args))

(defn  view-path
  [& args]
  (concat [:data :base.org :views] args))

(defn dataset-state-path
  [& args]
  (concat [:data :base.org :dataset-state] args))

(defn relation-path [& args]
  (concat [:data :base.org :relation]))

(reg-event-db
 :base.org/mdw.dto
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  (dataset-path) dataset)
         (assoc-in  (dataset-state-path)   state)))))

(reg-event-fx
 :base.org/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/api/v2/query/sys_org"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:base.org/mdw.dto]}}))


(reg-sub
 :base.org/page-state
 (fn [db [_]]
   (get-in db (view-path  :page-state))))

(reg-event-db
 :base.org/page-state.replace
 (fn [db [_ form]]
   (assoc-in db (view-path  :page-state) form)))

(reg-event-db
 :base.org/page-state
 (fn [db [_ form]]
   (update-in db (view-path  :page-state) merge form)))

(reg-sub
 :base.org/data.all
 (fn [db [_]]
   (get-in db (dataset-path))))

(reg-sub
 :base.org/data.sample
 :<- [:base.org/data.all]
 (fn [all-data [_]]
   (first all-data)))

(reg-sub
 :base.org/factory
 :<- [:base.org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "factory")) all-data)))

(reg-sub
 :base.org/company
 :<- [:base.org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "company")) all-data)))

(reg-sub
 :base.org/department
 :<- [:base.org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "department")) all-data)))

(reg-sub
 :base.org/repository
 :<- [:base.org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "repository")) all-data)))

(reg-sub
 :base.org/workshop
 :<- [:base.org/data.all]
 (fn [all-data [_]]
   (filter #(-> % :code (= "workShop")) all-data)))

(reg-sub
 :base.org/find-by-id
 :<- [:base.org/data.all]
 (fn [all-data [_ id]]
   (-> (filter #(-> % :base.org_id (= id))  all-data)
       first)))

(reg-sub
 :base.org/children-by-id
 :<- [:base.org/data.all]
 (fn [all-data [_ id]]
   (let [trees  (group-by :parent_id  all-data)]
     (get trees id))))




















