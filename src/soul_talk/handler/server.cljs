(ns soul-talk.handler.server
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.db :refer [api-uri]]
             [soul-talk.utils :refer [mapset2map]]
             [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
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

;; 成本模块数据来源

(reg-event-db
 :cost/mdw.dto
 (fn [db [_ d-name  response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in [d-name :datasets]   dataset)
         (assoc-in  [d-name :state]   state)))))

(reg-event-fx
 :cost.human/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/human"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :cost.human]}}))

(reg-event-fx
 :energy.oa_report/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/energy/oa_report"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :energy.oa_report]}}))

(reg-event-fx
 :cost.material-craft/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/material-craft"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :cost.material-craft]}}))

(reg-event-fx
 :cost.material-craft/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/material-craft"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :cost.material-craft]}}))

(reg-event-fx
 :cost.material-raw/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/material-raw"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :cost.material-raw]}}))

(reg-event-fx
 :product.output/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/product-output"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :product.output]}}))

(reg-event-fx
 :product.output/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/product-output"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost/mdw.dto :product.output]}}))


(reg-event-db
 :order-track/mdw.dto
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  [:order-track :datasets] dataset)
         (assoc-in  [:order-track :state]   state)))))

(reg-event-fx
 :order-track/server.pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/order-track"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:order-track/mdw.dto]}}))

(reg-event-db
 :cost.detail/mdw-material_craft
 (fn [db [_ response]]
   (let [dataset (get-in response [:result])]
     (-> db
         (assoc-in [:views :cost.detail :data :material_craft]   dataset)))))

(reg-event-fx
 :cost.detail/material_craft-pull
 (fn [_ [_ query]]
   {:http {:method  POST
           :url   "http://0.0.0.0:3000/cost/material-craft"
           :ajax-map       {:keywords? true
                            :params query
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:cost.detail/mdw-material_craft]}}))