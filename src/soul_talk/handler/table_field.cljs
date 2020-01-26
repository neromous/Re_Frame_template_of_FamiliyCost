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

(defn dataset-path
  [& args]

  (concat [:data :table :dataset] args))
(defn  view-path
  [& args]
  (concat [:data :table :views] args))
(defn dataset-state-path
  [& args]
  (concat [:data :table :dataset-state] args))

(defn relation-path [& args]
  (concat [:data :table :relation]))

(reg-event-db
 :table/mdw.dto
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  (dataset-path) dataset)
         (assoc-in  (dataset-state-path)   state)))))

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
   (get-in db (view-path))))

(reg-sub
 :table/dataset.all
 (fn [db _]
   (get-in db (dataset-path))))

(reg-sub
 :table/page-state
 (fn [db [_  & args]]
   (get-in db (concat (view-path :page-state) args))))

(reg-event-db
 :table/page-state
 (fn [db [_  form]]
   (let  []
     (update-in db (view-path :page-state) merge form))))

(reg-sub
 :table/selected-table
 :<- [:table/page-state]
 :<- [:table/dataset.all]
 (fn [[page-state all-data] [_]]
   (let [table-name (get page-state :selected-table "")
         selected-table  (cond
                           (keyword? table-name) table-name
                           (string? table-name)  (keyword table-name)
                           (map? table-name) (get table-name :table_name "")
                           :default "")]
     [selected-table
      (get-in all-data [selected-table :columns] {})])))

(reg-sub
 :table/selected-field
 :<- [:table/selected-table]
 :<- [:table/page-state]
 (fn [[selected-table page-state] [_]]
   (let [[table-name table-data]   selected-table
         field-name (:selected-field page-state)]
     [table-name
      field-name
      (get table-data field-name {})])))

;;=================

(reg-sub
 :table/table-names
 :<- [:table/dataset.all]
 (fn [table [_]]
   (->> table
        keys
        set
        sort
        (map name))))

;;=================

(reg-sub
 :table/table.table
 :<- [:table/dataset.all]
 (fn [all-data [_ table-name]]
   (get-in all-data [table-name])))

(reg-sub
 :table/table.all-field
 :<- [:table/dataset.all]
 (fn [all-data [_ table-name]]
   (-> (get-in all-data [table-name :columns])
       vals)))

(reg-event-db
 :table/table.table
 (fn [db [_ table-name attrib-key attrib-value]]
   (assoc-in db  (dataset-path table-name attrib-key)  attrib-value)))

;; (reg-sub
;;  :table/table.relation
;;  :<- [:table/dataset.all]
;;  (fn [all-data [_ table-name]]
;;    (get-in all-data [table-name  :relation])))

;; (reg-event-db
;;  :table/table.relation
;;  (fn [db [_ table-name relation]]
;;    (update-in db   (dataset-path table-name :relation) conj relation)))

(reg-sub
 :table/table.fields
 :<- [:table/dataset.all]
 (fn [all-data [_ table-name field-name]]
   (get-in all-data [table-name :columns field-name])))

(reg-event-db
 :table/table.fields
 (fn [db [_ table-name field-name form]]
   (update-in  db
               (dataset-path table-name :columns field-name)
               merge form)))

(reg-sub
 :table/view.tables
 :<- [:table/dataset.all]
 (fn [all-data [_ table-name]]
   [;; vec1
    (keys all-data)
    ;; vec2
    (-> all-data
        (get-in [table-name  :columns])
        vals)]))

;;=======================

(reg-sub
 :table/relation
 (fn [db [_]]
   (get-in db (relation-path))
   ))

(reg-sub
 :table/table-relation
 :<- [:table/relation]
 :<- [:table/page-state]
 (fn [[ page-state all-relation ] [_]]
   (let [[table-name field-name] page-state]
     (filter #(and (=  (first %) table-name)  (=  (second %)  field-name)) all-relation)
     ;;
     )
   ))

(reg-event-db
 :table/relation-update
 (fn [db [_ table-name field-name relation]]
   (update-in  db
               (relation-path)
               assoc relation)))

(reg-event-db
 :table/relation-add
 (fn [db [_ relation]]
   (update-in  db
               (relation-path)
               conj relation)))
