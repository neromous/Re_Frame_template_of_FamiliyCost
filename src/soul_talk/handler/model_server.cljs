(ns soul-talk.handler.model-server
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
             [soul-talk.db :refer [model-register]]
             [soul-talk.sub.funcs.orm :as orm]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))
;;;
(reg-event-db :model/mdw.replace orm/replace>)
(reg-event-fx
 :model/server.pull
 (fn [_ [_ model-key query]]
   {:http {:method    GET
           :url   (get-in model-register [model-key :url])
           :ajax-map       {;;:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:model/mdw.replace model-key]}}))

;;
(reg-event-db :model/mdw.add orm/add>)
(reg-event-fx
 :model/server.add
 (fn [_ [_ model-key form]]
   {:http {:method    POST
           :url    (get-in model-register [model-key :url])
           :ajax-map       {:params form
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:model/mdw.add model-key]}}))
;;

(reg-event-db :model/mdw.del orm/del>)
(reg-event-fx
 :model/server.del
 (fn [_ [_ model-key id]]
   {:http {:method    DELETE
           :url   (str (get-in model-register [model-key :url])  "/" id)
           :ajax-map       {;;:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:model/mdw.del model-key]}}))
;;
(reg-event-db :model/mdw.update orm/update>)
(reg-event-fx
 :model/server.update
 (fn [_ [_ model-key  {:keys [id] :as item}]]
   {:http {:method    PUT
           :url   (str (get-in model-register [model-key :url])  "/" id)
           :ajax-map       {:params item
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:model/mdw.update model-key]}}))




