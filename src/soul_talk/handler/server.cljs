(ns soul-talk.handler.server
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.db :refer [api-uri]]
             [soul-talk.utils :refer [mapset2map]]
             [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
             [soul-talk.model.sample :refer [meta-data]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))

(def prefix-url "http://0.0.0.0:3000/api/v1")
(reg-event-db
 :mdw/document-pull
 (fn [db [_ model-key response]]
   (let [dataset  (get-in response ["result" "dataset"])
         state  (get response "query")]
     (-> db
         (assoc-in  [:documents model-key]   dataset)
         (assoc-in  [:documents-state  model-key]   state)))))

(reg-event-fx
 :server/query-document
 (fn [_ [_ model-key query]]
   {:http {:method    POST
           :url   (str prefix-url "/entity/"  model-key)
           :ajax-map       {:params query
                            :keywords? false
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:mdw/document-pull model-key]}}))

(reg-event-db
 :mdw/ring-pull
 (fn [db [_ model-key response]]
   (let [dataset  (get response "result")
         state  (get response "query")]
     (-> db
         (assoc-in  [:dataset model-key]   dataset)
         (assoc-in  [:states  model-key]   state)))))

(reg-event-fx
 :server/query
 (fn [_ [_ model-key query]]
   {:http {:method    POST
           :url   (str prefix-url "/query/"  (name model-key))
           :ajax-map       {:params query
                            :keywords? false
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:mdw/ring-pull model-key]}}))

(reg-event-db
 :mdw/ring-new
 (fn [db [_ model-key response]]
   (let [result  (get response "result")
         id (get result "_id")]
     (-> db
         (assoc-in  [:dataset model-key id]   result)))))

(reg-event-fx
 :server/new
 (fn [_ [_ model-key query]]
   {:http {:method    POST
           :url   (str prefix-url "/add/"  (name model-key))
           :ajax-map       {:params query
                            :keywords? false
                            :format (json-request-format)
                            :response-format :json}

           :success-event [:mdw/ring-new model-key]}}))

(reg-event-db
 :mdw/ring-del
 (fn [db [_ model-key response]]
   (let [result  (get response "result")
         ids (get response "query")]
     (reduce #(update-in %1  [:dataset model-key] dissoc %2) db ids))))

(reg-event-fx
 :server/del
 (fn [_ [_ model-key ids]]
   {:http {:method    POST
           :url   (str prefix-url "/del/"  (name model-key))
           :ajax-map       {:params {"ids" ids}
                            :keywords? false
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:mdw/ring-del model-key]}}))

(reg-event-db
 :mdw/ring-update
 (fn [db [_ model-key response]]
   (let [ids  (get response "result")
         form (get response "query")]
     (reduce #(update-in %1  [:dataset model-key %2] merge form) db ids))))

;; (update-in  db [:dataset model-key id]  merge form))))
;; (update-in {:haha {:hehe {:dodo "ddd"}}}   [:haha :hehe  ] dissoc :dodo )

(reg-event-fx
 :server/update
 (fn [_ [_ model-key ids form]]
   {:http {:method    POST
           :url   (str prefix-url "/update/"  (name model-key))
           :ajax-map       {:params {"ids" ids
                                     "form" form}
                            :keywords? false
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:mdw/ring-update model-key]}}))






