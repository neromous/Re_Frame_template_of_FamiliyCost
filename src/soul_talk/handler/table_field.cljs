 (ns soul-talk.handler.table-fields
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

(defn build-table-field-map [origin target]
  (assoc-in origin [(get target :table_name)  (get target :column_name)] target))

(reg-event-db
 :mdw/table-pull
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  [:documents :table] dataset)
         (assoc-in  [:documents-state  :table]   state)))))

(reg-event-fx
 :server/query-table
 (fn [_ [_]]
   {:http {:method  GET
           :url   "http://0.0.0.0:3000/table-fields"
           :ajax-map       {:keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:mdw/table-pull]}}))

(reg-event-db
 :table/set-select-table
 (fn [db [_ table-name]]
   (assoc-in db [:table-relation :selected-table] table-name)))

(reg-sub
 :table/get-select-table
 (fn [db [_]]
   (get-in db [:table-relation :selected-table])))

(reg-sub
 :table/all-data
 (fn [db _]
   (get-in db [:documents :table])))

(reg-sub
 :table/tables
 (fn [db [_ table-name]]
   (get-in db [:documents :table table-name])))

(reg-sub
 :table/table-fields
 :<- [:table/all-data]
 (fn [all-data [_ table-name field-name]]
   (get-in all-data [table-name :columns field-name])))

(reg-sub
 :table/fd-type
 :<- [:table/all-data]
 (fn [all-data [_ table-name field-name]]
   (get-in all-data [:documents :table table-name  :columns field-name  :data_type])))

(reg-event-db
 :table/add-field-attrb
 (fn [db [_ table-name field-name attrib-name attrib-value]]
   (assoc-in db [:documents :table table-name  field-name attrib-name] attrib-value)))

(reg-event-db
 :table/set-table-attrb
 (fn [db [_ table-name attrib-name attrib-value]]
   (assoc-in db [:documents :table table-name attrib-name] attrib-value)))

(reg-sub
 :table/get-table-attrb
 (fn [db [_ table-name attrib-name attrib-value]]
   (get-in db [:documents :table table-name attrib-name])))

(reg-sub
 :table/get-field-attrb
 :<- [:table/all-data]
 (fn [all-data [_ table-name field-name attrib-name]]
   (get-in all-data [table-name field-name attrib-name])))

(reg-sub
 :table/sample-one
 :<- [:table/all-data]
 (fn [all-data _]
   (first all-data)))

(reg-sub
 :table/count
 :<- [:table/all-data]
 (fn [table  [event-head & args]]
   (count table)))

(reg-sub
 :table/table-names
 :<- [:table/all-data]
 (fn [table [_  name-filter]]
   (->> table
        keys
        set
        sort
        (map name))))

(reg-sub
 :table/field-names
 :<- [:table/all-data]
 (fn [all-data [_ table_name]]
   (->> (filter #(-> % key :table_name (= (name table_name))) all-data)
        vals
        (map :column_name)
        set
        sort)))

(reg-sub
 :table/filter-by-table_name
 :<- [:table/all-data]
 (fn [all-data [_ table_name]]
   (->
    (get-in all-data [(keyword @table_name)  :columns])
    vals)))

