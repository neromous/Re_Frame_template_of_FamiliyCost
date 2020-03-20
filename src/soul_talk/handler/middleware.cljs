(ns soul-talk.handler.middleware
  (:require  [re-frame.core :refer [subscribe reg-event-db
                                    dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.util.route-utils :refer [run-events
                                                 run-events-admin
                                                 logged-in?
                                                 navigate!]]
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

(reg-event-db
 :mdw/query-dataset
 (fn [db [_ model-map resp]]
   (let [dataset (get resp :dataset)
         middleware (get model-map :middleware false)
         root-path (get model-map :root-path)
         state (dissoc resp :dataset)
         dataset (if middleware
                   (into [] middleware dataset)
                   dataset)]
     (-> db
         (assoc-in (conj root-path :dataset)
                   (zipmap (map :id dataset) dataset))

         (assoc-in  (conj root-path :server-callback)  state)))))

(reg-event-db
 :mdw/replace
 (fn [db [_ model-map resp]]
   (let [root-path (get model-map :root-path)
         dataset (get resp :dataset)
         state (dissoc resp :dataset)]
     (-> db
         (assoc-in root-path state)
         (assoc-in (conj root-path :dataset) dataset)))))





(reg-event-db
 :mdw/new
 (fn [db [_ model-map resp]]
   (let [data (get resp :dataset)
         id (get data :id)
         root-path (get model-map :root-path)]
     (-> db
         (assoc-in (concat root-path [:dataset id])  resp)))))

(reg-event-db
 :mdw/del
 (fn [db [_ model-map resp]]
   (let [data (get resp :dataset)
         id (get data :id)
         root-path (get model-map :root-path)]
     (-> db
         (update-in (concat root-path [:dataset]) dissoc id)))))

(reg-event-db
 :mdw/update
 (fn [db [_ model-map resp]]
   (let [data (get resp :dataset)
         id (get data :id)
         root-path (get model-map :root-path)]
     (-> db
         (update-in (concat root-path [:dataset id]) merge data)))))







