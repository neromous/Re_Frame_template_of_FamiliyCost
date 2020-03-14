(ns soul-talk.handler.common
  (:require  [re-frame.core :refer [subscribe reg-event-db
                                    dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.util.route-utils :refer [run-events
                                                 run-events-admin
                                                 logged-in?
                                                 navigate!]]
             [soul-talk.util.db :as udb]
             [ajax.edn :refer [edn-request-format
                               edn-response-format]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))

(defn- to-name [model-key]
  (if (keyword? model-key)
    (if (nil? (namespace model-key))
      (name model-key)
      (str (namespace model-key) "/"  (name model-key)))
    model-key))

(defn- state-all [model-key model-map]
  (let [model-key (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "state"))]
    (reg-sub
     attr-name
     (fn [db [_]]
       (get-in db root-path)))))

(defn- data-all [model-key model-map]
  (let [model-key (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "data"))]
    (reg-sub
     attr-name
     (fn [db [_]]
       (get-in db (conj root-path :dataset))))))

(defn- state-get [model-key model-map]
  (let [model-key  (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "get-state"))]
    (reg-sub
     attr-name
     (fn [db [_ k]]
       (get-in db (conj root-path k))))))

(defn- state-set [model-key model-map]
  (let [model-key  (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "set-state"))]
    (reg-event-db
     attr-name
     (fn [db [_ k v]]
       (assoc-in db (conj root-path k) v)))))

(defn- set-db [model-key model-map]
  (let [model-key  (if (keyword? model-key) (name model-key) model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "set-db"))]
    (reg-event-db
     attr-name
     (fn [db [_ input-vec]]
       (assoc-in db  root-path
                 (zipmap (map :id input-vec) input-vec))))))

(defn- item-new [model-key model-map]
  (let [model-key  (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "new"))]
    (reg-event-db
     attr-name
     (fn [db [_ {:keys [id] :as form}]]
       (assoc-in db (concat root-path [:dataset id]) form)))))

(defn- item-update [model-key model-map]
  (let [model-key  (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "update"))]
    (reg-event-db
     attr-name
     (fn [db [_ id form]]
       (update-in db (concat root-path [:dataset id]) merge form)))))

(defn- item-delete [model-key model-map]
  (let [model-key  (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "del"))]
    (reg-event-db
     attr-name
     (fn [db [_ id]]
       (update-in db (concat root-path [:dataset]) dissoc id)))))

(defn- item-find-id [model-key model-map]
  (let [model-key  (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "find_id"))]
    (reg-sub
     attr-name
     (fn [db [_ id]]
       (get-in db (concat root-path [:dataset id]))))))

(defn- item-find-by [model-key model-map]
  (let [model-key  (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "find_by"))]
    (reg-sub
     attr-name
     (fn [db [_ query]]
       (->> (get-in db (concat root-path [:dataset]))
            vals
            (filter #(= query (select-keys % (keys query)))))))))

(defn- item-all [model-key model-map]
  (let [model-key  (to-name model-key)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "find_all"))]
    (reg-sub
     attr-name
     (fn [db [_ query]]
       (->> (get-in db (concat root-path [:dataset]))
            vals)))))

(defn- server-query [model-key model-map]
  (let [model-key  (to-name model-key)
        post-url (:url model-map)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "query<-"))]

    (reg-event-fx
     attr-name
     (fn [_ [_ form]]
       (let []
         {:http {:method    POST
                 :url post-url
                 :ajax-map       {:params form
                                  :keywords? true
                                  :format (json-request-format)
                                  :response-format :json}
                 :success-event [:mdw/query-dataset model-map]}})))))

(defn- server-get [model-key model-map]
  (let [model-key  (to-name model-key)
        post-url (:url model-map)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "get<-"))]

    (reg-event-fx
     attr-name
     (fn [_ [_ form]]
       (let []
         {:http {:method    GET
                 :url post-url
                 :ajax-map       {:params form
                                  :keywords? true
                                  :format (json-request-format)
                                  :response-format :json}
                 :success-event [:mdw/replace model-map]}})))))

(defn- server-post [model-key model-map]
  (let [model-key  (to-name model-key)
        post-url (:url model-map)
        root-path (get model-map :root-path)
        attr-name (keyword (str model-key "." "post<-"))]

    (reg-event-fx
     attr-name
     (fn [_ [_ form]]
       (let []
         {:http {:method    POST
                 :url post-url
                 :ajax-map       {:params form
                                  :keywords? true
                                  :format (json-request-format)
                                  :response-format :json}
                 :success-event [:mdw/replace model-map]}})))))

(defn- server<-query [model-key model-map]
  (let [model-key  (to-name model-key)
        post-url (:url model-map)
        root-path (get model-map :root-path)
        prefix-query (get model-map :query)
        attr-name (keyword (str model-key "." "datalog"))]

    (reg-event-fx
     attr-name
     (fn [_ [_ query]]
       (let [form (or  query prefix-query)]
         {:http {:method    POST
                 :url post-url
                 :ajax-map       {:params form
                                  :format (edn-request-format)
                                  :Accept "application/edn"
                                  :response-format (edn-response-format)}
                 :success-event [:mdw/replace model-map]}})))))

(defn- server<-update [model-key model-map]
  (let [model-key  (to-name model-key)
        post-url (:url model-map)
        root-path (get model-map :root-path)
        prefix-query (get model-map :query)
        attr-name (keyword (str model-key "." "transact"))]

    (reg-event-fx
     attr-name
     (fn [_ [_ query]]
       (let [form (or  query prefix-query)]
         {:http {:method    PUT
                 :url post-url
                 :ajax-map       {:params form
                                  :format (edn-request-format)
                                  :Accept "application/edn"
                                  :response-format (edn-response-format)}
                 :success-event [:mdw/replace model-map]}})))))

(defn model-register [model-key model-map]
  (do
    (state-all model-key model-map)
    (data-all model-key model-map)
    (state-get model-key model-map)
    (state-set model-key model-map)
    (set-db model-key model-map)
    (item-new model-key model-map)
    (item-delete model-key model-map)
    (item-update model-key model-map)
    (item-find-id model-key model-map)
    (item-find-by model-key model-map)
    (item-all model-key model-map)
    (server-query model-key model-map)
    (server-post model-key model-map)
    (server<-query model-key model-map)
    (server<-update model-key model-map)
    (server-get model-key model-map)))

