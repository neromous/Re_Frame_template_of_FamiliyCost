(ns soul-talk.handler.order-list
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


(reg-event-db
 :order-list/ring-pull
 (fn [db [_ model-key response]]
   (let [dataset  (get response "result")
         state  (get response "query")]
     (-> db
         (assoc-in  [:dataset model-key]   dataset)
         (assoc-in  [:states  model-key]   state)))))

(reg-event-fx
 :order-list/query
 (fn [_ [_ model-key query]]
   {:http {:method    POST
           :url   ""
           :ajax-map       {:params query
                            :keywords? false
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:mdw/ring-pull model-key]}}))

