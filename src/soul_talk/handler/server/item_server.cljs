(ns soul-talk.handler.server.item-server
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.util.route-utils :refer [run-events
                                                 run-events-admin
                                                 logged-in?
                                                 navigate!]]
             [soul-talk.sub.funcs.orm :as orm]
             [soul-talk.sub.funcs.item-path :as item-path]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))

(reg-event-db
 :item/mdw.set-db
 (fn [db [_  item-key response]]
   (let [data-path (item-path/->data-path  item-key)
         data   (get-in response [:dataset])]

     (assoc-in db data-path data))
   ;;
   ))

(reg-event-fx
 :item/server.get
 (fn [_ [_ item-key query]]
   {:http {:method    GET
           :url   (item-path/->url item-key)
           :ajax-map       {:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:item/mdw.set-db item-key]}}))

(reg-event-db
 :item/mdw.set-db
 (fn [db [_  item-key response]]
   (let [data-path (item-path/->data-path  item-key)
         data   (get-in response [:dataset])]
     (assoc-in db data-path data))
   ;;
   ))

(reg-event-fx
 :item/server.set
 (fn [_ [_ item-key query]]
   {:http {:method    POST
           :url   (item-path/->url item-key)
           :ajax-map       {:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:item/mdw.set-db item-key]}}))

(reg-event-fx
 :item/server.pull
 (fn [_ [_ item-key query]]
   {:http {:method    POST
           :url   (item-path/->url item-key)
           :ajax-map       {:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:item/mdw.set-db item-key]}}))




