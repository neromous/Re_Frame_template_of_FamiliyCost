 (ns soul-talk.handler.order-track
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
  (concat [:data :order-track :dataset] args))

(defn  view-path
  [& args]
  (concat [:data :order-track :views] args))

(defn dataset-state-path
  [& args]
  (concat [:data :order-track :dataset-state] args))

(defn relation-path [& args]
  (concat [:data :order-track :relation]))

(reg-event-db
 :order-track/mdw.dto
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  (dataset-path) dataset)
         (assoc-in  (dataset-state-path)   state)))))

(reg-event-fx
 :order-track/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/order-track"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:order-track/mdw.dto]}}))

(reg-sub
 :order-track/page-state
 (fn [db [_]]
   (get-in db (view-path  :page-state))))

(reg-event-db
 :order-track/page-state.replace
 (fn [db [_ form]]
   (assoc-in db (view-path  :page-state) form)))

(reg-event-db
 :order-track/page-state
 (fn [db [_ form]]
   (update-in db (view-path  :page-state) merge form)))

(reg-sub
 :order-track/data.all
 (fn [db [_]]
   (get-in db (dataset-path))))

(reg-sub
 :order-track/data.sample
 :<- [:order-track/data.all]
 (fn [all-data [_]]
   (first all-data)))

(reg-sub
 :order-track/view.table-columns
 :<- [:order-track/data.sample]
 (fn [samples [_]]
   (let [sample-keys  (keys samples)]
     (map (fn [x]  {:title (name x)
                    :dataIndex (name x)
                    :key (name x)})    sample-keys))))

(reg-sub
 :order-track/columns.unique-key
 :<- [:order-track/data.all]
 (fn [all-data [_ columns-key]]
   (->> all-data
        (map #(get % columns-key))
        set
        sort)))

(reg-sub
 :order-track/group-by
 :<- [:order-track/data.all]
 (fn [all-data [_ columns-key]]
   (group-by #(get % columns-key) all-data)))

(reg-sub
 :order-track/filter
 :<- [:order-track/data.all]
 (fn [all-data [_ query]]
   (let [query-keys  (keys query)]
     (filter #(= (select-keys % query-keys)   query) all-data))))

(reg-event-db
 :order-track/new
 (fn [db [_ form]]
   (update-in db (dataset-path)  conj form)))

(reg-event-db
 :order-track/delete
 (fn [db [_ query]]
   (let [query-keys  (keys query)]
     (update-in db  (dataset-path)
                (fn [all-data]
                  (filter #(not= (select-keys % query-keys) query) all-data))))))

(defn if-true->update [item query update-form query-keys]
  (if (= (select-keys query-keys item) query)
    (merge item update-form)
    item))

(reg-event-db
 :order-track/update
 (fn [db [_ query update-form]]
   (let [query-keys  (keys query)]
     (update-in db (dataset-path)
                (fn [all-data]
                  (map #(if-true->update % query update-form query-keys) all-data))))))
