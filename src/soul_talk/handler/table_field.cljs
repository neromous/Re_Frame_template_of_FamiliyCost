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

(def dataset-path [:data :table :dataset])
(def view-path [:data :table :views])
(def dataset-state-path [:data :table :dataset-state])

(reg-event-db
 :table/mdw.dto
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  dataset-path dataset)
         (assoc-in  dataset-state-path   state)))))

(reg-event-fx
 :table/server.pull
 (fn [_ [_]]
   {:http {:method  GET
           :url   "http://0.0.0.0:3000/table-fields"
           :ajax-map       {:keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:table/mdw.dto]}}))

(reg-event-fx
 :table/server.update
 (fn [_ [_]]
   {:http {:method  GET
           :url   "http://0.0.0.0:3000/table-fields"
           :ajax-map       {:keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:table/mdw.dto]}}))

(reg-sub
 :table/view.all
 (fn [db _]
   (get-in db view-path)))

(reg-event-db
 :table/view.selected-table
 (fn [db [_ table-name]]
   (assoc-in db (concat view-path [:selected-table]) table-name)))

(reg-sub
 :table/view.selected-table
 :<- [:table/view.all]
 (fn [views [_]]
   (get-in views [:selected-table])))

(reg-event-db
 :table/view.selected-field
 (fn [db [_ field-name]]
   (assoc-in db (concat view-path [:selected-field]) field-name)))

(reg-sub
 :table/view.selected-field
 :<- [:table/view.all]
 (fn [views [_]]
   (get-in views [:selected-field])))

(reg-sub
 :table/dataset.all
 (fn [db _]
   (get-in db dataset-path)))

(reg-sub
 :table/table.table
 :<- [:table/dataset.all]
 (fn [all-data [_ table-name]]
   (get-in all-data [table-name])))

(reg-sub
 :table/view.table-fields
 :<- [:table/dataset.all]
 (fn [all-data [_ table-name]]
   [;; vec1
    (keys all-data)
    ;; vec2
    (-> all-data
        (get-in [table-name  :columns])
        vals)
    ]))

(reg-sub
 :table/field.table-field
 :<- [:table/dataset.all]
 (fn [all-data [_ table-name field-name]]
   (get-in all-data [table-name :columns field-name])))

(reg-sub
 :table/data.sample-one
 :<- [:table/dataset.all]
 (fn [all-data _]
   (first all-data)))

(reg-sub
 :table/count
 :<- [:table/dataset.all]
 (fn [table  [event-head & args]]
   (count table)))

(reg-sub
 :table/table-names
 :<- [:table/dataset.all]
 (fn [table [_]]
   (->> table
        keys
        set
        sort
        (map name))))

(reg-sub
 :table/field-names
 :<- [:table/dataset.all]
 (fn [all-data [_ table_name]]
   (->> (filter #(-> % key :table_name (= (name table_name))) all-data)
        vals
        (map :column_name)
        set
        sort)))

(reg-sub
 :table/table.table-fields
 :<- [:table/dataset.all]
 (fn [all-data  [_  table_name]]
   (->
    (get-in all-data [table_name  :columns])
    vals)))

(reg-sub
 :table/table.table-heads
 :<- [:table/dataset.all]
 (fn [all-data   [_  table_name]]
   (->
    (get-in all-data [table_name])
    (dissoc :columns)
    (->> (map (fn [x] {:attrib (key x)
                       :value (val x)}))))))

(reg-event-db
 :table/table.table-heads
 (fn [db [_ table-name attrib-key attrib-value]]
   (assoc-in db (concat dataset-path [table-name attrib-key])  attrib-value)))

(reg-event-db
 :table/attrib_name.cache
 (fn [db [_ attrib-names]]
   (update-in db (concat view-path [:attrib_names]) concat attrib-names)))

(reg-sub
 :table/attrib_name.cache
 (fn [db [_]]
   (get-in db (concat view-path [:attrib_names]))))

