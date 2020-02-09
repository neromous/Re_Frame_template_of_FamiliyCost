(ns soul-talk.handler.resource-server
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
             [soul-talk.db :refer [model-register]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))

(reg-event-db
 :resource/dto
 (fn [db [_ model-key response]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)
         view-path (get model :view-path)
         data-state-path (concat view-path [:data-state])
         dataset  (get-in response [:result])
         state  (get response :query)
         add-data (fn [origin target]
                    (let [origin (set origin)
                          target (set target)]
                      (into origin target)))]

     (-> db
         (update-in  data-path add-data  dataset)
         (assoc-in  data-state-path state)))))

(reg-event-fx
 :resource/server.query
 (fn [_ [_ model-key query]]
   {:http {:method    POST
           :url   (get-in model-register [model-key :url])
           :ajax-map       {:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:resource/dto model-key]}}))


;;;


(reg-event-db
 :resource-api/mdw.replace
 (fn [db [_ model-key response]]
   (let [model (get model-register model-key)
         model-name (get model :model-name)
         data-path (get model :data-path)
         view-path (get model :view-path)
         data-state-path (concat view-path [:pagination])
         dataset  (get-in response [:dataset])
         dataset (-> (group-by :id dataset)
                     ((fn [x] (zipmap (keys x)  (->> x vals   (map first))))))

         pagination  (get response :pagination)]

     (-> db
         (assoc-in  data-path  dataset)
         (assoc-in  data-state-path pagination)))))

(reg-event-fx
 :resource-api/server.pull
 (fn [_ [_ model-key query]]
   {:http {:method    GET
           :url   (get-in model-register [model-key :url])
           :ajax-map       {;;:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:resource-api/mdw.replace model-key]}}))




;;


(reg-event-db
 :resource-api/mdw.add
 (fn [db [_ model-key response]]
   (let [model (get model-register model-key)
         model-name (get model :model-name)
         data-path (get model :data-path)
         view-path (get model :view-path)
         data  (first (get-in response [:dataset]))
         id (:id data)]
     (assoc-in  db (conj data-path id)  data))))

(reg-event-fx
 :resource-api/server.add
 (fn [_ [_ model-key form]]
   {:http {:method    POST
           :url    (get-in model-register [model-key :url])
           :ajax-map       {:params form
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:resource-api/mdw.add model-key]}}))


;;


(reg-event-db
 :resource-api/mdw.del
 (fn [db [_ model-key response]]
   (let [model (get model-register model-key)
         model-name (get model :model-name)
         data-path (get model :data-path)
         id   (get-in response [:dataset])
         view-path (get model :view-path)]
     (update-in  db data-path  dissoc id))))

(reg-event-fx
 :resource-api/server.del
 (fn [_ [_ model-key id]]
   {:http {:method    DELETE
           :url   (str (get-in model-register [model-key :url])  "/" id)
           :ajax-map       {;;:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:resource-api/mdw.del model-key]}}))
;;
(reg-event-db
 :resource-api/mdw.update
 (fn [db [_ model-key response]]
   (let [model (get model-register model-key)
         model-name (get model :model-name)
         data-path (get model :data-path)
         item   (get-in response [:dataset])
         view-path (get model :view-path)]
     (assoc-in  db (conj data-path id) item))))

(reg-event-fx
 :resource-api/server.update
 (fn [_ [_ model-key  {:keys [id] :as item}]]
   {:http {:method    PUT
           :url   (str (get-in model-register [model-key :url])  "/" id)
           :ajax-map       {:params item
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:resource-api/mdw.del model-key]}}))









